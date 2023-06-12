package com.example.pinauth.viewmodel

import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricManager.*
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import com.example.pinauth.MainContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "DEBUG_ANKIT_VIEW_MODEL"
@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel(), MainContract.Model {
    private lateinit var mBiometricManager: BiometricManager
    private var mInitialize = false

    override fun initialize(biometricManager: BiometricManager) {
        mInitialize = true
        mBiometricManager = biometricManager
    }

    override fun getAuthStatus(authType: Int): Int {
        return when(authType) {
            DEVICE_CREDENTIAL -> mBiometricManager.canAuthenticate(DEVICE_CREDENTIAL)
            BIOMETRIC_STRONG -> mBiometricManager.canAuthenticate(BIOMETRIC_STRONG)
            else -> {
                Log.d(TAG, "Invalid auth type.")
                BIOMETRIC_STATUS_UNKNOWN
            }
        }
    }
    fun getStringAuthStatus(authType: Int): String {
        if(!mInitialize) return "Main view model not Initialized"
        return when(getAuthStatus(authType)) {
            BIOMETRIC_SUCCESS -> {
                when(authType) {
                    BIOMETRIC_STRONG -> "Biometric strong is set"
                    DEVICE_CREDENTIAL -> "Pin is set"
                    else -> "Unknown Auth Type"
                }
            }
            BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Hardware not available"
            BIOMETRIC_ERROR_NONE_ENROLLED -> {
                when(authType) {
                    BIOMETRIC_STRONG -> "Biometric strong is not set"
                    DEVICE_CREDENTIAL -> "Pin is not set"
                    else -> "Unknown Auth Type"
                }
            }
            BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> "Security Update required"
            else -> "Error Reason un known"
        }
    }
    override fun isAuthenticated(authType: Int) : Boolean {
        if(!mInitialize) {
            Log.d(TAG, "isAuthenticated: MainViewModel is not initialized")
            return false
        }
        return getAuthStatus(authType) == BIOMETRIC_SUCCESS
    }

    fun getPrompt() = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setDescription("User needs to be authenticated before using the app")
}