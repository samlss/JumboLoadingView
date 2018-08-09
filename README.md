# JumboLoadingView
A loading(progress) view that you can select different shapes.

[![Api reqeust](https://img.shields.io/badge/api-11+-green.svg)](https://github.com/samlss/JumboLoadingView)  [![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://github.com/samlss/JumboLoadingView/blob/master/LICENSE) [![Blog](https://img.shields.io/badge/samlss-blog-orange.svg)](https://blog.csdn.net/Samlss)


![screenshot_circle](https://github.com/samlss/JumboLoadingView/blob/master/screenshots/screenshot_circle.gif)

![screenshot_square](https://github.com/samlss/JumboLoadingView/blob/master/screenshots/screenshot_square.gif)

![screenshot_triangel](https://github.com/samlss/JumboLoadingView/blob/master/screenshots/screenshot_triangel.gif)

![screenshot_star](https://github.com/samlss/JumboLoadingView/blob/master/screenshots/screenshot_star.gif)



## Use <br>
Add it in your root build.gradle at the end of repositories：
```
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```

Add it in your app build.gradle at the end of repositories:
```
dependencies {
    implementation 'com.github.samlss:JumboLoadingView:1.0'
}
```

## Attributes description：

| 属性        | 说明           |
| ------------- |:-------------:|
| circleColor      | 设置外圈圆的颜色 |
| showProgress | 显示显示进度 |
| progress | 当前进度大小(0-100) |
| progressTextColor | 设置进度字体颜色 |
| progressTextSize | 设置进度字体大小 |
| shapeColor | 旋转的形状的颜色 |
| shapeType | 旋转的形状的类型，例如circle圆圈，square正方形，triangle三角形，star星星|
| shapeStyle | 旋转的形状的填充形式，stoke描边，fill整个图形都填充颜色 |
| interpolator | 动画插值器 |

### interpolator: <br>
* AccelerateDecelerateInterpolator
* AccelerateInterpolator
* DecelerateInterpolator
* BounceInterpolator
* CycleInterpolator
* LinearInterpolator
* AnticipateOvershootInterpolator
* AnticipateInterpolator
* OvershootInterpolator

<br/>


## in layout.xml：
```
  <com.iigo.library.JumboLoadingView
            app:shapeType="star"
            app:showProgress="true"
            app:interpolator="OvershootInterpolator"
            app:progressTextColor="@color/colorPrimary"
            app:progressTextSize="18sp"
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_marginTop="20dp"
            app:circleColor="@color/colorPrimary"
            app:shapeColor="@color/colorPrimary" />
```

## in java code：
```
   jumboLoadingView.start(); //开始动画
   jumboLoadingView.stop(); //停止动画
   jumboLoadingView.release(); //释放，一般在activity的destroy()调用
   
   jumboLoadingView.setCircleColor(Color.RED); //设置外圈圆的颜色
   jumboLoadingView.setShapeColor(Color.RED); //设置形状颜色
   jumboLoadingView.setProgressTextColor(Color.RED); //设置进度字体颜色
   jumboLoadingView.setProgressTextSize(12); //设置进度字体大小
   jumboLoadingView.setProgress(99); // 设置进度大小
```


## [LICENSE](https://github.com/samlss/JumboLoadingView/blob/master/LICENSE)
