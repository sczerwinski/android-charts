package it.czerwinski.android.charts.piechart

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.View.*
import it.czerwinski.android.charts.core.*
import it.czerwinski.android.graphics.AdvancedPath
import it.czerwinski.android.graphics.mixColors
import it.czerwinski.android.graphics.withRadialTranslation

class SimplePieChartUI @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : PieChartUI {

    private var colors = intArrayOf(Color.CYAN)
    private var selectedColors = intArrayOf(Color.BLUE)

    private var shadowColor = Color.BLACK

    private var selectedElevation = 4f

    private var sliceSpacing = 0f
    private var selectedDonutShift = 0f

    private val path = AdvancedPath()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        context?.withStyledAttributes(
            attrs = attrs,
            stylables = R.styleable.SimplePieChartUI,
            defStyleRes = defStyleRes
        ) {
            initAttrs(context)
        }
    }

    private fun TypedArray.initAttrs(context: Context?) {
        colors = getResourceId(R.styleable.SimplePieChartUI_simplePieChartUI_colors, 0)
            .takeUnless { it == 0 }
            ?.let { context?.resources?.getIntArray(it) }
                ?: colors
        selectedColors = getResourceId(R.styleable.SimplePieChartUI_simplePieChartUI_selectionColors, 0)
            .takeUnless { it == 0 }
            ?.let { context?.resources?.getIntArray(it) }
                ?: selectedColors
        shadowColor =
                getColor(R.styleable.SimplePieChartUI_simplePieChartUI_shadowColor, Color.BLACK)
        selectedElevation =
                getDimension(R.styleable.SimplePieChartUI_simplePieChartUI_selectionElevation, 4f)
        sliceSpacing =
                getDimension(R.styleable.SimplePieChartUI_simplePieChartUI_sliceSpacing, 0f)
        selectedDonutShift =
                getDimension(R.styleable.SimplePieChartUI_simplePieChartUI_selectionShift, 0f)
    }

    override fun onAttachedToView(view: View) {
        if (selectedElevation > 0) {
            view.setLayerType(LAYER_TYPE_SOFTWARE, paint)
        } else {
            view.setLayerType(LAYER_TYPE_HARDWARE, paint)
        }
    }

    override fun beforeDraw(canvas: Canvas) = Unit

    override fun draw(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        index: Int,
        startAngle: Float,
        endAngle: Float,
        selection: Float
    ) {
        val colorIndex = index % colors.size
        paint.color = mixColors(colors[colorIndex], selectedColors[colorIndex], selection)
        if (selection > 0.01f) {
            paint.setShadowLayer(
                selection * selectedElevation,
                0f,
                selection * selectedElevation,
                shadowColor
            )
        } else {
            paint.clearShadowLayer()
        }
        val middleAngle = (startAngle + endAngle) / 2
        val outerRadius = radius - selectedDonutShift - selectedElevation
        canvas.withRadialTranslation(
            distance = selection * selectedDonutShift,
            angle = middleAngle
        ) {
            path.reset()
            path.addCircleSector(
                cx = cx,
                cy = cy,
                radius = outerRadius,
                startAngle = startAngle,
                sweepAngle = endAngle - startAngle,
                inset = sliceSpacing / 2
            )
            drawPath(path, paint)
        }
    }

    override fun afterDraw(canvas: Canvas) {
        paint.clearShadowLayer()
    }
}
