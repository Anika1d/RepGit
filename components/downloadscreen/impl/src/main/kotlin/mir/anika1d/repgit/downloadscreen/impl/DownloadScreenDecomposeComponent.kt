package mir.anika1d.repgit.downloadscreen.impl

import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import mir.anika1d.core.decompose.DecomposeOnBackParameter
import mir.anika1d.repgit.downloadscreen.api.ACDownloadScreenDecomposeComponent
import mir.anika1d.repgit.downloadscreen.impl.screen.Screen
import mir.anika1d.repgit.downloadscreen.impl.screen.management.ManagementScreen
import java.io.File


class DownloadScreenDecomposeComponent(
    componentContext: ComponentContext,
    private val onBack: DecomposeOnBackParameter,
    private val navigationToDetails: (String, String) -> Unit,
) :
    ACDownloadScreenDecomposeComponent(componentContext) {


    private val _repositories = MutableStateFlow(listOf<Pair<String, String>>())


    fun getLocalRepositories() {
        componentScope.launch {
            val downloadFolder = File(
                Environment.getExternalStorageDirectory().toString() + "/Download/repgit"
            )
            val filesAndFolders = mutableListOf<Pair<String, String>>()
            fun exploreFolder(folder: File, prefix: String = "") {
                folder.listFiles()?.forEach { file ->
                    if (file.isDirectory) {
                        exploreFolder(file, "$prefix/${file.name}")
                    } else {
                        val parentFolder = folder.name
                        val fileName = file.nameWithoutExtension
                        filesAndFolders.add(Pair(parentFolder, fileName))
                    }
                }
            }
            exploreFolder(downloadFolder)
            _repositories.emit(filesAndFolders)
        }
    }


    @Composable
    override fun Render() {
        Screen(
            repositories = _repositories.collectAsState().value,
            managementScreen = object : ManagementScreen {
                override fun onBack() {
                    this@DownloadScreenDecomposeComponent.onBack()
                }

                override fun getLocalRepositories() {
                    this@DownloadScreenDecomposeComponent.getLocalRepositories()
                }

                override fun navigationToDetails(first: String, second: String) {
                    this@DownloadScreenDecomposeComponent.navigationToDetails(first, second)
                }

            })
    }
}