# CrashHandler

[![Build Status](https://api.travis-ci.org/OpenKotlin/CrashHandler.svg?branch=CrashHandle)](https://github.com/OpenKotlin/CrashHandler)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/openkotlin/maven/CrashHandler/images/download.svg) ](https://bintray.com/openkotlin/maven/CrashHandler/_latestVersion)

## What is CrashHandler?

CrashHandler is a simply util to handle android application crash, people who want handle application crash can use this util easily and also this util is uncoupled with their own code.

## How to use it?

- Add Jcenter to your project

  ```groovy
  repositories {
      jcenter()
      // your own repositries...
  }
  ```

- Add CrashHandler to your project

  ```groovy
  compile 'org.openkotlin.library:crashhandler:1.0.0@aar'
  ```

- Install CrashHandler into your Application like below:

  ```kotlin
  class DemoApplication : Application(), CrashListener {
  
      override fun onCreate() {
          super.onCreate()
          //Register CrashHandler when application is created
          CrashHandler.of(this).install(this, false)
      }
  
      override fun handleCrashInUiThread(t: Throwable?, activity: Activity) {
          //Do things in UiThread(toast crash info, recover activity...)
      }
  
      override fun handleCrashInAsync(t: Throwable?) {
          //Do async task at here(Upload crash info, write log file...)
      }
  }
  ```

## License

```
Copyright 2019 OpenKotlin.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
