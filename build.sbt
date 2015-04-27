import android.Keys._

android.Plugin.androidBuild

name := "scaloid-apidemos"

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-target:jvm-1.6", "-feature")

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

proguardCache in Android ++= Seq(
  ProguardCache("org.scaloid") % "org.scaloid"
)

proguardOptions in Android ++= Seq("-dontobfuscate"
  , "-dontoptimize", "-dontwarn net.pocorall.**", "-dontwarn com.google.**"
  , "-keep class scala.collection.SeqLike", "-keep class org.scaloid.**")

libraryDependencies ++= Seq(
  "org.scaloid" %% "scaloid" % "4.0-RC1",
  "com.android.support" % "multidex" % "1.0.1"
)

dexMulti in Android := true

dexMinimizeMainFile in Android := true

dexMainFileClasses in Android := IO.readLines(baseDirectory.value / "MainDexList.txt")

apkbuildExcludes in Android ++= Seq(
  "META-INF/MANIFEST.MF",
  "META-INF/LICENSE.txt",
  "META-INF/LICENSE",
  "META-INF/NOTICE.txt",
  "META-INF/NOTICE"
)

run <<= run in Android

install <<= install in Android

retrolambdaEnable in Android := false
