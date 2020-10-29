package br.com.alura.meetups.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import br.com.alura.meetups.R

class CanaisNotificacao(private val context: Context) {

    private val manager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun criaCanal(canal: Canal) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val nome = canal.nome(context)
            val descricao = canal.descricao(context)
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canalDeNotificacao = NotificationChannel(canal.id, nome, importancia)
            canalDeNotificacao.description = descricao

            manager.createNotificationChannel(canalDeNotificacao)
        }
    }

    enum class Canal(val id: String) {
        PRINCIPAL("69fc1633-d994-4fce-a502-131fed1167f0") {
            override fun nome(context: Context): String =
                context.getString(R.string.canal_nome_principal)

            override fun descricao(context: Context): String =
                context.getString(R.string.canal_descricao_principal)
        },
        SECUNDARIO("58f3329b-ec37-4d19-ab7d-7bdb3eeac598") {
            override fun nome(context: Context): String =
                context.getString(R.string.canal_nome_secundario)

            override fun descricao(context: Context): String =
                context.getString(R.string.canal_descricao_secundario)
        };

        abstract fun nome(context: Context): String
        abstract fun descricao(context: Context): String
    }

}