package uk.ac.bournemouth.ap.battleships

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    /*
     * Sets up two event listeners, one for the easy mode of the game and another for the hardmode upon Activity creation
     * Both these listeners will set up an intent to load the GameActivity class, and pass in a string to indicate the level
     * of diffculty the class should produce.
     * This will dictate wether the BattleShipGridComputer class uses the hard or easy shot method against the user
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialButton>(R.id.easyButton)?.setOnClickListener {
            Intent(this, GameActivity::class.java).also { welcomeIntent ->
                welcomeIntent.putExtra(EASY_HARD_DIFFICULTY_TAG, "EASY101XX")
                startActivity(welcomeIntent)
            }
        }

        findViewById<MaterialButton>(R.id.hardButton)?.setOnClickListener {
            Intent(this, GameActivity::class.java).also { welcomeIntent ->
                welcomeIntent.putExtra(EASY_HARD_DIFFICULTY_TAG, "HARD666_69lolXX")
                startActivity(welcomeIntent)
            }
        }
    }

    companion object{ //Allows attributes an methods to be called outside the call by using the class name instead of an object
        const val EASY_HARD_DIFFICULTY_TAG = "EASY101XX"
    }
}