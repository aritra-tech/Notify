package com.aritra.notify.data.repository

import android.content.Context
import android.net.Uri
import com.aritra.notify.data.db.NoteDatabase
import com.aritra.notify.utils.Const
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class BackupRepository(
    private val provider: NoteDatabase,
    private val context: Context,
    private val mutex: Mutex,
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend fun export(uri: Uri) {
        withContext(dispatcher + scope.coroutineContext) {
            mutex.withLock {
                provider.close()

                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    context.getDatabasePath(Const.DB_NAME).inputStream().copyTo(stream)
                }
            }
        }
    }

    suspend fun import(uri: Uri) {
        withContext(dispatcher + scope.coroutineContext) {
            mutex.withLock {
                provider.close()

                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val dbFile = context.getDatabasePath(Const.DB_NAME)
                    dbFile?.delete()
                    stream.copyTo(dbFile.outputStream())
                }
            }
        }
    }
}