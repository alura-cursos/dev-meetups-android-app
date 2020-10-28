package br.com.alura.meetups.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import br.com.alura.meetups.R
import coil.imageLoader
import coil.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Notificacao(
    private val context: Context
) {

    private val manager by lazy {
        context.getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
    }

    companion object {
        private var id = 1
    }

    fun mostra(dados: Map<String, String?>) {
        CoroutineScope(Dispatchers.IO).launch {
            val imagem = tentaBuscarImagem(dados["imagem"])
            val estilo = criaEstilo(imagem, dados)
            val notificacao = criaNotificacao(dados, estilo)
            manager.notify(id, notificacao)
            id++
        }
    }

    private fun criaEstilo(
        imagem: Bitmap?,
        dados: Map<String, String?>,
    ): NotificationCompat.Style {
        return imagem?.let {
            NotificationCompat.BigPictureStyle().bigPicture(it)
        } ?: NotificationCompat.BigTextStyle().bigText(dados["descricao"])
    }

    private fun criaNotificacao(
        dados: Map<String, String?>,
        estilo: NotificationCompat.Style,
    ): Notification? {
        return NotificationCompat.Builder(context,
            IDENTIFICADOR_DO_CANAL)
            .setContentTitle(dados["titulo"])
            .setContentText(dados["descricao"])
            .setSmallIcon(R.drawable.ic_acao_novo_evento)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(estilo)
            .build()
    }

    private suspend fun tentaBuscarImagem(imagem: String?): Bitmap? {
        val request = ImageRequest.Builder(context)
            .data(imagem)
            .build()
        return context.imageLoader.execute(request).drawable?.toBitmap()
    }

}
