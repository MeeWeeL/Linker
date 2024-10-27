import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.URI

fun String.copy() {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val stringSelection = StringSelection(this)
    clipboard.setContents(stringSelection, null)
}

fun String.browse() {
    val link = when {
        contains("https://") || contains("http://") -> this
        else -> "https://$this"
    }
    val uri = URI(link)
    Desktop.getDesktop().browse(uri)
}
