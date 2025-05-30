package pe.edu.cibertec.proyecto_efrst.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import pe.edu.cibertec.proyecto_efrst.R
import pe.edu.cibertec.proyecto_efrst.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener NavHostFragment y NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // Configurar BottomNavigationView sin usar setupWithNavController
        // para manejar eventos personalizados
        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.homeFragment -> {
                    // Navegar a homeFragment limpiando el stack
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true) // pop todo el stack
                        .build()
                    navController.navigate(R.id.homeFragment, null, navOptions)
                    true
                }
                else -> {
                    // Para otros items, navegar normalmente usando navigateUp si no estamos en destino actual
                    if (navController.currentDestination?.id != item.itemId) {
                        navController.navigate(item.itemId)
                        true
                    } else {
                        false
                    }
                }
            }
        }
    }
}


/*
package pe.edu.cibertec.proyecto_efrst.home

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import pe.edu.cibertec.proyecto_efrst.R

class `MainActivity` : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔍 Verificación de Firebase
        if (FirebaseApp.getApps(this).isNotEmpty()) {
            Log.d("FirebaseCheck", "✅ Firebase está inicializado correctamente")
        } else {
            Log.e("FirebaseCheck", "❌ Firebase NO está inicializado")
        }
    }
}
*/
