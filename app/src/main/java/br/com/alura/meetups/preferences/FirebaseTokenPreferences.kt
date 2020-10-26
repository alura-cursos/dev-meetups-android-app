package br.com.alura.meetups.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private const val NOME_PREFERENCES = "br.com.alura.meetups.preferences.FirebaseTokenPreferences"
private const val CHAVE_ENVIADO = "enviado"

class FirebaseTokenPreferences(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(NOME_PREFERENCES, Context.MODE_PRIVATE)
    val enviado get() = preferences.getBoolean(CHAVE_ENVIADO, false)

    fun tokenNovo() {
        preferences.edit {
            putBoolean(CHAVE_ENVIADO, false)
        }
    }

    fun tokenEnviado() {
        preferences.edit {
            putBoolean(CHAVE_ENVIADO, true)
        }
    }

}