package br.com.alura.meetups.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import br.com.alura.meetups.R

const val IDENTIFICADOR_DO_CANAL = "8eadaa43-c475-48bb-84d2-050b0ebfc2e5"

class CanalPrincipal(
    private val context: Context,
    private val manager: NotificationManager
) {

    fun criaCanal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val nome = context.getString(R.string.channel_name)
            val descricao = context.getString(R.string.channel_description)
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel(IDENTIFICADOR_DO_CANAL, nome, importancia)
            canal.description = descricao

            manager.createNotificationChannel(canal)
        }
    }

}