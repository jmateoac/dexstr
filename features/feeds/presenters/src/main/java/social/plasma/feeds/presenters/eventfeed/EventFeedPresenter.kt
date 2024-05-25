package social.plasma.feeds.presenters.eventfeed

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import social.plasma.features.feeds.presenters.R
import social.plasma.features.feeds.screens.eventfeed.EventFeedUiEvent
import social.plasma.features.feeds.screens.eventfeed.EventFeedUiState
import social.plasma.features.feeds.screens.feeditems.notes.NoteScreen
import social.plasma.models.Event
import social.plasma.models.EventModel
import social.plasma.shared.utils.api.StringManager
import kotlin.math.min

class EventFeedPresenter @AssistedInject constructor(
    private val stringManager: StringManager,
    @Assisted private val navigator: Navigator,
    @Assisted private val pagingData: Flow<PagingData<EventModel>>,
    @Assisted private val screenProvider: EventFeedUiScreenProvider,
    @Assisted private val initialIndex: Int,
) : Presenter<EventFeedUiState> {

    @Composable
    override fun present(): EventFeedUiState {
        val coroutineScope = rememberCoroutineScope()

        val listState = rememberLazyListState(initialIndex)

        val currentVisibleIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

        var currentFeedItemCount by remember { mutableStateOf(0) }

        val initialFeedCount by produceRetainedState(
            initialValue = 0,
            currentFeedItemCount,
            currentVisibleIndex
        ) {
            if (value == 0) {
                value = currentFeedItemCount
            }

            if (currentVisibleIndex <= currentFeedItemCount - value) {
                value = currentFeedItemCount - currentVisibleIndex
            }
        }

        val unseenItemCount by produceState(
            initialValue = 0,
            currentVisibleIndex,
            currentFeedItemCount,
            initialFeedCount
        ) {
            value = min(currentVisibleIndex, currentFeedItemCount - initialFeedCount)
        }

        val refreshText = remember(unseenItemCount) {
            if (unseenItemCount < NOTE_COUNT_MAX) {
                stringManager.getFormattedString(
                    R.string.new_notes_count,
                    mapOf("count" to unseenItemCount)
                )
            } else {
                stringManager.getFormattedString(
                    R.string.many_new_notes,
                    mapOf("count" to "$NOTE_COUNT_MAX+")
                )
            }
        }

        return EventFeedUiState(
            listState = listState,
            items = pagingData,
            displayRefreshButton = unseenItemCount > 0,
            refreshText = refreshText,
            screenProvider = screenProvider,
        ) { event ->
            when (event) {
                is EventFeedUiEvent.OnFeedCountChange -> currentFeedItemCount = event.itemCount
                EventFeedUiEvent.OnRefreshButtonClick -> coroutineScope.launch {
                    listState.scrollToItem(0)
                }

                is EventFeedUiEvent.OnChildNavEvent -> navigator.onNavEvent(event.navEvent)
            }
        }
    }

    companion object {
        private const val NOTE_COUNT_MAX = 99
    }

    @AssistedFactory
    interface Factory {
        fun create(
            navigator: Navigator,
            pagingData: Flow<PagingData<EventModel>>,
            screenProvider: (EventModel) -> Screen = EventFeedDefaults.defaultScreenProvider,
            initialIndex: Int = 0,
        ): EventFeedPresenter
    }

}

object EventFeedDefaults {
    val noteCardKinds = setOf(
        Event.Kind.Repost,
        Event.Kind.Note,
        Event.Kind.Audio,
    )

    val defaultScreenProvider = { event: EventModel ->
        when (event.kind) {
            in noteCardKinds -> NoteScreen(event)
            else -> throw IllegalArgumentException("Unknown event kind: ${event.kind}")
        }
    }
}

typealias EventFeedUiScreenProvider = (EventModel) -> Screen
