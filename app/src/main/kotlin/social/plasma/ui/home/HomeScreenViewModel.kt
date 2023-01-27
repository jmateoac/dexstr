package social.plasma.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import social.plasma.PubKey
import social.plasma.nostr.models.UserMetaData
import social.plasma.repository.AccountStateRepository
import social.plasma.repository.UserMetaDataRepository
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    accountStateRepository: AccountStateRepository,
    userMetaDataRepository: UserMetaDataRepository,
) : ViewModel() {

    private val myPubKey = PubKey.of(accountStateRepository.getPublicKey()!!)

    private val initialUiState = HomeScreenUiState.Loaded(
        userPubkey = myPubKey,
        userMetadata = UserMetaData(
            name = null,
            picture = "https://api.dicebear.com/5.x/bottts/jpg?seed=${myPubKey.hex}",
            displayName = myPubKey.shortBech32,
            about = null,
            nip05 = null,
            website = null,
            banner = null,
        )
    )

    val uiState: StateFlow<HomeScreenUiState> =
        userMetaDataRepository.observeUserMetaData(myPubKey.hex)
            .distinctUntilChanged()
            .mapLatest {
                HomeScreenUiState.Loaded(
                    userMetadata = it,
                    userPubkey = myPubKey,
                )
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), initialUiState)
}
