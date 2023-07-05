# Otp View

> The library using for otp code in Android. 

<a href="https://github.com/ericktijerou/koleton/actions"><img src="https://github.com/ericktijerou/koleton/workflows/Build%20and%20test/badge.svg"/>  

An Android library that provides an easy enter and verify otp code. 

Made with ❤ by [phong](https://github.com/phong016688). 

## Installation
You can install `otp-loading-view` with `Maven Central` and `Gradle`:

```gradle
// In your module's `build.gradle`
dependencies {
    implementation 'com.github.phong016688:otp-code-android-view:0.1'
}
```

Make sure to include `maven { url 'https://jitpack.io' }` in your repositories
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}
```


## Default config: 
```
<com.otp.otp_code_view.OtpCodeView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="100dp"/>
```

[video_black.webm](https://github.com/phong016688/otp-code-android-view/assets/37899092/1c8138a9-35c7-4a12-940c-9a8af6c2f409)  

## Custom config: 
```
<com.otp.otp_code_view.OtpCodeView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:code_color="#CC171717"
        app:code_stroke_color="#185C1F"
        app:code_done_stroke_color="#1AA327"
        app:code_background_color="#667A7979"
        app:code_error_color="#A50202"
        app:code_stroke_error_color="#F11919"
        app:code_length="4"
        app:code_radius="10dp"
        app:code_stroke_width="2dp"/>
```
> Note: max length code is 6.

[video_white.webm](https://github.com/phong016688/otp-code-android-view/assets/37899092/44f7077c-95f3-4fc5-b1bf-4c101cb05b77)
   
## License

       Copyright 2020

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

           http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
