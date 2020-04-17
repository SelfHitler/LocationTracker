## LocationTracker
Android Location Tracking Library 

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Sdk](https://img.shields.io/badge/sdk-16%2B-brightgreen.svg?style=plastic)](https://android-arsenal.com/api?level=16)

Feature
-----------------
1. Location Based on Network
2. Location Based on GPS
3. Location Based on Google Fused Location Client

How To
-----------------
How does another developer add this as a dependency?

STEP 1:  Add Below lines into project-level build.gradle:    

        allprojects {
          repositories {
            // ...
            maven { url 'https://jitpack.io' }

          }
        }
        
STEP 2: Reference the library itself in your module-level build.gradle:      

        implementation "com.github.SelfHitler:LocationTracker:V1.0.0"

STEP 3: Must declare this Variable 

  ``Kotlin``

    val locationTracker = object: LocationTracker(context){

                override fun onLocationChanged(
                    networkLocation: Location?,
                    networkLocationSpeed: Double,
                    locationUpdate: LocationUpdate
                ) {
                    Log.e("${locationUpdate.name}", networkLocation?.latitude.toString())
                }

                override fun onFailure(errorMessage: ErrorMessage) {
                    Log.e("ERROR",errorMessage.name)
                }

                override fun onSuccess(successMessage: SuccessMessage) {
                    Log.e("SUCCESS",successMessage.name)
                }

            }
            
 
 ``Java``
 
     LocationTracker locationTracker = new LocationTracker(context) {
        @Override
        protected void onSuccess(@NotNull SuccessMessage successMessage) {

        }

        @Override
        protected void onFailure(@NotNull ErrorMessage errorMessage) {

        }

        @Override
        protected void onLocationChanged(@Nullable Location networkLocation, double networkLocationSpeed, @NotNull LocationUpdate locationUpdate) {
            
        }
    };



STEP 4: Start Location Update

   To get GPS location updates    
   ``startLocationTracker(LocationUpdate.GPS)``

   To get NETWORK location updates
   ``startLocationTracker(LocationUpdate.NETWORK)

   To get Google Fused location updates
   ``startLocationTracker(LocationUpdate.FUSED_LOCATION)
   
   To get All Location Updates
   ``startLocationTracker(LocationUpdate.ALL)    
   
   
STEP 5: Stop Location Update
    ``locationTracker.stopLocationTracker(LocationUpdate.ALL)``
    To stop specified update, use ``LocationUpdate.GPS`` , ``LocationUpdate.NETWORK`` , ``LocationUpdate.FUSED_LOCATION``

                       
## NOTE:
1. onDestroy make sure location update stoped or not. If you want to run in background continiously then check the demo app.
                      
## Author

👤 **Yabaze**

- FaceBook: [@MirakleYabaze](https://www.facebook.com/mirakle.yabaze)
- twitter: [@MirakleYabaze](https://twitter.com/mirakleyabaze)
- instagram: [@Yabaze1](https://www.instagram.com/yabaze1/)

License
-----------------

      Copyright (c) 2020-present, yabaze.t

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing,
      software distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.
