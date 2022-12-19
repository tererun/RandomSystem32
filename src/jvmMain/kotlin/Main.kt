import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File
import java.util.*

@Composable
@Preview
fun App() {
    var dialogState by remember { mutableStateOf(false) }
    var path by remember { mutableStateOf("") }
    var displayText by remember { mutableStateOf("まだ選ばれていません!") }

    MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                val system32 = File("C:\\Windows\\System32")
                val randomizedFile = getRandomizedFile(system32)
                path = randomizedFile.absolutePath
            }) {
                Text("ランダムに選出")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                if (path.isEmpty()) {
                    dialogState = true
                } else {
                    val runtime = Runtime.getRuntime()
                    val p = runtime.exec("cmd /c explorer /select,$path")
                    p.waitFor()
                    p.destroy()
                }
            }) {
                Text("フォルダを開く")
            }
            displayText = if (path.isEmpty()) {
                "まだ選ばれていません!";
            } else {
                "選ばれたファイルは $path です";
            }
            Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = displayText)
            Dialog(visible = dialogState, onCloseRequest = { dialogState = false }) {
                Text("ファイルが選ばれていません")
            }
        }

    }
}

fun getRandomizedFile(folder: File): File {
    val random = Random()
    val listFiles = folder.listFiles()
    val randomFile = listFiles[random.nextInt(listFiles.size)]
    if (randomFile.isDirectory) return getRandomizedFile(randomFile)
    return randomFile
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
