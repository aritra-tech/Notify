import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.aritra.notify.domain.models.Note
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Immutable
data class TrashNoteState(
    val trashNotes: List<TrashNoteInfo> = emptyList(),
    val isSelectionMode: Boolean = false,
    val selectedIds: Set<Int> = emptySet(),
)

data class TrashNoteInfo(
    val note: Note,
    val deleteDateTime: LocalDateTime,
    val formatDate: String,
) {
    @Composable
    fun getDateColor(periodDateTime: Int = 28): Color {
        val currentDateTime = LocalDateTime.now()
        val calcDateTime = periodDateTime - deleteDateTime.until(currentDateTime, ChronoUnit.DAYS)
        return if (calcDateTime >= 18) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else if (calcDateTime in 8..17) {
            Color(0xFFFFC436)
        } else {
            MaterialTheme.colorScheme.error
        }
    }
}
