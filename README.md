# Android状态栏适配

### Android手机适配一直是一个很头疼的问题，因为其机型之多，厂商也各种修改，导致适配起来相对麻烦；结合项目需要，本项目针对手机状态栏进行适配。可以任意修改状态栏颜色，字体颜色会根据状态颜色切换为白色或者黑色两种。

1. 让项目中的BaseActivity继承CompatStatusBarActivity，BaseActivity里面作各自的封装就好。

2. 修改AndroidMainifest.xml里面的主题样式android:theme="@style/AppTheme"：

   ```
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="android:windowNoTitle">true</item>
    </style>
   ```

3. 在Activity中设置：

   ```
   setStatusBarColor(isImmersion, isDarkColor, statusBarColor);
   ```

   isImmersion：是否为沉浸式状态栏

   isDarkColor：状态栏颜色是否为深色

   statusBarColor：需要设置的状态栏颜色
   
   可以直接在BaseActivity里面设置一次，如果需要单独修改，需要在当前Activity里面再设置一次就可！

5. 下面需要依赖：

   - 在项目根目录的build.gradle文件里添加：

     ```
     allprojects {
     		repositories {
     			...
     			maven { url 'https://jitpack.io' }
     		}
     	}
     ```

   - 在app目录下的build.gradle文件里添加：

     ```
     dependencies {
     	        implementation 'com.github.gh-zhang-quan:XStatusBar:1.0.3.1'
     	}
     ```

   ##### 如有疑问请联系：zhang_quan_888@163.com,对你有所帮助的话请star!!!
