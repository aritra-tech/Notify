package com.aritra.notify.biometric

interface BiometricAuthListener {
    fun onBiometricAuthSuccess()
    fun onUserCancelled()
    fun onErrorOccurred()
}
