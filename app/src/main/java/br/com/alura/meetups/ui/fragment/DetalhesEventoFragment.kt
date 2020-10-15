package br.com.alura.meetups.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.alura.meetups.R
import br.com.alura.meetups.model.Evento
import br.com.alura.meetups.ui.extensions.snackBar
import br.com.alura.meetups.ui.viewmodel.ComponentesVisuais
import br.com.alura.meetups.ui.viewmodel.DetalhesEventoViewModel
import br.com.alura.meetups.ui.viewmodel.EstadoAppViewModel
import coil.load
import kotlinx.android.synthetic.main.detalhes_evento.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class DetalhesEventoFragment : BaseFragment(R.layout.detalhes_evento) {

    private val argumentos by navArgs<DetalhesEventoFragmentArgs>()
    private val eventoId: String by lazy {
        argumentos.eventoId
    }
    private val viewModel: DetalhesEventoViewModel by viewModel()
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()
    private val controlador by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscaEvento(view)
    }

    private fun buscaEvento(view: View) {
        viewModel.buscaEvento(eventoId).observe(viewLifecycleOwner) {
            it?.let { resultado ->
                resultado.dado?.let { evento ->
                    configuraViews(evento)
                    preencheCampos(evento)
                    configuraBotaoInscrever(evento)
                }
                resultado.erro?.let { erro ->
                    view.snackBar(erro)
                    controlador.popBackStack()
                }
            }
        }
    }

    private fun configuraViews(evento: Evento) {
        if (evento.imagem.isNullOrBlank()) {
            detalhes_evento_imagem.visibility = GONE
        }
        if (evento.inscritos < 1) {
            detalhes_evento_container_inscritos.visibility = GONE
        }
    }

    private fun preencheCampos(evento: Evento) {
        detalhes_evento_imagem.load(evento.imagem)
        detalhes_evento_inscritos.text = "${evento.inscritos}"
        detalhes_evento_titulo.text = evento.titulo
        detalhes_evento_descricao.text = evento.descricao
    }

    private fun configuraBotaoInscrever(
        evento: Evento,
    ) {

        val toggle = detalhes_evento_botao_toggle
        val texto: String
        val fundo: Int
        val acao: () -> Unit
        if (evento.estaInscrito){
            texto = "Cancelar"
            fundo = R.color.botaoCancelar
            acao = { cancela() }
        } else {
            texto = "Inscrever"
            fundo = R.color.botaoInscrever
            acao = { inscreve() }
        }
        toggle.text = texto
        toggle.setBackgroundColor(ContextCompat.getColor(requireContext(), fundo))
        toggle.setOnClickListener { acao() }
    }

    private fun inscreve() {
        viewModel.inscreve(eventoId).observe(viewLifecycleOwner) {
            controlador.popBackStack()
        }
    }

    private fun cancela() {
        viewModel.cancela(eventoId).observe(viewLifecycleOwner) {
            it?.let { resultado ->
                resultado.dado?.let { sucesso ->
                    if (sucesso) {
                        controlador.popBackStack()
                    }
                }
                resultado.erro?.let {
                    view?.snackBar("Falha ao cancelar inscrição")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        estadoAppViewModel.temComponentes = ComponentesVisuais(
            appBar = false,
            bottomNavigation = false
        )
    }
}
