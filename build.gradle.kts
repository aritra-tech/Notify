@file:Suppress("DSL_SCOPE_VIOLATION")
buildscript {
    dependencies {
        classpath (libs.gradle)
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.ktlint) apply false
}
