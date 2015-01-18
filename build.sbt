import android.Keys._

android.Plugin.androidBuild

name := "scaloid-apidemos"

scalaVersion := "2.11.5"

proguardCache in Android ++= Seq(
  ProguardCache("org.scaloid") % "org.scaloid"
)

proguardOptions in Android ++= Seq("-dontobfuscate"
  , "-dontoptimize", "-dontwarn net.pocorall.**", "-dontwarn com.google.**"
  , "-keep class scala.collection.SeqLike", "-keep class org.scaloid.**")

libraryDependencies ++= Seq("org.scaloid" %% "scaloid" % "3.6.1-10")

scalacOptions in Compile += "-feature"

run <<= run in Android

install <<= install in Android
