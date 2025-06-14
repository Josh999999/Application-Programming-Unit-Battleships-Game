package uk.ac.bournemouth.ap.battleships

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PopUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PopUpFragment(var playerTextPopUp: String) : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var inflationDevice: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    /*
     * Creates the pop-up based on the paramters pass to it, and it either puts the text
     * for player 1 or player 2 in the pop-up box
    */
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inflationDevice = inflater.inflate(R.layout.fragment_pop_up, container, false)
        val textView: TextView = inflationDevice.findViewById(R.id.pop_up_text1)
        if(playerTextPopUp == "1") textView.text = "Player 1 wins"
        else textView.text = "Player 2 wins"

        // Inflate the layout for this fragment
        return inflationDevice
    }


    /*
     * Puts an onClick listener on the back_home_button which sets up an intent that passes control
     * back to the MainActivity
    */
    override fun onResume() {
        super.onResume()
        inflationDevice.findViewById<Button>(R.id.back_home_button)?.setOnClickListener {
            Intent(context, MainActivity::class.java).also { welcomeIntent ->
                startActivity(welcomeIntent)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PopUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PopUpFragment(param1).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}