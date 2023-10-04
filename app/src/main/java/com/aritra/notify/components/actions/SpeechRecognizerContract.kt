package com.aritra.notify.components.actions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContract
import java.util.*
import kotlin.collections.ArrayList

class SpeechRecognizerContract : ActivityResultContract<Unit, ArrayList<String>?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
        )
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            "Speak Something"
        )

        return intent
    }
    override fun parseResult(resultCode: Int, intent: Intent?): ArrayList<String>? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
    }
}
