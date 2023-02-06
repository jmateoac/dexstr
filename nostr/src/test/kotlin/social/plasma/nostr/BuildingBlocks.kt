package social.plasma.nostr

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import social.plasma.nostr.relay.RelayImpl
import social.plasma.nostr.relay.message.NostrMessageAdapter

object BuildingBlocks {

    val moshi = Moshi.Builder()
        .add(NostrMessageAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
        .build()

    val scarlet = Scarlet.Builder()
        .addMessageAdapterFactory(MoshiMessageAdapter.Factory(moshi))
        .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
        .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())

    fun testRelay(scope: CoroutineScope) = RelayImpl(
        "ws:localhost:7707",
        scarlet
            .webSocketFactory(client.newWebSocketFactory("ws:localhost:7707"))
            .build()
            .create(),
        scope
    )

    const val JemPubKey = "8366029071b385def2e4fb964d2d73e6f4246131ac1ff7608bbcb1971c5081d2"
    const val JackPubKey = "82341f882b6eabcd2ba7f1ef90aad961cf074af15b9ef44a09f9d2a8fbfbe6a2"

}
