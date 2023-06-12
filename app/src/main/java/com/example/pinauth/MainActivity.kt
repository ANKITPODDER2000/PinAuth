package com.example.pinauth

import android.annotation.SuppressLint
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import com.example.pinauth.databinding.ActivityMainBinding
import com.example.pinauth.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var biometricManager: BiometricManager
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    // @Inject lateinit var mBiometricPrompt: CheckBiometricPrompt
    private lateinit var callBack: BiometricCallBack
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        biometricManager = BiometricManager.from(this)
        callBack = BiometricCallBack(this)
        biometricPrompt = CheckBiometricPrompt(this, callBack)

        initializeViewModel()
    }

    override fun onResume() {
        super.onResume()
        initializeLayout()
    }

    override fun initializeViewModel() {
        mainViewModel.initialize(biometricManager)
    }

    override fun initializeLayout() {
        val isPinSet = mainViewModel.isAuthenticated(DEVICE_CREDENTIAL)
        val isBiometricSet = mainViewModel.isAuthenticated(BIOMETRIC_STRONG)
        initializeLayoutForPin(isPinSet)
        initializeLayoutForBiometric(isBiometricSet)
        showDeviceStatus()
        setClickListener()
    }

    override fun initializeLayoutForPin(isPinSet: Boolean) {
        when(isPinSet) {
            true -> {
                binding.btnOpenPinSetting.visibility = View.GONE
                binding.btnDeviceCredential.visibility = View.VISIBLE
            }
            else -> {
                binding.btnOpenPinSetting.visibility = View.VISIBLE
                binding.btnDeviceCredential.visibility = View.GONE
            }
        }
    }

    override fun initializeLayoutForBiometric(isBiometricSet: Boolean) {
        when(isBiometricSet) {
            true -> {
                binding.btnOpenBiometricSetting.visibility = View.GONE
                binding.btnBiometricStrong.visibility = View.VISIBLE
            }
            else -> {
                binding.btnOpenBiometricSetting.visibility = View.VISIBLE
                binding.btnBiometricStrong.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun showDeviceStatus() {
        val pinStatus = mainViewModel.getStringAuthStatus(DEVICE_CREDENTIAL)
        val biometricStatus = mainViewModel.getStringAuthStatus(BIOMETRIC_STRONG)
        binding.tvLockStatus.text = pinStatus + "\n" + biometricStatus
    }

    override fun setClickListener() {
        binding.btnDeviceCredential.setOnClickListener {
            val prompt = mainViewModel.getPrompt().setAllowedAuthenticators(DEVICE_CREDENTIAL).build()
            biometricPrompt.authenticate(prompt)
        }
        binding.btnOpenBiometricSetting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_FINGERPRINT_ENROLL))
        }
        binding.btnOpenPinSetting.setOnClickListener {
            startActivity(Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD))
        }
        binding.btnBiometricStrong.setOnClickListener {
            val promptInfo = mainViewModel.getPrompt()
                .setDeviceCredentialAllowed(true)
                .build()
            biometricPrompt.authenticate(promptInfo)
        }
    }
}