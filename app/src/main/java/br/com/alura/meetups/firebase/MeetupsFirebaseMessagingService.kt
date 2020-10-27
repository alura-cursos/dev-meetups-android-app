package br.com.alura.meetups.firebase

import android.app.NotificationManager
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
        val notificacao = NotificationCompat.Builder(this, "principal")
            .setContentTitle(dados["titulo"])
            .setContentText(dados["descricao"])
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val gerenciadorDeNotificacoes = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        gerenciadorDeNotificacoes.notify(1, notificacao)
    }

}