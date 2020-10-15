package br.com.alura.meetups.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.alura.meetups.repository.EventoRepository

class ListaEventoViewModel(private val repository: EventoRepository) : ViewModel() {

    fun buscaTodos() = repository.buscaTodos()

}