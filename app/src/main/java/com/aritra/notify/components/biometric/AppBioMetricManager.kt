package com.aritra.notify.components.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.aritra.notify.ui.screens.MainActivity
import javax.inject.Inject

class AppBioMetricManager @Inject constructor(appContext: Context) {

    private var biometricPrompt: BiometricPrompt? = null
    private val biometricManager = BiometricManager.from(appContext)

    fun canAuthenticate(): Boolean {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                true
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                false
            }

            else -> {
                false
            }
        }
    }

    fun initBiometricPrompt(activity: MainActivity, listener: BiometricAuthListener) {
        biometricPrompt = BiometricPrompt(
            activity,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    val cancelled = errorCode in arrayListOf(
                        BiometricPrompt.ERROR_CANCELED,
                        BiometricPrompt.ERROR_USER_CANCELED,
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON
                    )
                    if (cancelled) {
                        listener.onUserCancelled()
                    } else {
                        listener.onErrorOccurred()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    listener.onBiometricAuthSuccess()
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in with biometric auth")
            .setNegativeButtonText("Cancel")
            .build()
        biometricPrompt?.authenticate(promptInfo)
    }
}
