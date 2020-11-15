这个例子在于说明：在有些手机上

在act的layout里如果当前需要绘制的ui没有铺满整个屏幕（就是ui中有不需要绘制的区域，比如只有一个viewgroup没背景，有个view只占屏幕一半，那另一半就没有绘制），
如果设置了application的style有：<item name="android:windowBackground">@null</item>
那么就会导致UI绘制混乱
