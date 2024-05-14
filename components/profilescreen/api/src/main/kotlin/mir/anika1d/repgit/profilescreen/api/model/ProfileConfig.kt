package mir.anika1d.repgit.profilescreen.api.model

import kotlinx.serialization.Serializable
import mir.anika1d.repgit.core.data.model.repository.Owner

@Serializable
data class ProfileConfig(val user: Owner)
