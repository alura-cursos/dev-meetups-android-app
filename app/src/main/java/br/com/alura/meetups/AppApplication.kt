package br.com.alura.meetups

import android.app.Application
import br.com.alura.meetups.di.appModules
import br.com.alura.meetups.notifications.CanaisNotificacao
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@AppApplication)
            modules(appModules)
        }
        val canais = CanaisNotificacao(this)
        canais.criaCanal(CanaisNotificacao.Canal.PRINCIPAL)
        canais.criaCanal(CanaisNotificacao.Canal.SECUNDARIO)
    }

}