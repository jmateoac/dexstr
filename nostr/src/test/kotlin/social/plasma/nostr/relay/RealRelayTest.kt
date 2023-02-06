package social.plasma.nostr.relay

import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import social.plasma.nostr.BuildingBlocks.JemPubKey
import social.plasma.nostr.BuildingBlocks.moshi
import social.plasma.nostr.BuildingBlocks.scarlet
import social.plasma.nostr.models.UserMetaData
import social.plasma.nostr.relay.message.ClientMessage.SubscribeMessage
import social.plasma.nostr.relay.message.Filter

@OptIn(ExperimentalCoroutinesApi::class)
class RealRelayTest : StringSpec({

/*
    val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
        .build()

    // TODO - a deterministic test using a mock webserver
    val relayUrl = "wss://brb.io"

    "can get events from a relay" {
        runTest {
            val relay = RelayImpl(
                relayUrl,
                scarlet
                    .webSocketFactory(client.newWebSocketFactory(relayUrl))
                    .build()
                    .create(),
                this
            )
            relay.connect()
            val events = relay.subscribe(SubscribeMessage(Filter.contactList(JemPubKey)))
            events.first().event.content shouldContain "nostr.satsophone.tk"
            relay.disconnect()
        }
    }

    "can subscribe to a relay" {
        val relay = RelayImpl(
            relayUrl,
            scarlet
                .webSocketFactory(client.newWebSocketFactory(relayUrl))
                .build()
                .create(),
            this
        )
        relay.connect()
        relay.subscribe(SubscribeMessage(Filter.contactList(JemPubKey)))

        val event = relay.subscribe(
            SubscribeMessage(
                Filter.userMetaData("67e4027f797f15c8a89de7a03a07ddb0efe63985fa6716f8b3c742a008ca0be7")
            )
        ).filter { it.event.content.contains("heastmined") }
            .first()


        event should {
            moshi.adapter(UserMetaData::class.java)
                .fromJson(it.event.content)?.name shouldBe "heastmined"
        }

        relay.disconnect()
    }
*/
})
