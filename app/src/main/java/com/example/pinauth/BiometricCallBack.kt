package com.example.pinauth

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt

class BiometricCallBack(private val context: Context) :
    BiometricPrompt.AuthenticationCallback() {

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        showToast("On Authentication Error")
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        showToast("On Authentication Succeeded")
    }

    override fun onAuthenticationFailed() {
        showToast("On Authentication Failed")
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
