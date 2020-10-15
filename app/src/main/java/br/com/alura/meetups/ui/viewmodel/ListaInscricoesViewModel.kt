package br.com.alura.meetups.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.alura.meetups.repository.EventoRepository

class ListaInscricoesViewModel(private val repository: EventoRepository) : ViewModel() {

    fun buscaInscricoes() = repository.buscaInscricoes()

}
