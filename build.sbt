import android.Keys._

android.Plugin.androidBuild

name := "scaloid-apidemos"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-target:jvm-1.6", "-feature")

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

proguardCache in Android ++= Seq(
  ProguardCache("org.scaloid") % "org.scaloid"
)

proguardOptions in Android ++= Seq("-dontobfuscate"
  , "-dontoptimize", "-dontwarn net.pocorall.**", "-dontwarn com.google.**"
  , "-keep class scala.collection.SeqLike", "-keep class org.scaloid.**")

libraryDependencies ++= Seq(
  "org.scaloid" %% "scaloid" % "3.6.1-10",
  "com.android.support" % "multidex" % "1.0.0"
)

dexMulti in Android := true

dexMinimizeMainFile in Android := true

dexMainFileClasses in Android := Seq(
  "com/example/android/apis/MultidexApplication.class",
  "android/support/multidex/BuildConfig.class",
  "android/support/multidex/MultiDex$V14.class",
  "android/support/multidex/MultiDex$V19.class",
  "android/support/multidex/MultiDex$V4.class",
  "android/support/multidex/MultiDex.class",
  "android/support/multidex/MultiDexApplication.class",
  "android/support/multidex/MultiDexExtractor$1.class",
  "android/support/multidex/MultiDexExtractor.class",
  "android/support/multidex/ZipUtil$CentralDirectory.class",
  "android/support/multidex/ZipUtil.class"
)

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
