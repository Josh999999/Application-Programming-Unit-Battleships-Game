import uk.ac.bournemouth.ap.battleshiplib.Ship

class mainShip(
    override val top: Int,
    override val left: Int,
    override val bottom: Int,
    override val right: Int,
    XorYHint: Int): Ship{
    /*
     * Variable added into the class to give a hint of weather a one dimensional ship
     * occupies its size over the X or the Y axis of the grid
     * This helps when creating the random method for one dimensional ships
     */

    private val X_YHint: Int
    private var isSunk: Boolean

    init{
        //Initialise the two new variables inside of the ship class
        X_YHint = XorYHint
        isSunk = false
    }
}