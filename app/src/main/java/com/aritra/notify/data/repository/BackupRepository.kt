package com.aritra.notify.data.repository

import android.content.Context
import android.net.Uri
import com.aritra.notify.data.converters.DateTypeConverter
import com.aritra.notify.data.db.NoteDatabase
import com.aritra.notify.utils.Const
import com.aritra.notify.utils.CsvWriter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileWriter
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

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
                try {
                    // create a backup folder in the cache dir
                    val backupDir = File(context.externalCacheDir, "backup")
                    // if the backup directory already exists, delete it
                    if (backupDir.exists()) backupDir.deleteRecursively()
                    // create a new backup directory
                    backupDir.mkdir()
                    // creates a csv writer for writing the notes to a csv file in the backup directory
                    val csvWriter = CsvWriter(FileWriter(File(backupDir, "notes.csv")))
                    // write the headers to the csv file
                    csvWriter.writeNext(arrayOf("Id", "Title", "Content", "Date", "Image"))
                    // write the notes to the csv file
                    provider.noteDao().getAllNotes().first().forEach { note ->
                        // write the image to the backup directory if it exists
                        val image = note.image?.let { image ->
                            val imageName = "image_${note.id}.webp"
                            val imageFile = File(backupDir, imageName)
                            context.contentResolver.openInputStream(image)?.use { inputStream ->
                                imageFile.outputStream().use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                            imageName
                        }
                        csvWriter.writeNext(
                            arrayOf(
                                note.id.toString(),
                                note.title,
                                note.note,
                                DateTypeConverter.toString(note.dateTime).orEmpty(),
                                image.orEmpty()
                            )
                        )
                    }
                    // close the csv writer
                    csvWriter.close()
                    // create a zip file containing the csv and the images
                    ZipOutputStream(
                        BufferedOutputStream(
                            context.contentResolver.openOutputStream(
                                uri
                            )
                        )
                    ).use { zip ->
                        backupDir.listFiles()?.forEach { file ->
                            zip.putNextEntry(ZipEntry(file.name))
                            file.inputStream().copyTo(zip)
                            zip.closeEntry()
                        }
                    }
                } catch (_: Exception) {
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