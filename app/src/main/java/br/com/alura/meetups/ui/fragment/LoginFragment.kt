package br.com.alura.meetups.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.alura.meetups.R
import br.com.alura.meetups.ui.extensions.snackBar
import br.com.alura.meetups.ui.viewmodel.ComponentesVisuais
import br.com.alura.meetups.ui.viewmodel.EstadoAppViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.inicio.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

private const val RC_SIGN_IN = 1

class LoginFragment : Fragment(R.layout.inicio) {

    private val controlador by lazy {
        findNavController()
    }
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verificaAutenticacao()
        configuraBotaoEntrar()
    }

    private fun configuraBotaoEntrar() {
        inicio_botao_entrar.setOnClickListener { abreAutenticacaoComFirebaseUI() }
    }

    private fun abreAutenticacaoComFirebaseUI() {
        val provedores = listOf(AuthUI.IdpConfig.EmailBuilder().build())
        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(provedores)
            .build(),
            RC_SIGN_IN)
    }

    override fun onResume() {
        super.onResume()
        estadoAppViewModel.temComponentes = ComponentesVisuais(
            appBar = false,
            bottomNavigation = false
        )
    }

    private fun verificaAutenticacao() {
        FirebaseAuth.getInstance().currentUser?.let {
            vaiParaListaEventos()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    vaiParaListaEventos()
                }
            } else {
                view?.snackBar("Não foi possível autenticar")
            }
        }
    }

    private fun vaiParaListaEventos() {
        LoginFragmentDirections
            .acaoLoginParaListaEventos()
            .let(controlador::navigate)
    }

}