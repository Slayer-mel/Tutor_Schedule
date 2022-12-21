package space.mel.tutorschedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import space.mel.tutorschedule.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        //setupActionBarWithNavController(findNavController(R.id.listFragment))
        /*val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        if (savedInstanceState == null) {
            navigateToFragment(HomeFragment())
        }*/
    }
    private fun navigateToFragment(fragment: Fragment) {
        //navController.navigate(R.id.action_homeFragment_to_findInfoFragment2)
    }
}