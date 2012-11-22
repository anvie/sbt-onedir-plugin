package com.ansvia.onedir

import sbt._
import Keys._

object OneDirPlugin extends Plugin
{
 override lazy val settings = Seq(oneDir)

 lazy val copyDependencies = TaskKey[Unit]("onedir")

 def oneDir = copyDependencies <<= (update, crossTarget, scalaVersion,
                                         internalDependencyClasspath in Compile,
                                         mainClass in Runtime, copyResources in Compile) map {
   (updateReport, out, scalaVer, deps, main_class, cress) =>
	   //cress.foreach { z => println("cres: " + z) }
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
     
     // generate executable script start.sh
     println("Generating start.sh...")
	 val projDepsStr = if (projDeps.length > 0)
		":" + projDeps.reduce(_ + ":" + _)
	 else
	 	""
     val shScript =
       """#!/usr/bin/env sh
         |java -cp "classes:lib/*%s" %s $@
       """.stripMargin.format(projDepsStr, main_class.getOrElse("")).trim + "\n"
     IO.write(out / "start.sh", shScript.getBytes)
 }

}
