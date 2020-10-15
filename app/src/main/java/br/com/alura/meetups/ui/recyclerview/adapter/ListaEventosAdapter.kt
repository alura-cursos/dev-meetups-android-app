package br.com.alura.meetups.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.meetups.R
import br.com.alura.meetups.model.Evento
import coil.load
import kotlinx.android.synthetic.main.evento_item.view.*

class ListaEventosAdapter(
    private val context: Context,
    eventos: List<Evento> = listOf(),
    val cliqueNoItem: (id: String) -> Unit,
) : RecyclerView.Adapter<ListaEventosAdapter.ViewHolder>() {

    private val eventos = eventos.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(context)
            .inflate(
                R.layout.evento_item,
                parent,
                false
            ))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(eventos[position])
    }

    override fun getItemCount(): Int = eventos.size

    fun atualiza(eventos: List<Evento>) {
        this.eventos.clear()
        this.eventos.addAll(eventos)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titulo = itemView.evento_item_titulo
        private val descricao = itemView.evento_item_descricao
        private val imagem = itemView.evento_item_imagem
        private val containerInscritos = itemView.evento_item_container_inscritos
        private val inscritos = itemView.evento_item_inscritos
        private lateinit var evento: Evento

        init {
            itemView.setOnClickListener {
                if (::evento.isInitialized) {
                    cliqueNoItem(evento.id)
                }
            }
        }

        fun vincula(evento: Evento) {
            this.evento = evento
            configuraImagem()
            configuraInscritos()
            preencheCampos()
        }

        private fun configuraInscritos() {
            if (this.evento.inscritos > 0) {
                containerInscritos.visibility = VISIBLE
            } else {
                containerInscritos.visibility = GONE
            }
        }

        private fun configuraImagem() {
            if (this.evento.imagem.isNullOrBlank()) {
                imagem.visibility = GONE
            } else {
                imagem.visibility = VISIBLE
            }
        }

        private fun preencheCampos() {
            titulo.text = this.evento.titulo
            descricao.text = this.evento.descricao
            imagem.load(this.evento.imagem)
            inscritos.text = "${evento.inscritos}"
        }

    }

}
