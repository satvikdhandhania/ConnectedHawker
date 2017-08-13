# Gimbal Android Basic Sample
Minimal Gimbal Integration Example on Android. After setting up your application, place(s) and communication(s) on Gimbal Manager the code below will yield **Place Events** and **Local Notifications**.

## Before you create your Android application
Using the **Gimbal Manager**:
[https://manager.gimbal.com/](https://manager.gimbal.com/)
- create your Gimbal account 
- create an **Application** using package name **com.gimbal.hello_gimbal_android** (generates you API KEY)
- create at least one **Place** (using a Beacon or Geofence)
- create at least one **Communicate** (used for the local notification)

## In the sample Android application
- to run this sample in Android Studio choose the 'Import Project' option
- fill your API KEY into the MainActivity

Full Gimbal Docs [http://docs.gimbal.com/](http://docs.gimbal.com/)

## Marshmallow Permissions
If you are running this application on Marshmallow or higher you will need to add code for asking permissions. If you need to know how to do this , please refer to our github project  [https://github.com/gimbalinc/marshmallow-permission-example] (https://github.com/gimbalinc/marshmallow-permission-example)