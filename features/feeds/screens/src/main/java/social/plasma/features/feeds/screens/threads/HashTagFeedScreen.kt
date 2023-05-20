package social.plasma.features.feeds.screens.threads

import com.slack.circuit.runtime.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class HashTagFeedScreen(
    val hashTag: String,
) : Screen
