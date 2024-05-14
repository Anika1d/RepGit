package mir.anika1d.repgit.downloadlogic.service.api

import mir.anika1d.repgit.downloadlogic.service.data.request.DownloadRequest

interface IDownloadService {
    fun downloadFile(downloadRequest: DownloadRequest): Long
}
