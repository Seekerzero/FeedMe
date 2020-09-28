package com.example.feedme

import android.graphics.Canvas
import android.graphics.Point
import android.view.View

/**
 * Taken from android developer website
 * https://developer.android.com/guide/topics/ui/drag-drop
 */
class DragShadowBuilder(v: View) : View.DragShadowBuilder(v) {

    private val shadow = v//ColorDrawable(Color.LTGRAY)

    // Defines a callback that sends the drag shadow dimensions and touch point back to the
    // system.
    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        // Sets the width of the shadow to half the width of the original View
//        val width: Int = view.width / 2

        // Sets the height of the shadow to half the height of the original View
//        val height: Int = view.height / 2

        // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
        // Canvas that the system will provide. As a result, the drag shadow will fill the
        // Canvas.
//        shado(0, 0, width, height)
        // Sets the size parameter's width and height values. These get back to the system
        // through the size parameter.
        size.set(view.width, view.height)

        // Sets the touch point's position to be in the middle of the drag shadow
        touch.set(view.width / 2, view.height / 2)
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system constructs
    // from the dimensions passed in onProvideShadowMetrics().
    override fun onDrawShadow(canvas: Canvas) {
        // Draws the ColorDrawable in the Canvas passed in from the system.
        shadow.draw(canvas)
    }
}