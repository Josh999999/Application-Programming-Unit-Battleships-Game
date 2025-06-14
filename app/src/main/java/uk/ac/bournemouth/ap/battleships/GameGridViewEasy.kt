package uk.ac.bournemouth.ap.battleships

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import java.io.PrintWriter
import java.io.StringWriter

open class GameGridViewEasy: GameGridView {
    constructor() : this(null) {}
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override val tocuhDetector1 = GestureDetectorCompat(context, object: GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean = true

        override fun onSingleTapUp(e: MotionEvent): Boolean {

            println("onSingleTapUp")

            if(!battleShipGrid1.isFinished && !battleShipGrid2.isFinished) {
                val columnTouched = (e.x / boxWidth).toInt()
                val rowTouched = (e.y / boxHeigh).toInt()

                if (columnTouched in 0 until battleShipGrid2.columns
                    && rowTouched in 0 until battleShipGrid2.rows
                ) {
                    try {
                        battleShipGrid2.shootAt(columnTouched, rowTouched)
                        if (battleShipGrid2.isFinished) {
                            winningPopUp("1")
                        }
                        battleShipGrid1.computerShotEasyDifficult()
                        if (battleShipGrid1.isFinished) {
                            winningPopUp("2")
                        }

                    } catch (e: Exception) {
                        println("Excepgtion caught")
                        println(e)
                        val stacktrace =
                            StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString()
                                .trim()
                        println("Exception caught: $stacktrace")
                    }
                    invalidate()
                    return true
                } else {
                    return false
                }
            }
            return super.onSingleTapConfirmed(e)
        }
    })

}