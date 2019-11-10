package com.taksapp.taksapp.data.infrastructure

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.taksapp.taksapp.R
import com.taksapp.taksapp.data.webservices.client.SessionExpiredCallback
import com.taksapp.taksapp.ui.auth.WelcomeActivity

class SessionExpiryHandler(val context: Context) : SessionExpiredCallback {
    override fun onSessionExpired() {
        Toast.makeText(context, R.string.text_session_expired, Toast.LENGTH_LONG).show()

        val intent = Intent(context, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}