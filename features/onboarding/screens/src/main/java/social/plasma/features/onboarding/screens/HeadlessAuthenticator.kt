package social.plasma.features.onboarding.screens

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class HeadlessAuthenticator(
    val exitScreen: Screen? = null,
) : Screen
