package it.czerwinski.android.charts.common.graphics

import android.graphics.Path

class InSituPathProvider : PathProvider, RectFProvider by InSituRectFProvider() {

    private val value = Path()

    override fun path(init: Path.() -> Unit): Path = value.apply {
        reset()
        init()
    }
}
