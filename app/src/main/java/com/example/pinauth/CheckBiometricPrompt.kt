package com.example.pinauth

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CheckBiometricPrompt(private val context: Context, callBack: BiometricCallBack) :
    BiometricPrompt(context as MainActivity, ContextCompat.getMainExecutor(context), callBack)