# Android系统图片选择工具封装

标签（空格分隔）： Android

---
原文链接：https://www.zybuluo.com/Tyhj/note/1307477

图片选择经常用到，系统自带的图片选择挺好的，写起来不难，但是比较麻烦，所以自己集成了一个库，方便使用

### 集成方法：
Step 1. Add the JitPack repository to your build file
```
//Add it in your root build.gradle at the end of repositories:
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```
//Add the dependency
dependencies {
	        implementation 'com.github.tyhjh:picturePickUtil:v1.0.2'
	}
```


### 使用例子
```java
PicturePickUtil.pick(MainActivity.this, new OnPickListener() {
        @Override
        public void pickPicture(File file) {
                iv_picture.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        }
});
```
### 效果图
界面非常简洁，打开相机或者相册来选择图片
![屏幕快照 2018-10-11 下午8.50.09.png-15.3kB][1]



### 具体使用
首先Android 7.0以上在应用间共享**Uri**必须使用**FileProvider**，所以必须先配置在AndroidManifest中配置provider，由于每个应用的provider的**authorities**是不可以一样的，所以需要单独配置

```xml
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="yourName"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/provider_paths" />
</provider>
```
**@xml/provider_paths**文件库里面有写，不用再生成，但是**authorities**的值需要用到，需要保存

```java
//保存authority值
PicturePickUtil.init("yourName");
//设置图片长宽(作为参考不会修改图片比例)和文件大小，进行压缩
PicturePickUtil.setPictureSize(500, 500, 500);
//设置裁剪的比例，设置为null则可手动随意裁剪
PicturePickUtil.setPictureScale(1, 1);

```


  [1]: http://static.zybuluo.com/Tyhj/3a9tb8o8fjuyyjh1rms9vxlu/%E5%B1%8F%E5%B9%95%E5%BF%AB%E7%85%A7%202018-10-11%20%E4%B8%8B%E5%8D%888.50.09.png