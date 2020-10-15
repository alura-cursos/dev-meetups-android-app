package br.com.alura.meetups.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import br.com.alura.meetups.model.Evento
import br.com.alura.meetups.model.Usuario
import br.com.alura.meetups.webclient.EventoService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.net.SocketTimeoutException

class EventoRepository(
    private val service: EventoService,
) {

    private val email get() = FirebaseAuth.getInstance().currentUser?.email

    fun buscaTodos(): LiveData<Resultado<List<Evento>>?> =
        liveData(Dispatchers.IO) {
            val resultado =
                executaService(mensagemDeErro = "Falha ao buscar eventos") {
                    service.buscaTodos()
                }
            emit(resultado)
        }

    fun buscaEvento(id: String): LiveData<Resultado<Evento>?> =
        liveData(Dispatchers.IO) {
            val resultado =
                executaService(mensagemDeErro = "Falha ao buscar evento") {
                    val email = FirebaseAuth.getInstance().currentUser?.email
                    service.buscaPorId(id, email)
                }
            emit(resultado)
        }

    fun inscreve(eventoId: String): LiveData<Resultado<Boolean>> =
        liveData(Dispatchers.IO) {
            val resultado =
                executaServiceSemResultado(mensagemDeErro = "Falha ao inscrever no evento") {
                    service.inscreve(eventoId, Usuario(email))
                }
            emit(resultado)
        }

    fun cancela(eventId: String): LiveData<Resultado<Boolean>> =
        liveData(Dispatchers.IO) {
            val resultado =
                executaServiceSemResultado(mensagemDeErro = "Falha ao cancelar inscrição") {
                    service.cancela(eventId, Usuario(email))
                }
            emit(resultado)
        }

    fun buscaInscricoes(): LiveData<Resultado<List<Evento>>> =
        liveData(Dispatchers.IO) {
            val resultado = email?.let { email ->
                executaService(mensagemDeErro = "Falha ao buscar inscrições") {
                    service.buscaInscricoes(email)
                }
            } ?: Resultado(erro = "Usuário não autenticado")
            emit(resultado)
        }

    private suspend fun executaServiceSemResultado(
        mensagemDeErro: String,
        executa: suspend () -> Response<Unit>,
    ): Resultado<Boolean> {
        return try {
            criaResultadoSemResposta(executa(), mensagemDeErro)
        } catch (e: SocketTimeoutException) {
            Resultado(erro = "Falha ao conectar com o servidor")
        } catch (e: Exception) {
            Resultado(erro = "Erro desconhecido")
        }
    }

    private fun <T> criaResultado(
        resposta: Response<T>,
        mensagemDeErro: String? = null,
    ): Resultado<T>? {
        if (resposta.isSuccessful) {
            return resposta.body()?.let { Resultado(it) }
        }
        return Resultado(erro = mensagemDeErro)
    }

    private suspend fun <T> executaService(
        mensagemDeErro: String = "Falha na requisição",
        executa: suspend () -> Response<T>,
    ): Resultado<T>? {
        return try {
            criaResultado(executa(), mensagemDeErro)
        } catch (e: SocketTimeoutException) {
            Resultado(erro = "Falha ao conectar com o servidor")
        } catch (e: Exception) {
            Resultado(erro = "Erro desconhecido")
        }
    }

    private fun criaResultadoSemResposta(
        sucesso: Response<Unit>,
        mensagemDeErro: String? = null,
    ): Resultado<Boolean> {
        if (sucesso.isSuccessful) {
            return Resultado(true)
        }
        return Resultado(false, mensagemDeErro)
    }

}
