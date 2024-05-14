package mir.anika1d.repgit.network.core.data.tools

fun String.extractParams(): Map<String, String> {
    val queryIndex = this.indexOf('?')
    if (queryIndex == -1 || queryIndex == this.lastIndex) {
        return emptyMap()
    }

    val queryString = this.substring(queryIndex + 1)
    val queryParams = mutableMapOf<String, String>()

    for (pair in queryString.split('&')) {
        val (key, value) = pair.split('=').map { it.trim() }
        queryParams[key] = value
    }
    return queryParams

}

