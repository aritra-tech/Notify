
import androidx.compose.runtime.Immutable
import com.aritra.notify.domain.models.Note

@Immutable
data class TrashNoteState(
    val trashNotes: List<Note> = emptyList(),
    val isSelectionMode: Boolean = false,
    val selectedIds: Set<Int> = emptySet(),
)
