package br.com.alura.meetups.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import br.com.alura.meetups.R
import br.com.alura.meetups.model.Dispositivo
import br.com.alura.meetups.notifications.IDENTIFICADOR_DO_CANAL
import br.com.alura.meetups.preferences.FirebaseTokenPreferences
import br.com.alura.meetups.repository.DispositivoRepository
import coil.imageLoader
import coil.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        val gerenciadorDeNotificacoes = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        mostraNotificacao(dados, gerenciadorDeNotificacoes)
    }

    private fun mostraNotificacao(
        dados: Map<String, String>,
        gerenciadorDeNotificacoes: NotificationManager,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val request = ImageRequest.Builder(this@MeetupsFirebaseMessagingService)
                .data(dados["imagem"])
                .build()
            val imagem = imageLoader.execute(request).drawable?.toBitmap()

            val notificacao = NotificationCompat.Builder(this@MeetupsFirebaseMessagingService,
                IDENTIFICADOR_DO_CANAL)
                .setContentTitle(dados["titulo"])
                .setContentText(dados["descricao"])
                .setSmallIcon(R.drawable.ic_acao_novo_evento)
                .setLargeIcon(imagem)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(imagem)
                    .bigLargeIcon(null))
                .build()

            gerenciadorDeNotificacoes.notify(1, notificacao)
        }
    }

}