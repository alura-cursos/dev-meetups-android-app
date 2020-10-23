package br.com.alura.meetups.di

import br.com.alura.meetups.repository.EventoRepository
import br.com.alura.meetups.ui.viewmodel.DetalhesEventoViewModel
import br.com.alura.meetups.ui.viewmodel.EstadoAppViewModel
import br.com.alura.meetups.ui.viewmodel.ListaEventoViewModel
import br.com.alura.meetups.ui.viewmodel.ListaInscricoesViewModel
import br.com.alura.meetups.webclient.EventoService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val URL_BASE = "http://192.168.0.47:8080/api/"

val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<EventoService> { get<Retrofit>().create(EventoService::class.java) }
    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}

val viewModelModule = module {
    viewModel<ListaEventoViewModel> { ListaEventoViewModel(get()) }
    viewModel<DetalhesEventoViewModel> { DetalhesEventoViewModel(get()) }
    viewModel<EstadoAppViewModel> { EstadoAppViewModel() }
    viewModel<ListaInscricoesViewModel> { ListaInscricoesViewModel(get()) }
}

val repositoryModule = module {
    single<EventoRepository> { EventoRepository(get()) }
}

val appModules = listOf(
    retrofitModule,
    viewModelModule,
    repositoryModule,
)

