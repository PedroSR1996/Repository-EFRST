package pe.edu.cibertec.proyecto_efrst.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import pe.edu.cibertec.proyecto_efrst.home.MainActivity
import pe.edu.cibertec.proyecto_efrst.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            // Usuario ya logueado
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Usuario no logueado
            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish() // Cerramos el Splash
    }
}
