package mir.anika1d.repgit.network.core.data.request

import mir.anika1d.repgit.network.core.data.tools.extractParams
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(
    @SerialName("q") val query: String,
    @SerialName("per_page") val perPage: Int = 30,//По умолчанию: 30
    @SerialName("page") val page: Int = 1,//По умолчанию: 1
    @SerialName("sort") val sort: String? = null, //Возможные значения: created, updated
    @SerialName("order") var order: String? =null // Возможные значения: desc, asc
) {
    init {
        if (sort == null)
            order = null
    }
    companion object {
        fun fromUrl(url:String): SearchRequest {
                val params= url.extractParams()
            return (SearchRequest(
                query = params["q"]!!,
                perPage = params["per_page"]?.toInt()?:30,
                page = params["page"]!!.toInt(),
                sort = params["sort"],
                order = params["order"]
            ))
        }
    }
}


