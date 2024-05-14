package mir.anika1d.repgit.downloadlogic.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import mir.anika1d.core.resources.AppResources

class DownloadReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (id != -1L) {
                Toast.makeText(
                    context,
                    "${context.resources.getText(AppResources.Values.Strings.DOWNLOAD_END.id)}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}