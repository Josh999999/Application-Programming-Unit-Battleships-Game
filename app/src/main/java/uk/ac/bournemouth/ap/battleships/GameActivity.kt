package uk.ac.bournemouth.ap.battleships

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class GameActivity : AppCompatActivity() {

    /*
     * Loads a layout file depending on which string the MainActivity passes through the intent to GameActivity
     * If the HardMode string is passed (from pressing the "hard" button in MainActivity) it will load the layout file
     * with the GameGridView class conected, otherwise it will load the easy layout file with the GameGridViewEasy class
     * connected to it.
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        val fullName: String = intent?.getStringExtra(MainActivity.EASY_HARD_DIFFICULTY_TAG).toString()
        if(fullName == "HARD666_69lolXX") {
            setContentView(R.layout.activity_game)
        } else {
            setContentView(R.layout.activity_game_easy)
        }
        return super.onCreate(savedInstanceState)
    }

    /*
     * Sets a listener so that button at the top of the page takes the user back to the MainActivity using an intent with
     * no parameters put it (not neccesary)
    */
    override fun onResume(){
        val buttonBack: Button = findViewById(R.id.button_back)
        buttonBack.setOnClickListener {
            Intent(this, MainActivity::class.java).also { welcomeIntent ->
                startActivity(welcomeIntent)
            }
        }
        return super.onResume()
    }

}
