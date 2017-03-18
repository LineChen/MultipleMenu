# MultipleMenu


### 效果图

![imagge](https://github.com/LineChen/MultipleMenu/blob/master/multiple_menu.gif)

### 自定义属性

| 属性名 | 说明 | 默认值 |
|--------|--------|--------|
|    mm_tabHolderHeight    |    tab所在layout高度,必填    |	  48dp  |
|    mm_tabHolderColor    |    tablayout背景色    |	  0xffffffff  |
|    mm_underLineHeight    |   tablayout底部线条   |	  0.6dp  |
|    mm_underLineColor    |    tablayout底部线条颜色    |	  0xffe0e0e0  |
|    mm_tabTitleDefaultColor    |    tabtitle默认颜色    |	  0xff252525  |
|    mm_tabTitletSelectedColor    |   tabtitle选中颜色    |	  0xff5da6f0  |
|    mm_tabTitleDefaultSize    |   tab默认字体大小    |	  14dp  |
|    mm_tabTitleSelectedSize    |   tab选中字体大小    |	  14dp  |
|    mm_tabIconDefault    |    tab未选中时icon   |	  R.mipmap.tab_icon_default  |
|    mm_tabIconSelected    |    tab选中时icon   |	  R.mipmap.tab_icon_selected  |
|    mm_tabIconLeftmargin    |    icon左边距   |	  6dp  |
|    mm_dividerWidth    |    tab 间隔线宽度   |	  0.6dp  |
|    mm_dividerTopmargin    |   tab 间隔线上边距    |	  8dp  |
|    mm_dividerBottommargin    |   tab 间隔线下边距    |	  8dp  |
|    mm_dividerColor    |  tab 间隔线颜色   |	  0xffe0e0e0  |
|    mm_maskColor    |    阴影颜色   |	  0x40000000  |
|    mm_menuAnimateIn    |    menu进入动画    |	  R.anim.scale_in  |
|    mm_menuAnimateOut    |   menu退出动画   |	  R.anim.scale_out  |


### 使用

####注意：menu_layout和content_view的父控件使用FrameLayout或RelativeLayout，content_view设置topmargin为menu_layout的高度。

```java
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="50dp"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="center"
            android:text="多选菜单"
            android:textSize="20sp" />

    </LinearLayout>

    <com.beiing.library.MultipleMenu
        android:id="@+id/multiple_menu"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:mm_tabHolderHeight="50dp"
        app:mm_underLineHeight="2dp"
        app:mm_underLineColor="#FF09A63B"
        app:mm_tabTitleDefaultColor="#FF09A63B"
        app:mm_tabTitletSelectedColor="#dc0f57"
        app:mm_tabTitleDefaultSize="15sp"
        app:mm_tabTitleSelectedSize="16sp"
        app:mm_tabIconDefault="@mipmap/ic_default"
        app:mm_tabIconSelected="@mipmap/ic_selected"
        app:mm_tabIconLeftmargin="10dp"
        app:mm_dividerWidth="1dp"
        app:mm_dividerTopmargin="10dp"
        app:mm_dividerBottommargin="10dp"
        app:mm_dividerColor="#882DB91B"
        app:mm_maskColor="#889a9ae6"
        app:mm_menuAnimateIn="@anim/scale_in"
        app:mm_menuAnimateOut="@anim/scale_out">
    </com.beiing.library.MultipleMenu>


</FrameLayout>

```

代码：

```java

private List<MultipleMenu.MenuPage> initMenuPages(){
        ListView timeMenu = new ListView(this);
        timeMenu.setBackgroundColor(Color.WHITE);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, times);
        timeMenu.setAdapter(timeAdapter);

        ListView kindMenu = new ListView(this);
        kindMenu.setBackgroundColor(Color.WHITE);
        ArrayAdapter<String> kindAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, kinds);
        kindMenu.setAdapter(kindAdapter);

        ListView priceMenu = new ListView(this);
        priceMenu.setBackgroundColor(Color.WHITE);
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, prices);
        priceMenu.setAdapter(priceAdapter);

        List<MultipleMenu.MenuPage> menuPages = new ArrayList<>(3);
        menuPages.add(new MultipleMenu.MenuPage("时间", timeMenu));
        menuPages.add(new MultipleMenu.MenuPage("户型", kindMenu));
        menuPages.add(new MultipleMenu.MenuPage("价格", priceMenu));
        return menuPages;
    }

```

```java
multipleMenu.setMultipleMenu(initMenuPages());

```

##### 其他方法

`closeMenu();` : 关闭当前打开菜单
`setTabTitle(String text)`：设置当前打开菜单tabtitle
`setTbaTitle(int position, String text)`:设置指定位置tabtitle



```
   Copyright 2017 LineChen <15764230067@163.com>

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

















