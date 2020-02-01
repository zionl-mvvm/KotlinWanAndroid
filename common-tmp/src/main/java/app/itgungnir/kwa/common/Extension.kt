package app.itgungnir.kwa.common

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import app.itgungnir.kwa.common.util.glide.GlideApp
import app.itgungnir.kwa.common.widget.list_footer.FooterStatus
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jakewharton.rxbinding2.view.RxView
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import java.util.concurrent.TimeUnit

/**
 * 加载网络图片到ImageView中
 */
fun ImageView.load(url: String) = GlideApp.with(this.context)
    .load(url.replaceFirst("http://", "https://"))
    .placeholder(R.drawable.icon_developer)
    .error(R.drawable.icon_developer)
    .transform(CenterCrop(), RoundedCornersTransformation(20, 0))
    .into(this)

/**
 * 加载本地res文件夹中的图片到ImageView中（目前仅供无图模式下使用）
 */
fun ImageView.load(imgRes: Int) = GlideApp.with(this.context)
    .load(imgRes)
    .placeholder(R.drawable.icon_developer)
    .error(R.drawable.icon_developer)
    .transform(CenterCrop(), RoundedCornersTransformation(20, 0))
    .into(this)

/**
 * 根据状态渲染ListFooter的UI
 */
fun renderFooter(view: View, status: FooterStatus.Status) {
    view.backgroundColor = view.context.color(R.color.clr_divider)
    val title = view.findViewById<TextView>(R.id.footerTitle)
    val progress = view.findViewById<ProgressBar>(R.id.footerProgress)
    title.textColor = view.context.color(R.color.colorTheme)
    when (status) {
        FooterStatus.Status.PROGRESSING -> {
            title.visibility = View.GONE
            progress.visibility = View.VISIBLE
        }
        FooterStatus.Status.SUCCEED -> {
            title.visibility = View.VISIBLE
            progress.visibility = View.GONE
            title.text = "加载成功"
        }
        FooterStatus.Status.NO_MORE -> {
            title.visibility = View.VISIBLE
            progress.visibility = View.GONE
            title.text = "我是有底线的"
        }
        FooterStatus.Status.FAILED -> {
            title.visibility = View.VISIBLE
            progress.visibility = View.GONE
            title.text = "加载失败，请重试"
        }
    }
}

/**
 * dp转px
 */
fun Context.dp2px(dp: Float): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

/**
 * 获取XML配置中的颜色
 */
fun Context.color(id: Int) = ContextCompat.getColor(this, id)

/**
 * 隐藏软键盘
 */
fun View.hideSoftInput() = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
    ?.hideSoftInputFromWindow(windowToken, 0)

/**
 * 显示/隐藏控件
 */
fun View.ifShow(flag: Boolean) = apply {
    visibility = if (flag) View.VISIBLE else View.GONE
}

/**
 * 防抖动的点击事件
 */
fun View.onAntiShakeClick(milliSeconds: Long = 500L, block: (View) -> Unit) = RxView.clicks(this)
    .throttleFirst(milliSeconds, TimeUnit.MILLISECONDS)
    .subscribe { block.invoke(this) }!!

/**
 * 将HTML代码转换成带样式的字符串
 */
fun html(html: String): String = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()