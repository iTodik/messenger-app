package ge.itodadze.messengerapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ge.itodadze.messengerapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* WORKS JUST USED FOR TESTING

        val database = Firebase.database(getString(R.string.db_location))
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")

         */
    }
}