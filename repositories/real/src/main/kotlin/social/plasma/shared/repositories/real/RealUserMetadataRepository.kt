package social.plasma.shared.repositories.real

import kotlinx.coroutines.flow.Flow
import social.plasma.data.daos.UserMetadataDao
import social.plasma.models.PubKey
import social.plasma.models.UserMetadataEntity
import social.plasma.shared.repositories.api.UserMetadataRepository
import javax.inject.Inject

internal class RealUserMetadataRepository @Inject constructor(
    private val userMetadataDao: UserMetadataDao,
): UserMetadataRepository {
    override fun search(query: String): Flow<List<String>> {
        TODO("Not yet implemented")
    }

    override fun observeUserMetaData(pubKey: PubKey): Flow<UserMetadataEntity?> {
        return userMetadataDao.observeUserMetadata(pubKey.hex)
    }
}