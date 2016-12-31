// example build configuration for projects using RACE


name := "race-client-example"
scalaVersion := "2.12.1"

// those settings are not RACE specific but recommended when running applications from within a SBT shell
fork in run := true
outputStrategy := Some(StdoutOutput)
Keys.connectInput in run := true

lazy val root = (project in file(".")).
  enablePlugins(JavaAppPackaging). // provides 'stage' task to generate stand alone scripts that can be executed outside SBT
  settings(
    mainClass in Compile := Some("gov.nasa.race.main.ConsoleMain"),  // we just use RACEs driver
    libraryDependencies ++= Seq(
      "gov.nasa.race" %% "race-core" % "1.+",
      "gov.nasa.race" %% "race-air" % "1.+",
      "gov.nasa.race" %% "race-ww" % "1.+",
      "gov.nasa.race" %% "race-ww-air" % "1.+",       // depends on race-core, race-ww, race-air but since we use those explicitly we define the dependencies
      "uk.me.g4dpz" % "predict4java" % "1.1.3",   // satellite position calculation (https://github.com/badgersoftdotcom/predict4java)
      "com.github.nscala-time" %% "nscala-time" %  "latest.release" // the famous org.joda.DateTime
    )
  )