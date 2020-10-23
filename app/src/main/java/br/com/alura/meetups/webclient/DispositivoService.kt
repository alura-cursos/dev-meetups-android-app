package br.com.alura.meetups.webclient

import br.com.alura.meetups.model.Dispositivo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DispositivoService {

    @POST("devices")
    suspend fun salva(
        @Body dispositivo: Dispositivo,
    ): Response<Unit>

}