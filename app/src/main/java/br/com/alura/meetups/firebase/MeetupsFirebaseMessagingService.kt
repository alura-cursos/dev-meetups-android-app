package br.com.alura.meetups.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import br.com.alura.meetups.R
import br.com.alura.meetups.model.Dispositivo
import br.com.alura.meetups.preferences.FirebaseTokenPreferences
import br.com.alura.meetups.repository.DispositivoRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject

private const val TAG = "MeetupsFCM"
private const val IDENTIFICADOR_DO_CANAL = "principal"

class MeetupsFirebaseMessagingService : FirebaseMessagingService() {

    private val dispositivoRepository: DispositivoRepository by inject()
    private val preferences: FirebaseTokenPreferences by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "onNewToken: $token")
        preferences.tokenNovo()
        dispositivoRepository.salva(Dispositivo(token = token))
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.i(TAG, "onMessageReceived: recebeu mensagem de notificacao ${remoteMessage.notification}")
        Log.i(TAG, "onMessageReceived: recebeu mensagem de dados ${remoteMessage.data}")
        val dados = remoteMessage.data

        val gerenciadorDeNotificacoes = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val nome = getString(R.string.channel_name)
            val descricao = getString(R.string.channel_description)
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel(IDENTIFICADOR_DO_CANAL, nome, importancia)
            canal.description = descricao

            gerenciadorDeNotificacoes.createNotificationChannel(canal)
        }

        val notificacao = NotificationCompat.Builder(this, IDENTIFICADOR_DO_CANAL)
            .setContentTitle(dados["titulo"])
            .setContentText(dados["descricao"])
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        gerenciadorDeNotificacoes.notify(1, notificacao)
    }

}