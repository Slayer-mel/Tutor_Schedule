package space.mel.tutorschedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import space.mel.tutorschedule.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

   private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
    }
}