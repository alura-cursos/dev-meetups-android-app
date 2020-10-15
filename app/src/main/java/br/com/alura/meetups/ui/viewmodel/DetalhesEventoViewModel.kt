package br.com.alura.meetups.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.alura.meetups.repository.EventoRepository

class DetalhesEventoViewModel(private val repository: EventoRepository) : ViewModel() {

    fun buscaEvento(id: String) =
        repository.buscaEvento(id)

    fun inscreve(eventoId: String) =
        repository.inscreve(eventoId)

    fun cancela(eventoId: String) =
        repository.cancela(eventoId)

}
