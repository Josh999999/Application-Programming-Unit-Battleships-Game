package org.example.student.battleshipgame

import BattleShipGrid
import BattleShipOpponent
import mainShip
import uk.ac.bournemouth.ap.battleshiplib.*
import uk.ac.bournemouth.ap.battleshiplib.test.BattleshipTest
import uk.ac.bournemouth.ap.lib.matrix.boolean.BooleanMatrix
import kotlin.random.Random

class StudentBattleshipTest : BattleshipTest<mainShip>() {
    override fun createOpponent(
        columns: Int,
        rows: Int,
        ships: List<mainShip>
    ): BattleShipOpponent {
        return BattleShipOpponent(columns, rows,  ships)
    }

    override fun transformShip(sourceShip: Ship): mainShip {
        return mainShip(sourceShip.top, sourceShip.left, sourceShip.bottom, sourceShip.right, 1)
    }

    override fun createOpponent(
        columns: Int,
        rows: Int,
        shipSizes: IntArray,
        random: Random
    ): BattleShipOpponent {
        // Note that the passing of random allows for repeatable testing
        return BattleShipOpponent(columns, rows, listOf(mainShip(0,0, 0 , 5, 1)), shipSizes, random, true)
    }

    override fun createGrid(
        grid: BooleanMatrix,
        opponent: BattleshipOpponent
    ): BattleShipGrid {
        // If the opponent is not a StudentBattleshipOpponent, create it based upon the passed in data
        val studentOpponent =
            opponent as? BattleShipOpponent
                ?: createOpponent(opponent.columns, opponent.rows, opponent.ships.map { it as? mainShip ?: transformShip(it) })
        return BattleShipGrid(studentOpponent.columns, studentOpponent.rows, studentOpponent)
    }
}

