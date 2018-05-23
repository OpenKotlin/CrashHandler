# CrashHandler

## What is CrashHandler?

CrashHandler is a simply util to handle android application crash, people who want handle application crash can use this util easily and also this util is uncoupled with their own code.

## How I use it?

- Add this repo to your project

- Install CrashHandler into your Application like below:

  ```java
  class DemoApplication : Application(), CrashListener {
  
      override fun onCreate() {
          super.onCreate()
          //Register CrashHandler when application is created
          CrashHandler.of(this).install(this)
      }
  
      override fun onCrash(t: Throwable?, activity: Activity): Boolean {
          //TODO Do what you want at here(Upload crash log, recover your activity...)
          return false
      }
  }
  ```

## License

``` 
Copyright 2018 OpenKotlin.
  
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




