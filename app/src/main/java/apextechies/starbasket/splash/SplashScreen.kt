package apextechies.starbasket.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import apextechies.starbasket.R
import apextechies.starbasket.activity.MainActivity

class SplashScreen: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
    }
}