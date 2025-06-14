import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.GuessResult
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix

open class BattleShipGrid(
    final override val columns: Int,
    final override val rows: Int,
    override val opponent: BattleShipOpponent,
    override var shipsSunk: BooleanArray = BooleanArray(BattleshipGrid.DEFAULT_SHIP_SIZES.size){false}
) : BattleshipGrid {

    var BattleShipGrid = MutableMatrix<GuessCell>(columns, rows) {_, _-> GuessCell.UNSET}

    override fun get(column: Int, row: Int): GuessCell {
        return BattleShipGrid[column, row]
    }

    var shootAtResult: Int = 0


    /*
     * Takes in a pair of coordinates and takes a "shot" at it, it will then return as GuessResult
     * depending on what the shot does: hits, misses or sinks a ship
     * It will then also fire a the function "fireGridChange()" is there is a change in the grid based
     * on whats the shot does
    */
    override fun shootAt(column: Int, row: Int): GuessResult {
        require(column in 0 until columns && row in 0 until rows)
        require(BattleShipGrid[column, row] == GuessCell.UNSET)

        val shipLocation = opponent.shipAt(column, row)
        var isNotSunk = false

        if(shipLocation != null) {
            shootAtResult = 2
            val index = shipLocation.index

            BattleShipGrid[column, row] = GuessCell.HIT(index)

            for (x in (shipLocation.ship.left)..(shipLocation.ship.right)) {
                for (y in (shipLocation.ship.top)..(shipLocation.ship.bottom)) {
                    if (BattleShipGrid[x, y] != GuessCell.HIT(index)) {
                        isNotSunk = true
                        break
                    }
                    if(isNotSunk) break
                }
            }

            if(!isNotSunk) {
                shipsSunk[index] = true
                shootAtResult = 3
                for (x in (shipLocation.ship.left)..(shipLocation.ship.right)) {
                    for (y in (shipLocation.ship.top)..(shipLocation.ship.bottom)) {
                        BattleShipGrid[x, y] = GuessCell.SUNK(index)
                    }
                }
                fireGridChange(column, row)
                return GuessResult.SUNK(index)
            }
            fireGridChange(column, row)
            return GuessResult.HIT(index)
        }
        else{
            BattleShipGrid[column, row] = GuessCell.MISS
            shootAtResult = 1
            fireGridChange(column, row)
            return GuessResult.MISS
        }
    }

    private val OnGridChangeListeners = mutableListOf<BattleshipGrid.BattleshipGridListener>()

    override fun addOnGridChangeListener(listener: BattleshipGrid.BattleshipGridListener) {
        if (!(listener in OnGridChangeListeners)) OnGridChangeListeners.add(listener)
    }

    override fun removeOnGridChangeListener(listener: BattleshipGrid.BattleshipGridListener) {
        OnGridChangeListeners.remove(listener)
    }

    fun fireGridChange(column: Int, row: Int){
        for(listener in OnGridChangeListeners){
            listener.onGridChanged(this, column, row)
        }
    }
}