# Android ApiDemos rewritten elegantly

A Scala port of well-known ApiDemos for Android. This project demonstrates effectiveness of [Scaloid](https://github.com/pocorall/scaloid/) library.

Using [Scaloid](https://github.com/pocorall/scaloid/), Android apps can be written much simpler. Compare with our [Scala version](https://github.com/pocorall/scaloid-apidemos/blob/master/src/main/java/com/example/android/apis/app/ActionBarDisplayOptions.scala) of `ActionBarDisplayOptions` and original [Java version](http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android-apps/4.1.1_r1/com/example/android/apis/app/ActionBarDisplayOptions.java).

## How to build

This is a maven project. Issue `mvn package` to build, and `mvn android:deploy` to deploy on your virtual device.

### Requirements

Scaloid-ApiDemos requires Android API Level 16 and above.
Please note that this is just a requirement of Scaloid-ApiDemos project.
**Scaloid supports Android API Level 8 and above.**

### Troubleshooting

#### Error `ANDROID-904-002: Found aidl files: Count = 0`
The environment variable `ANDROID_HOME` is incorrect.

### Roadmap

* **Completely move the code from Java to Scala** <br/>
  Currently, the code did not completely ported into Scala yet.
  
### License

This software is licensed under the [Apache 2 license](http://www.apache.org/licenses/LICENSE-2.0.html).
