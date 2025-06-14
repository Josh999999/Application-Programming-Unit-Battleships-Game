package uk.ac.bournemouth.ap.battleshiplib

import BattleShipGrid
import BattleShipOpponent
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate


class BattleShipGridComputer(
    columns: Int,
    rows: Int,
    opponent: BattleShipOpponent,
    override var shipsSunk: BooleanArray = BooleanArray(BattleshipGrid.DEFAULT_SHIP_SIZES.size){false}
) : BattleShipGrid(columns, rows, opponent, shipsSunk) {
    /*
     * This class will be used to get the computer to make educated guesses on where to make
     * the next shots.
     * It will have variables to keep track of the previous shot, what the previous shot was (HIT / MISS)
     * If it is a hit, it will attempt to shoot in different directions until the ship is sunk, it will also
     * have code to switch back to random shots if it gets a miss in all 4 directions after its first hit
     * It will have a variable to keep track of the initial shot and reverse the direction from that point if
     * it gets more hits but not a sink once it gets to the end of the ship (it will have hit the ship in the
     * middle)
     * There will also a list of directions (Coordinate's) that will contain the number for the x, y coordinates
     * in the array to be incremented by
     * The shoot at function will have to be implemented inside of the computer guess algorithm to make sure it
     * knows wether the shot was a hit or a miss and can make a decision
     * Make sure directonFound and Random guess are never the same value
    */

    var currentCord: Coordinate = Coordinate(y = 0, x = 0)
    var originCord: Coordinate = Coordinate(y = 0, x = 0)
    var directionFound: Boolean = false
    var directionindex: Int = 0

    private var radomGuessing: Boolean = true
    private val directionList: List<Coordinate> = listOf(
        Coordinate(y = 0, x = 1),
        Coordinate(y = 0, x = -1),
        Coordinate(y = 1, x = 0),
        Coordinate(y = -1, x = 0)
    )

    var startingXGuess = 0
    var startingYGuess = 0
    var nextXGuess = 0
    var nextYGuess = 0


    /*
     * Takes the variables back to their default setting
     * This is used when a ship has been sunk, or the current parameters becomes to complex to continue with their course
     * On the next run-through of the algorithm this will ensure it runs the random algorithm akin to in the first running
    */
    private fun backToRandom(){
        radomGuessing = true
        directionindex = 0
        directionFound = false
        startingXGuess = 0
        startingYGuess = 0
        nextXGuess = 0
        nextYGuess = 0
    }


    /*
     * Takes a shot at the grid choosing the shooting point and random
     * This will choose points on the board until it chooses a square that is equal to GuessCell.UNSET()
     * (GuessCell.UNSET() occurs when shootAtResult != 0)
    */
    private fun shotAtRandom(){
        lateinit var shotAtResult: GuessResult
        do {
            startingXGuess = (0 until columns).random()
            startingYGuess = (0 until rows).random()
            try {
                shotAtResult = shootAt(startingXGuess, startingYGuess)
            } catch (e: Exception){
                shotAtRandom()
            }
        } while(shootAtResult == 0) //RECURSION POINT!!(make sure to error check frequently)
        val shipIndex = (opponent.shipAt(startingXGuess, startingYGuess))?.index ?: -1
        when(shotAtResult) {
            //Switch for Miss:
            GuessResult.MISS, GuessResult.SUNK(shipIndex) -> return

            //Switch for HIT:
            GuessResult.HIT(shipIndex) -> {
                radomGuessing = false
                originCord = Coordinate(y = startingYGuess, x = startingXGuess)
                currentCord = originCord
            } else -> return
        }
    }


    /*
     * Goes through different directions around a point that has been hit by the random function
     * Once the direction has been found directionFound will be set to true and the algorithm will continue
     * on that path in the main function
    */
    private fun shotAtDirection(){
        lateinit var shotAtResult: GuessResult
        var excpetionCaught = false

        if (directionindex >= directionList.size){
            backToRandom()
            shotAtRandom()
        }
        else {
            nextXGuess = currentCord.x + directionList[directionindex].x
            nextYGuess = currentCord.y + directionList[directionindex].y
            try {
                shotAtResult = shootAt(nextXGuess, nextYGuess)
            } catch(e: Exception){
                excpetionCaught = true
            }
            val shipIndex = (opponent.shipAt(nextXGuess, nextYGuess))?.index ?: -1
            try {
                if (shipIndex in 0 until opponent.ships.size) {
                    if (shotAtResult == GuessResult.HIT(shipIndex)) {
                        currentCord = Coordinate(y = nextYGuess, x = nextXGuess)
                        directionFound = true
                        radomGuessing = false
                    } else directionindex++
                }
                else{
                    if (excpetionCaught) {
                        directionindex++
                        shotAtDirection() //RECURSION POINT HERE!!!!!!!!!!!!!!!!!!! (make sure to error check frequently)
                    } else directionindex++
                }
            }
            catch (e: Exception){
                if (excpetionCaught) {
                    directionindex++
                    shotAtDirection() //RECURSION POINT HERE!!!!!!!!!!!!!!!!!!! (make sure to error check frequently)
                } else directionindex++
            }
        }
    }


    /*
     * Goes through the grid in it's current state and checks to see if they are any cells that are hit
     * This function is run when the algorithm isn't alreay persuing a direction where they its found to be
     * a hit cell
     * The is usefull for when ships overlap and the algoithm accesidently finds more than one ship when checking
     * the direction of the current ship (allows it to go back and check again)
    */
    private fun checkOverlap(): Boolean{
        for(x in 0 until columns){
            for(y in 0 until rows){
                val shipIndex = (opponent.shipAt(x, y))?.index ?: -1
                try {
                    if (BattleShipGrid[x, y] == GuessCell.HIT(shipIndex)) {
                        currentCord = Coordinate(y = y, x = x)
                        originCord = currentCord
                        directionFound = false
                        radomGuessing = false
                        directionindex = 0
                        return true
                    }
                } catch (e: Exception){}
            }
        }
        return false
    }


    /*
     * The main algorithm / flow for the "computerShotHardDifficult()" method
    */
    fun computerShotHardDifficult(){
        lateinit var shotAtResult: GuessResult
        var excpetionCaught = false

        //Check the random variable:
        if(radomGuessing){
            val isOverlap: Boolean = checkOverlap()

            if(isOverlap) {
                isOverlap
                shotAtDirection()
            }
            else shotAtRandom()
        }

        else{
            if (directionindex >= directionList.size){
                backToRandom()
                shotAtRandom()
            }
            nextXGuess = currentCord.x + directionList[directionindex].x
            nextYGuess = currentCord.y + directionList[directionindex].y
            if(directionFound){
                //If the direction is found continue on that path
                if(nextXGuess in 0 until columns && nextYGuess in 0 until rows) {
                    try {
                        shotAtResult = shootAt(nextXGuess, nextYGuess)
                    } catch(e: Exception){
                        excpetionCaught = true
                    }
                    val shipIndex = (opponent.shipAt(nextXGuess, nextYGuess))?.index ?: -1

                    try {
                        when (shotAtResult) { //Different senarios for HIT / MISS / SUNK
                            //Switch for SUNK:
                            GuessResult.SUNK(shipIndex) -> backToRandom()

                            //Switch for HIT:
                            GuessResult.HIT(shipIndex) -> currentCord =
                                Coordinate(y = nextYGuess, x = nextXGuess)

                            //Switch for MISS
                            GuessResult.MISS -> {
                                if (directionindex % 2 == 1) backToRandom()
                                else {
                                    currentCord = originCord
                                    directionindex++
                                }
                            }
                            else -> directionindex++
                        }
                    }
                    catch(e: Exception) {
                        if (excpetionCaught && directionindex % 2 == 1) {
                            backToRandom()
                            shotAtRandom()
                        } else {
                            currentCord = originCord
                            directionindex++
                        }
                    }

                } else {
                    val valCurPos = BattleShipGrid[currentCord.y, currentCord.x]
                    val shipIndex = (opponent.shipAt(currentCord.x, currentCord.y))?.index ?: -1

                    if(shipIndex != -1 && valCurPos == GuessCell.SUNK(shipIndex)) {
                        backToRandom()
                        shotAtRandom()
                    }

                    else if(shipIndex != -1 && valCurPos == GuessCell.HIT(shipIndex)){
                        if(directionindex % 2 == 1){
                            backToRandom()
                            shotAtRandom()
                        }
                        else {
                            currentCord = originCord
                            directionindex++
                            nextXGuess = currentCord.x + directionList[directionindex].x
                            nextYGuess = currentCord.y + directionList[directionindex].y

                            try {
                                shotAtResult = shootAt(nextXGuess, nextYGuess)
                            } catch (e: Exception) {
                                excpetionCaught = true
                            }

                            try {
                                when (shotAtResult) {
                                    //Switch for SUNK:
                                    GuessResult.MISS, GuessResult.SUNK(shipIndex) -> backToRandom()

                                    //Switch for HIT:
                                    GuessResult.HIT(shipIndex) -> {
                                        directionFound = true
                                        currentCord = Coordinate(y = nextYGuess, x = nextXGuess)
                                    }
                                    else -> return
                                }
                            }
                            catch (e: Exception){
                                return
                            }
                        }
                    }

                    else {
                        backToRandom()
                        shotAtRandom()
                    }
                }
            }
            else{
                //Otherwise start searching though the paths
                if(nextXGuess in 0 until columns && nextYGuess in 0 until rows) {
                    shotAtDirection()
                }

                else {
                    directionindex++
                    shotAtDirection()
                }
            }
        }
    }


    /*
     * The easy mode for the computers shots
     * If shootAtResult is equal to 0 then the function won't have completed so the while loop is valid
     */
    fun computerShotEasyDifficult(){
        do {startingXGuess = (0 until columns).random()
            startingYGuess = (0 until rows).random()
            try {
                shootAt(startingXGuess, startingYGuess)
            } catch (e: Exception){
                computerShotEasyDifficult()
            }
        } while(shootAtResult == 0)
    }
}