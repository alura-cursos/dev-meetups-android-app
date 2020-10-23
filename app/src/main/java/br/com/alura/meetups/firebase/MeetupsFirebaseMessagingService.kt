package br.com.alura.meetups.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

private const val TAG = "MeetusFCM"

class MeetupsFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "onNewToken: $token")
    }

}