package br.com.alura.meetups.ui.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import br.com.alura.meetups.R
import br.com.alura.meetups.ui.viewmodel.ComponentesVisuais
import br.com.alura.meetups.ui.viewmodel.EstadoAppViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: EstadoAppViewModel by viewModel()
    private val controlador by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(activity_main_toolbar)
        configuraEstadoInicialDosComponentes()
        configuraNavigation()
    }

    private fun configuraNavigation() {
        controlador.addOnDestinationChangedListener {
                _,
                destination,
                _,
            ->
            title = destination.label
            viewModel.componentes.observe(this) {
                it?.let { temComponentes ->
                    configuraEstadoAppBar(temComponentes)
                    configuraEstadoBottomNavigation(temComponentes)
                }
            }
        }
        main_activity_bottom_navigation
            .setupWithNavController(controlador)
    }

    private fun configuraEstadoBottomNavigation(temComponentes: ComponentesVisuais) {
        if (temComponentes.bottomNavigation) {
            main_activity_bottom_navigation.visibility = VISIBLE
        } else {
            main_activity_bottom_navigation.visibility = GONE
        }
    }

    private fun configuraEstadoAppBar(temComponentes: ComponentesVisuais) {
        if (temComponentes.appBar) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    private fun configuraEstadoInicialDosComponentes() {
        supportActionBar?.hide()
    }

}