package com.my.coildemo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.load
import coil.size.Precision
import coil.size.Scale
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.GrayscaleTransformation
import coil.transform.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_main.*

/**
 * scaletype可以设置给ImageView
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //静态图
        btn_static.setOnClickListener {
            iv.load(URL_STATIC) {
//                crossfade(enable = true)//淡入
                crossfade(durationMillis = 2000)//淡入时间（从placeholder慢慢变过来）
                placeholder(R.mipmap.ic_launcher)
                error(R.drawable.ic_launcher_background)
                size(100, 200)
                transformations(//变换，可以写任意多个（四个都写貌似很慢）
//                    GrayscaleTransformation(),//灰度
//                    CircleCropTransformation(),//圆形
//                    BlurTransformation(this@MainActivity), //模糊
//                    RoundedCornersTransformation(10f, 50f, 100f, 150f)//圆角
                )
            }
        }

        //gif
        val gifLoader = ImageLoader.Builder(this)
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder(context = this@MainActivity))
                } else {
                    add(GifDecoder())
                }
            }.build()
        btn_gif.setOnClickListener {
            iv.load(URL_GIF, gifLoader) {
                placeholder(R.mipmap.ic_launcher)//可
                error(R.drawable.ic_launcher_background)//可
                //gif不能用上面静态图的变换
            }
        }

        //svg
        val svgLoader = ImageLoader.Builder(this)
            .componentRegistry {
                add(SvgDecoder(this@MainActivity))
            }.build()
        btn_svg.setOnClickListener {
            iv.load(SVG_GIF, svgLoader)
        }
    }

    companion object {
        const val URL_STATIC = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F1812.img.pp.sohu.com" +
            ".cn%2Fimages%2Fblog%2F2009%2F11%2F18%2F18%2F8%2F125b6560a6ag214" +
            ".jpg&refer=http%3A%2F%2F1812.img.pp.sohu.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625624961&t=c0505c8bdfbb9d2523792a456c0cab21"
        const val URL_GIF = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn" +
            ".sohucs.com%2Fimages%2F20170826%2F7e7f8141422b4c32a7efad0bb2978c22" +
            ".gif&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999," +
            "10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625625867&t=527bd91f5ff80517c5228abf648815de"
        const val SVG_GIF = ""
    }
}