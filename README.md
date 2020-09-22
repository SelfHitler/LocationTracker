## LocationTracker
Android Location Tracking Library 

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Sdk](https://img.shields.io/badge/sdk-16%2B-brightgreen.svg?style=plastic)](https://android-arsenal.com/api?level=16)
[![deepcode](https://www.deepcode.ai/api/gh/badge?key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwbGF0Zm9ybTEiOiJnaCIsIm93bmVyMSI6IlNlbGZIaXRsZXIiLCJyZXBvMSI6IkxvY2F0aW9uVHJhY2tlciIsImluY2x1ZGVMaW50IjpmYWxzZSwiYXV0aG9ySWQiOjIyOTMwLCJpYXQiOjE2MDA4MDAxODN9.udpEukBia4PKVlUUhjNO0oCjZHPrJ6M9m3Dk7Z9S55c)](https://www.deepcode.ai/app/gh/SelfHitler/LocationTracker/_/dashboard?utm_content=gh%2FSelfHitler%2FLocationTracker)

Feature
-----------------
1. Location Based on Network
2. Location Based on GPS
3. Location Based on Google Fused Location Client
4. Movement Speed Based on Previous Location (Harversine Algorithm used)

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

STEP 4: Check Location Permission
      
      ``Kotlin``
      val permissionCheck = PermissionCheck(context)
      permissionCheck.checkPermission(permissionCheck.LOCATION_PERMISSIONS)
      
      ``Java``
      PermissionCheck permissionCheck = PermissionCheck(context);
      permissionCheck.checkPermission(permissionCheck.LOCATION_PERMISSIONS);

      use permissionCheck.STORAGE_AND_CAMERA_PERMISSIONS -> for storage and camera
      PermissionCheck.STORAGE_PERMISSIONS -> only for storage
      PermissionCheck.CAMERA_PERMISSIONS -> only for Camera
      
      in Android Q only for storage add below lines in manifest file
      
          <application
               android:requestLegacyExternalStorage="true"
               ....>
               ...
          </application>     
      

STEP 5: Start Location Update

   To get GPS location updates    
   
        startLocationTracker(LocationUpdate.GPS)

   To get NETWORK location updates
   
        startLocationTracker(LocationUpdate.NETWORK)

   To get Google Fused location updates
   
        startLocationTracker(LocationUpdate.FUSED_LOCATION)
   
   To get All Location Updates
   
        startLocationTracker(LocationUpdate.ALL)
   
   
STEP 6: Stop Location Update
    
    locationTracker.stopLocationTracker(LocationUpdate.ALL)
    
    To stop specified update, use LocationUpdate.GPS , LocationUpdate.NETWORK , LocationUpdate.FUSED_LOCATION

STEP 7: Other Feature
        To Find Distance between two location 
    
           HaversineAlgorithm().HaversineInM( lat1: Double, long1: Double,  lat2: Double, long2: Double)
    
           HaversineAlgorithm().HaversineInKM( lat1: Double, long1: Double,  lat2: Double, long2: Double)
    
## NOTE:
1. onDestroy make sure location update stoped or not. If you want to run in background continiously then check the demo app.
                      
## Author

👤 **Yabaze**

- FaceBook: [@MirakleYabaze](https://www.facebook.com/mirakle.yabaze)
- twitter: [@MirakleYabaze](https://twitter.com/mirakleyabaze)
- instagram: [@Yabaze1](https://www.instagram.com/sound_of.heart/)

License
-----------------

      MIT License

      Copyright (c) 2020 SelfHitler

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in all
        copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
        SOFTWARE.`
