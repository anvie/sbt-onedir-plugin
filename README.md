
SBT Onedir Plugin
===================

Is a sbt plugin for easily gather project deps into single dir and generate `start.sh` script to running in no time.

Installation
--------------

Place this line into global plugins `~/.sbt/plugins/build.sbt` or into specific project `project/plugins.sbt`:

	addSbtPlugin("com.ansvia" % "onedir" % "0.2")

Usage
--------

In sbt console type:

    > onedir

Output are in `target/scala-x.x.x/`

To execute (example):

    cd target/scala-2.9.1/
    sh start.sh my.app.EntryPoint

Done.

Patch and any improvement are welcome.

--robin
