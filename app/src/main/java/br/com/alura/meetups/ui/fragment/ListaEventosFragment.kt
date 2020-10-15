package br.com.alura.meetups.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import br.com.alura.meetups.R
import br.com.alura.meetups.model.Evento
import br.com.alura.meetups.ui.extensions.snackBar
import br.com.alura.meetups.ui.recyclerview.adapter.ListaEventosAdapter
import br.com.alura.meetups.ui.viewmodel.ComponentesVisuais
import br.com.alura.meetups.ui.viewmodel.EstadoAppViewModel
import br.com.alura.meetups.ui.viewmodel.ListaEventoViewModel
import kotlinx.android.synthetic.main.lista_eventos.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ListaEventosFragment : BaseFragment(R.layout.lista_eventos) {

    private val viewModel: ListaEventoViewModel by viewModel()
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()
    private val controlador: NavController by lazy {
        findNavController()
    }
    private val adapter by lazy {
        configuraAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        configuraSwipe()
        buscaEventos()
    }

    override fun onResume() {
        super.onResume()
        estadoAppViewModel.temComponentes = ComponentesVisuais(
            appBar = true,
            bottomNavigation = true
        )
    }

    private fun configuraSwipe() {
        lista_eventos_swipe_refresh.setOnRefreshListener {
            buscaEventos {
                lista_eventos_swipe_refresh.isRefreshing = false
            }
        }
    }

    private fun buscaEventos(completada: () -> Unit = {}) {
        viewModel.buscaTodos().observe(viewLifecycleOwner) {
            it?.let { resultado ->
                resultado.dado?.let(this::atualiza)
                resultado.erro?.let { erro ->
                    view?.snackBar(erro)
                }
            }
            completada()
        }
    }

    private fun atualiza(eventos: List<Evento>) {
        adapter.atualiza(eventos)
    }

    private fun configuraRecyclerView() {
        lista_eventos_recyclerview.adapter = adapter
    }

    private fun configuraAdapter(): ListaEventosAdapter {
        return ListaEventosAdapter(
            context = requireContext(),
            cliqueNoItem = { id ->
                vaiParaDetalhesDoEvento(id)
            })
    }

    private fun vaiParaDetalhesDoEvento(id: String) {
        ListaEventosFragmentDirections
            .acaoListaEventosParaDetalhesEvento(id)
            .let(controlador::navigate)
    }

}
