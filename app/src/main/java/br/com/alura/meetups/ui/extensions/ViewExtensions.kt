package br.com.alura.meetups.ui.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackBar(
    mensagem: String,
    duracao: Int = Snackbar.LENGTH_LONG,
) {
    Snackbar.make(
        this,
        mensagem,
        duracao
    ).show()
}