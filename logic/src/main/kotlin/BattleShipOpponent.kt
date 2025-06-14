import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.Companion.DEFAULT_SHIP_SIZES
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.Ship
import java.lang.Integer.max
import java.lang.Math.min
import kotlin.random.Random

class BattleShipOpponent(
    override val columns: Int,
    override val rows: Int,
    override var ships: List<Ship> = listOf(mainShip(0,0, 0 , 5, 1)),
    val shipSizes: IntArray = DEFAULT_SHIP_SIZES,
    val random: Random = Random(0),
    val isRandom: Boolean = false,
) : BattleshipOpponent {

    init{
        if(isRandom){
            ships = RandomShips(shipSizes, columns, rows, random)
        }
        else {
            val shipValidation: Boolean = ValidateShips(ships)
            if (!shipValidation) {
                throw Exception("Ships not valid")
            }
        }
    }

    override fun shipAt(column: Int, row: Int): BattleshipOpponent.ShipInfo<Ship>? {
        ships.forEachIndexed { i, ship ->
            if(column in ship.left..ship.right
                && row in ship.top..ship.bottom
            ) return BattleshipOpponent.ShipInfo(i, ship)
        }
        return null
    }


    /*
     * Overall function to go through each validation function to validate the manually given ships
    */
    fun ValidateShips(ships: List<Ship>): Boolean{
        //Initialise the variables for this function
        var isOverlap = false
        var isValid = true
        var isCorrectSize = true
        var inProportion = true


        /*
         * Code to validate wether the ship is placed in a vlad position of the grid
         * The text / user should provide both the grid size and ship placements here
         * so there is no reason to think they wouldnt be placed in a valid position outside
         * of poor user placement (bad user input)
         */

        ships.forEach{
            //Only continue if the ships so far are found to be valid
            if(isValid) isValid = ValidateShip(it)
        }

        ships.forEach{
            //Only continue if the ships so far are found to be valid
            if(isCorrectSize) isCorrectSize = checkShipSize(it)
        }


        ships.forEach{
            //Only continue if the ships so far are found to be valid
            if(inProportion) inProportion = checkProportions(it)
        }


        /*
         * This was the initial code for checking the overlap
         * Keep this here incase of need to reference to it / it becomes re-useable
         * either through the syntax of how to use the "forEachIndexed" function
         * or becomes applicable to anothe part of the program
        ships.forEachIndexed{
            i, value -> if(i != 0){ //Make sure it doesnt try to
                if(!isOverlap) { //Only continue if the ships so far are found to be valid
                    var j = i
                    isOverlap = ValidateOverlap(ships[--j], ships[j])
                }
            }
        }
         */


        /*
         * The new code to check the overlap of the ships
         * The code here in similar to that of a selection sort
         */
        var i = 0
        while(i <= (ships.size - 1)){
            //Dont need to compare the last ship with any of the others (alreay done by proxy)
            var j = i
            while(j <= (ships.size - 2)){
                //Check the current ship with all ships to the right of it (prev already done)
                j++
                isOverlap = validateOverlap(
                    ships[i].left, ships[i].right ,ships[i].bottom, ships[i].top,
                    ships[j].left, ships[j].right ,ships[j].bottom, ships[j].top
                )
                if(isOverlap) break //Break the loop if there is an overlap
            }
            if(isOverlap) break
            i++
        }

        //Evaluate the validation tests
        if(isOverlap || !isValid || !isCorrectSize ||!inProportion) return false
        return true
    }


    /*
     * Checks that the ship is drawn within the correct proporions by size
     */
    private fun checkShipSize(ship: Ship): Boolean{
        if(ship.left != ship.right && ship.top != ship.bottom) return false
        return true
    }


    /*
     * Checks that the ship is drawn within the correct proporions in reference to coordinates
     */
    private fun checkProportions(ship: Ship): Boolean{
        if(ship.top > ship.bottom || ship.left > ship.right) return false
        return true
    }


    /*
     * Checks that the ship doesnt exceed the correct range in reference to the rows and columns
     */
    private fun ValidateShip(ship: Ship): Boolean{
        if( ship.top in 0 until rows
            && ship.left in 0 until columns
            && ship.bottom in 0 until rows
            && ship.right in 0 until columns
        ) return true
        return false
    }



    companion object {
        /*
         * Function to make sure that none of the ships in the gird overlap with one another
         * Checks overlapp by treating each ship as an individual rectangle and using the top, left, bottom, right
         * variables as low and high XY coordinates, it then takes the max of the rist two and min of the rest
         * to determine wether they overlap
        */
        fun validateOverlap(
            left: Int, right: Int, bottom: Int, top: Int,
            left2: Int, right2: Int, bottom2: Int, top2: Int
        ): Boolean{
            val maxLeft = max(left2, left)
            val minRight = min(right2, right)

            val minTop = min(bottom2, bottom)
            val maxBottom = max(top2, top)

            val diff1 = (minRight - maxLeft)
            val diff2 = (minTop - maxBottom)

            if(diff1 >= 0 && diff2 >= 0) return true
            return false
        }


        /*
         * Generates random ships based on the random seed given to the function
         * These ships are generated specifically within the border of the grid
         * The same logic to check for overlapping in manually chosen ships is used to detect for overlapping
         * in randomly chosen ships, they are then re-chosen until there is no overlap with the rest of the
         * chosen ships
        */
        fun RandomShips(Ship_sizes: IntArray, columns: Int, rows: Int, random: Random): List<Ship> {
            val newShips = mutableListOf<Ship>()
            //This will only implement a random placement method for ships of width 1
            Ship_sizes.forEachIndexed { i, shipSize ->
                run {//Ships can only be placed either bellow or to the right of the first chosen coordinate
                    val bottomConstraint = (rows - Ship_sizes[i]) - 1
                    val rightConstraint = (columns - Ship_sizes[i]) - 1

                    //Generate a couple of random numbers to decide the starting coods for the ship:
                    //Initialise the variables for the conditial operations
                    var startingX: Int
                    var startingY: Int
                    var endingX: Int
                    var endingY: Int
                    val newShip: Ship
                    var xOrY: Int


                    do { //This block will always be executed
                        //Generate a random number (1 or 0) to decide wether to stay on the X or Y axis:
                        if(bottomConstraint > 0 && rightConstraint > 0){
                            xOrY = random.nextInt(0,2)
                        } else if (bottomConstraint > 0 && rightConstraint <= 0){
                            xOrY = 1
                        } else if (rightConstraint > 0 && bottomConstraint <= 0){
                            xOrY = 0
                        } else xOrY = 0

                        //Conditional operations using the X or Y hint
                        if(bottomConstraint < 0 && rightConstraint < 0){
                            startingX = 0
                            startingY =  random.nextInt(0,rows)
                            endingX = startingX + shipSize - 1
                            endingY = startingY
                        }
                        else if (xOrY == 0) {
                            startingX =  random.nextInt(0, rightConstraint + 1)
                            startingY =  random.nextInt(0, rows)
                            endingX = startingX + shipSize - 1
                            endingY = startingY
                        } else {
                            startingX =  random.nextInt(0, columns)
                            startingY =  random.nextInt(0, bottomConstraint + 1)
                            endingY = startingY + shipSize - 1
                            endingX = startingX
                        }

                        //check the new ship against every other ship in
                        var i = 0
                        var isOverlap = false
                        while (i < newShips.size) {
                            isOverlap = validateOverlap(
                                newShips[i].left, newShips[i].right, newShips[i].bottom, newShips[i].top,
                                startingX, endingX, endingY, startingY
                            )
                            if (isOverlap) break //Break the loop if there is an overlap
                            i++
                        }
                    } while (isOverlap)

                    //Create the ship to be put into the list:
                    newShip = mainShip(startingY, startingX, endingY, endingX, xOrY)
                    //Add the new ship to the list
                    newShips.add(newShip)
                }
            }
            return newShips
        }
    }
}