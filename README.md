# JumboLoadingView
A loading(progress) view that you can select different shapes.

### [中文](https://github.com/samlss/JumboLoadingView/blob/master/README-ZH.md)

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

| attr        | description           |
| ------------- |:-------------:|
| circleColor      | set the color of the bigger circle |
| showProgress | set if show progress, the default is not to show |
| progress | set progress(0-100) |
| progressTextColor | set the color of progress text |
| progressTextSize | set the size of progress text |
| shapeColor | set the color of shape |
| shapeType | set the type of shape，includes: circle，square，triangle，star |
| shapeStyle | style of the shape，includes: stoke，fill |
| interpolator | the animation interpolator |

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
   jumboLoadingView.start(); //start animation
   jumboLoadingView.stop(); //stop animation
   jumboLoadingView.release(); //release，generally call in the activity's destroy().
   
   jumboLoadingView.setCircleColor(Color.RED); //set the color of the bigger circle
   jumboLoadingView.setShapeColor(Color.RED); //set the color of shape
   jumboLoadingView.setProgressTextColor(Color.RED); //set the color of progress text
   jumboLoadingView.setProgressTextSize(12); //set the size of progress text
   jumboLoadingView.setProgress(99); // set progress
```


## [LICENSE](https://github.com/samlss/JumboLoadingView/blob/master/LICENSE)
