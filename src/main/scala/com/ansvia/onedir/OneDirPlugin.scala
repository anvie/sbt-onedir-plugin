package com.ansvia.onedir

import sbt._
import Keys._
import _root_.sbt.Defaults.{packageTasks, packageBinTask, inDependencies}

object OneDirPlugin extends Plugin
{

 lazy val copyDependencies = TaskKey[Unit]("onedir")
 val javaOpts = SettingKey[String]("onedir-java-opts")

 lazy val onedirSettings = Seq(
    javaOpts := "",
    copyDependencies <<= (update, crossTarget, scalaVersion,
                         internalDependencyClasspath in Compile,
                         mainClass in Runtime, copyResources in Compile, javaOpts, sourceDirectory) map {
       (updateReport, out, scalaVer, deps, main_class, cress, jopts, sd) =>
         var projDeps = Array[String]()
         deps.foreach {
           depsDir =>
             val x = depsDir.data.getCanonicalPath.split("/")
             if (x.length > 3){
               val projName = x(x.length - 3)
               println("Copying `" + out / "lib" / projName + "`...")
               IO.copyDirectory(depsDir.data, out / "lib" / projName, preserveLastModified = true)
               projDeps :+= "lib/" + projName
             }
         }
         updateReport.allFiles foreach {
           srcPath =>
             val destPath = out / "lib" / srcPath.getName
             println("Copying `" + destPath + "`...")
             IO.copyFile(srcPath, destPath, preserveLastModified = true)
         }
         

         val webappDir = sd / "main" / "webapp"
         if (webappDir.exists && webappDir.isDirectory){
             println("Copying webapp...")
             IO.copyDirectory(webappDir, out / "webapp")
         }

         // val wress = inDependencies(sourceDirectory(sd => Seq(sd / "webapp")), ref => Nil, false) apply {_.flatten}
         // val warPath = target / "webapp"
         // val wcToCopy = for {
         //                 dir <- wress
         //                 file <- dir.descendentsExcept("*", filter).get
         //                 val target = Path.rebase(dir, warPath)(file).get
         //             } yield {
         //                 println("webapp: " + file)
         //                 println("target: " + target)
         //                 (file, target)
         //             }
         // 
         // val copiedWebapp = IO.copy(wcToCopy)
         // println("copiedWebapp: " + copiedWebapp.toList)

         // generate executable script start.sh
         println("Generating start.sh...")
    	 val projDepsStr = if (projDeps.length > 0)
    		":" + projDeps.reduce(_ + ":" + _)
    	 else
    	 	""

         val shScript =
           """#!/usr/bin/env sh
             |java %s -cp "classes:lib/*%s" %s $@
           """.stripMargin.format(jopts, projDepsStr, main_class.getOrElse("")).trim + "\n"
         IO.write(out / "start.sh", shScript.getBytes)
     }
 )

}
