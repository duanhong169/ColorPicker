# ColorPicker [![gitHub release](https://img.shields.io/github/release/duanhong169/ColorPicker.svg?style=social)](https://github.com/duanhong169/ColorPicker/releases) [![platform](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ColorPicker-green.svg?style=flat)](https://android-arsenal.com/details/1/7068) <a target="_blank" href="https://android-arsenal.com/api?level=14"><img src="https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat"></a> [![license](https://img.shields.io/badge/license-Apache%202-green.svg)](https://github.com/duanhong169/ColorPicker/blob/master/LICENSE)

[English](README.md) | 中文

Android颜色选择器。通过基于HSV颜色空间的调色盘和滑块来选择颜色，支持透明度值选择。

<img src='art/screen-shot-1.png' width='32%'/> <img src='art/screen-shot-2.png' width='32%'/> <img src='art/screen-record.gif' width='32%'/>

## Gradle

```
dependencies {
    implementation 'com.github.duanhong169:colorpicker:${latestVersion}'
    ...
}
```

> 将上方的`${latestVersion}`替换为当前最新的版本号，最新版本号参见[releases](https://github.com/duanhong169/ColorPicker/releases)。

## 使用方法

### 使用弹出框`ColorPickerPopup`

```java
new ColorPickerPopup.Builder(this)
        .initialColor(Color.RED) // Set initial color
        .enableBrightness(true) // Enable brightness slider or not
        .enableAlpha(true) // Enable alpha slider or not
        .okTitle("Choose")
        .cancelTitle("Cancel")
        .showIndicator(true)
        .showValue(true)
        .build()
        .show(v, new ColorPickerPopup.ColorPickerObserver() {
            @Override
            public void onColorPicked(int color) {
                v.setBackgroundColor(color);
            }

            @Override
            public void onColor(int color, boolean fromUser) {

            }
        });
```

### 直接使用视图`ColorPickerView`

* 将`ColorPickerView`添加到需要的`layout.xml`文件中：

```xml
<top.defaults.view.ColorPickerView
    android:id="@+id/colorPicker"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    app:enableBrightness="true"
    app:enableAlpha="true"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent"/>
```

> 更多支持设置的属性请查阅[`top_defaults_view_color_picker_attrs.xml`](./colorpicker/src/main/res/values/top_defaults_view_color_picker_attrs.xml)。

* 实现`ColorObserver`观察者接口并从`ColorPickerView`订阅颜色更新事件:

```java
colorPickerView.subscribe((color, fromUser) -> {
    // use the color
});
```

* 设置选择器的初始颜色值：

```java
colorPickerView.setInitialColor(0x7F313C93);
```

* 充值为初始颜色值：

```java
colorPickerView.reset();
```

完整的示例代码请查阅项目所附app。

## License

    Copyright 2018 Hong Duan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.