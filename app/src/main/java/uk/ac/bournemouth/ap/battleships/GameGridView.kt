package uk.ac.bournemouth.ap.battleships

import BattleShipGrid
import BattleShipOpponent
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import uk.ac.bournemouth.ap.battleshiplib.BattleShipGridComputer
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.random.Random

@Suppress("UNREACHABLE_CODE")
open class GameGridView: View {
    constructor() : this(null) {}
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val firstPlayerPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 100f
    }
    private val secondPlayerPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 100f
    }

    private val secondPlayerPaintText: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
        textSize = 50f
    }

    private var battleShipGridPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private val battleShipGridPaint2: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    private var battleShipGridBackgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
    }

    private val columnNum: Int = 9
    private val rowNum: Int = 9
    private val boxSpacing: Float = 0f


    //Start the game instance
    private val rows: Int = 9
    private val columns: Int = 9
    private val randomGenerator = Random


    private val shipsOpponent1 = BattleShipOpponent.RandomShips(DEFAULT_SHIP_SIZES, columns, rows, randomGenerator)
    var opponent1: BattleShipOpponent = BattleShipOpponent(columns, rows, shipsOpponent1)
    var battleShipGrid1: BattleShipGridComputer = BattleShipGridComputer(columns, rows, opponent1, BooleanArray(BattleshipGrid.DEFAULT_SHIP_SIZES.size){false})

    private val shipsOpponent2 = BattleShipOpponent.RandomShips(DEFAULT_SHIP_SIZES, columns, rows, randomGenerator)
    var opponent2: BattleShipOpponent = BattleShipOpponent(columns, rows, shipsOpponent2)
    var battleShipGrid2: BattleShipGrid = BattleShipGrid(columns, rows, opponent2)


    var boxWidth = (width) / columnNum
    var boxHeigh = (height /2f) / rowNum


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val dividerLen = 40f
        (height - dividerLen / 2) / 2f
        val secondGridHeight: Float = (height + dividerLen / 2) / 2f
        width.toFloat()


        boxWidth = (width) / columnNum
        boxHeigh = (height / 2f) / rowNum

        //Draw the first grid:
        canvas?.drawRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            battleShipGridPaint2
        )

        battleShipGridBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
            strokeWidth = boxWidth * (1.2f / 10f)
        }


        battleShipGridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = Color.WHITE
            strokeWidth = 100f
        }


        for (row in 0 until rowNum) {
            for (column in 0 until columnNum) {
                //Drawing the first grid:
                var pointX1 = boxSpacing + (boxWidth * column) + boxWidth * 0.5f
                var pointY1 = boxSpacing + (boxHeigh * row) + boxHeigh * 0.5f
                var pointX2 = boxSpacing + (boxWidth * column + 1) + boxWidth * 0.5f
                var pointY2 = boxSpacing + (boxHeigh * row + 1) + boxHeigh * 0.5f
                var IndexedShip = opponent2.shipAt(column, row)?.index ?: 0
                var guessCellResult = battleShipGrid2.BattleShipGrid[column, row]

                when (guessCellResult) {

                    GuessCell.HIT(IndexedShip) -> {
                        canvas?.drawRect(pointX1, pointY1, pointX2, pointY2, battleShipGridPaint)
                        canvas?.drawText("X", pointX1, pointY1, secondPlayerPaintText)
                    }

                    GuessCell.MISS -> {
                        canvas?.drawRect(pointX1, pointY1, pointX2, pointY2, battleShipGridPaint)
                        canvas?.drawText("O", pointX1, pointY1, secondPlayerPaintText)
                    }

                    GuessCell.SUNK(IndexedShip) -> canvas?.drawRect(pointX1, pointY1, pointX2, pointY2, secondPlayerPaint)

                    else -> canvas?.drawRect(pointX1, pointY1, pointX2, pointY2, battleShipGridPaint
                    )
                }


                //Drawing the second grid:
                pointX1 = boxSpacing + (boxWidth * column) + boxWidth * 0.5f
                pointY1 = boxSpacing + (boxHeigh * row) + secondGridHeight + boxHeigh * 0.5f
                pointX2 = boxSpacing + (boxWidth * column + 1) + boxWidth * 0.5f
                pointY2 = boxSpacing + (boxHeigh * row + 1) + secondGridHeight + boxHeigh * 0.5f

                if (opponent1.shipAt(column, row) != null) canvas?.drawRect(pointX1, pointY1, pointX2, pointY2, firstPlayerPaint)
                else canvas?.drawRect(pointX1, pointY1, pointX2, pointY2, battleShipGridPaint)

                IndexedShip = opponent1.shipAt(column, row)?.index ?: 0
                guessCellResult = battleShipGrid1.BattleShipGrid[column, row]

                when (guessCellResult) {
                    GuessCell.HIT(IndexedShip) -> {
                        canvas?.drawRect(pointX1, pointY1, pointX2, pointY2, battleShipGridPaint)
                        canvas?.drawText("X", pointX1, pointY1, secondPlayerPaintText)
                    }

                    GuessCell.MISS -> {
                        canvas?.drawRect(pointX1, pointY1, pointX2, pointY2, battleShipGridPaint)
                        canvas?.drawText("O", pointX1, pointY1, secondPlayerPaintText)
                    }

                    GuessCell.SUNK(IndexedShip) -> canvas?.drawRect(pointX1, pointY1, pointX2, pointY2, secondPlayerPaint)

                    else -> {}
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    fun winningPopUp(player: String){
        //Saved code for string formatting:
        //val format = String.format("<font color=#3177a3> test1: <b>%s</b><br> test2: <b>%s</b><br> test3: <b>%s</b></font>", "value1", "value2", "value3")
        //textView.text = format
        R.id.gameView
        val popUp = PopUpFragment(player)
        popUp.show((context as AppCompatActivity).supportFragmentManager, "showPopUp")
        val textView: TextView = findViewById(R.id.pop_up_text1)
        if(player == "1") textView.text = "Player 1 wins"
        else textView.text = "Player 2 wins"

    }


    open val tocuhDetector1 = GestureDetectorCompat(context, object: GestureDetector.SimpleOnGestureListener() {

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
                        battleShipGrid1.computerShotHardDifficult()
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


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean { //Override onTouchEvent to have it forward the events to the gesture detector.
        return tocuhDetector1.onTouchEvent(event) || super.onTouchEvent(event)
    }

    companion object {
        val DEFAULT_SHIP_SIZES = intArrayOf(
            5, // Carrier
            4, // Battleship"
            3, // Cruiser"
            3, // Submarine"
            2 // Destroyer
        )
    }
}