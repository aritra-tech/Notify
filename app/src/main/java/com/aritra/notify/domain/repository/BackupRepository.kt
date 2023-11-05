package com.aritra.notify.domain.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.aritra.notify.data.converters.DateTypeConverter
import com.aritra.notify.data.db.NoteDatabase
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.usecase.SaveSelectedImageUseCase
import com.aritra.notify.utils.CsvIo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
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
                    val csvWriter = CsvIo.Writer(FileWriter(File(backupDir, "notes.csv")))
                    // write the headers to the csv file
                    csvWriter.writeNext(arrayOf("Id", "Title", "Content", "Date", "Image"))
                    // write the notes to the csv file
                    provider.noteDao().getAllNotesFlow().first().forEach { note ->
                        // write the image to the backup directory if it exists
                        val images = note.image.filterNotNull().mapIndexed { index, image ->
                            val imageName = "image_${note.id}_($index).webp"
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
                                DateTypeConverter.toString(note.dateTime).orEmpty()
                            ).plus(images)
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
                    // delete the backup directory
                    backupDir.deleteRecursively()
                } catch (e: Exception) {
                    Log.e(BackupRepository::class.simpleName, "Export", e)
                }
            }
        }
    }

    suspend fun import(uri: Uri) {
        withContext(dispatcher + scope.coroutineContext) {
            mutex.withLock {
                try {
                    val restoreDir = File(context.externalCacheDir, "restore")
                    // delete the restore directory if it already exists
                    if (restoreDir.exists()) restoreDir.deleteRecursively()
                    // create a new restore directory
                    restoreDir.mkdir()
                    // extract the zip file to the restore directory
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        ZipInputStream(BufferedInputStream(stream)).use { zip ->
                            var entry = zip.nextEntry
                            while (entry != null) {
                                val file = File(restoreDir, entry.name)
                                file.outputStream().use { output ->
                                    zip.copyTo(output)
                                }
                                entry = zip.nextEntry
                            }
                        }
                    }
                    // open the notes csv file
                    val csvReader = CsvIo.Reader(FileReader(File(restoreDir, "notes.csv")))
                    // read all the lines and discard the headers
                    val rows = csvReader.rows().drop(1)
                    Log.d(
                        BackupRepository::class.simpleName!!,
                        "import: ${rows.map { it.toList() }}}"
                    )
                    // close the csv reader
                    csvReader.close()
                    // clear the database to remove all the existing notes
                    provider.noteDao().clear()
                    // delete all images from the cache directory
                    File(context.externalCacheDir, SaveSelectedImageUseCase.DIRECTORY)
                        .deleteRecursively()
                    // import the notes from the csv file into the database
                    rows.forEach { columns ->
                        val id = columns[0].toInt()
                        val title = columns[1]
                        val text = columns[2]
                        val dateTime = DateTypeConverter.toDate(columns[3])
                        val imageNameList = columns.slice(IntRange(4, columns.size - 1))
                        val images = imageNameList.map { imageName ->
                            if (imageName.isNotEmpty()) {
                                val index =
                                    imageName.substringBeforeLast(".webp")
                                        .substringAfterLast('_')
                                        .trim('(', ')')
                                        .toInt()
                                val imageStore = SaveSelectedImageUseCase.image(context, id, index)
                                // copy the image from the restore directory to the cache directory
                                context.contentResolver.openInputStream(
                                    Uri.fromFile(File(restoreDir, imageName))
                                )?.use { inputStream ->
                                    imageStore.outputStream().use { outputStream ->
                                        inputStream.copyTo(outputStream)
                                    }
                                }
                                // get the uri for the image in the cache directory
                                FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    imageStore
                                )
                            } else {
                                null
                            }
                        }
                        val note = Note(
                            id = id,
                            title = title,
                            note = text,
                            dateTime = dateTime,
                            image = images
                        )
                        Log.d(BackupRepository::class.simpleName, "import: $note")
                        provider.noteDao().insertNote(
                            note
                        )
                    }
                    // delete the restore directory
                    restoreDir.deleteRecursively()
                } catch (e: Exception) {
                    Log.e(BackupRepository::class.simpleName, "Import", e)
                }
            }
        }
    }
}
