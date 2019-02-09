package it.czerwinski.android.charts.common

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes

internal fun View.withStyledAttributes(
    attrs: AttributeSet?,
    @StyleableRes stylables: IntArray,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
    initAttrs: TypedArray.() -> Unit
) {
    context?.withStyledAttributes(attrs, stylables, defStyleAttr, defStyleRes, initAttrs)
}

internal fun Context.withStyledAttributes(
    attrs: AttributeSet?,
    @StyleableRes stylables: IntArray,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
    initAttrs: TypedArray.() -> Unit
) {
    val attrsArray = obtainStyledAttributes(attrs, stylables, defStyleAttr, defStyleRes)
    if (attrsArray != null) {
        attrsArray.initAttrs()
        attrsArray.recycle()
    }
}
