package com.example.pinauth

import androidx.biometric.BiometricManager

interface MainContract {
    interface Model {
        fun initialize(biometricManager: BiometricManager)
        fun getAuthStatus(authType: Int): Int
        fun isAuthenticated(authType: Int): Boolean
    }
    interface View{
        fun initializeViewModel()
        fun initializeLayout()
        fun initializeLayoutForPin(isPinSet: Boolean)
        fun initializeLayoutForBiometric(isBiometricSet: Boolean)
        fun showDeviceStatus()
        fun setClickListener()
    }
}