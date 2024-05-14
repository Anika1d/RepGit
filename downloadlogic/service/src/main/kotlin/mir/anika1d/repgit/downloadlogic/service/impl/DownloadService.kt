package mir.anika1d.repgit.downloadlogic.service.impl

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import mir.anika1d.core.resources.AppResources
import mir.anika1d.repgit.downloadlogic.service.api.IDownloadService
import mir.anika1d.repgit.downloadlogic.service.data.request.DownloadRequest
import java.io.File

class DownloadService(private val context: Context) : IDownloadService {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)


    override fun downloadFile(downloadRequest: DownloadRequest): Long {
        val subPath =
            "/repgit/${downloadRequest.owner}/${downloadRequest.name.replace(".", "_")}.zip"
        val duplicateFile = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/Download/repgit/${downloadRequest.owner}",
            "${downloadRequest.name.replace(".", "_")}.zip"
        )
        if (duplicateFile.exists()) {
            try {
                Toast.makeText(
                    context,
                    "File is exist. The file will be overwritten",
                    Toast.LENGTH_LONG
                ).show()
                duplicateFile.delete()
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Delete File failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        Toast.makeText(
            context,
            "${context.resources.getText(AppResources.Values.Strings.DOWNLOAD_START.id)} " +
                    "${downloadRequest.name}.zip",
            Toast.LENGTH_LONG
        ).show()
        val request = DownloadManager.Request(Uri.parse(downloadRequest.url))
            .setMimeType("application/zip")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(downloadRequest.name)
            .addRequestHeader("Authorization", "Bearer ${downloadRequest.jwtToken}")
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                subPath
            )
        return downloadManager.enqueue(request)
    }
}
