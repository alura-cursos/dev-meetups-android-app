package br.com.alura.meetups.webclient

import br.com.alura.meetups.model.Evento
import br.com.alura.meetups.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface EventoService {

    @GET("events")
    suspend fun buscaTodos(): Response<List<Evento>>

    @GET("events/{id}/subscribed")
    suspend fun buscaPorId(
        @Path("id") id: String,
        @Query("email") email: String?,
    ): Response<Evento>

    @POST("events/{id}/subscribe")
    suspend fun inscreve(
        @Path("id") eventoId: String,
        @Body usuario: Usuario,
    ): Response<Unit>

    @PUT("events/{id}/unsubscribe")
    suspend fun cancela(
        @Path("id") eventId: String,
        @Body usuario: Usuario,
    ): Response<Unit>

    @GET("events/subscriptions")
    suspend fun buscaInscricoes(@Query("email") email: String): Response<List<Evento>>

}
