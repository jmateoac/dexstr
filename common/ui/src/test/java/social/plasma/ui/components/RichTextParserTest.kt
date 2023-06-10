package social.plasma.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import app.cash.nostrino.crypto.PubKey
import app.cash.nostrino.crypto.SecKeyGenerator
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import shortBech32
import social.plasma.models.NoteId
import social.plasma.models.NoteMention
import social.plasma.models.ProfileMention
import social.plasma.ui.components.richtext.RichTextParser
import social.plasma.ui.components.richtext.RichTextTag

class RichTextParserTest {
    private val parser = RichTextParser(linkColor = Color.Blue)

    @Test
    fun `note with multiline note mentions`() = runTest {
        val plainText =
            "Some text \n\nhttps://apidocs.imgur.com/\n\n#[0]\n#[1] \n#[2] \n#[3] \n#[4]"

        val mentions = (0..4).associateWith {
            NoteMention(text = "@note$it", noteId = NoteId("$it"))
        }

        val result = parser.parse(plainText, mentions)

        assertThat(result.text)
            .isEqualTo("Some text \n\nhttps://apidocs.imgur.com/\n\n@note0\n@note1 \n@note2 \n@note3 \n@note4")
    }

    @Test
    fun `note with multiline profile mentions`() {
        runTest {
            val plainText =
                "Some text \n\nhttps://apidocs.imgur.com/\n\n#[0]\n#[1] \n#[2]"

            val keys = listOf(
                PubKey.parse("npub1mm90r3fkz436p4jgtkcqdy4uvelgx3xru6ej242ktx096flmmhjsfqrwg0"),
                PubKey.parse("npub1zvrwm4n0rk3hftwyzl8csjaulatuvwvk2c3kc8u89mssgq7qrvks5zvf63"),
                PubKey.parse("npub1jcgyf7tcshfkc48w40g2s3769h0uw79mnx73hspwcpdgy049rm5spf50ps"),
            )

            val mentions = keys.mapIndexed { i, key ->
                i to ProfileMention(text = "@note$i", pubkey = key)
            }.toMap()

            val result = parser.parse(plainText, mentions)

            assertThat(result.text)
                .isEqualTo("Some text \n\nhttps://apidocs.imgur.com/\n\n@note0\n@note1 \n@note2")
        }
    }

    @Test
    fun `note with characters after mention`() = runTest {
        val plainText =
            "Hey #[0], have you considered collaborating with #[1]?"

        val mentions = mapOf(
            0 to ProfileMention(
                "@joe",
                PubKey.parse("npub1mm90r3fkz436p4jgtkcqdy4uvelgx3xru6ej242ktx096flmmhjsfqrwg0")
            ),
            1 to ProfileMention(
                "@will",
                PubKey.parse("npub1jcgyf7tcshfkc48w40g2s3769h0uw79mnx73hspwcpdgy049rm5spf50ps")
            )
        )

        val result = parser.parse(plainText, mentions)

        assertThat(result.text).isEqualTo(
            "Hey @joe, have you considered collaborating with @will?"
        )
    }

    @Test
    fun `note with characters before mention`() = runTest {
        val plainText =
            "Best people to follow:#[0] and #[1]. #followme3000"

        val mentions = mapOf(
            0 to ProfileMention(
                "@joe",
                PubKey.parse("npub1felyu03mh8xdszx927cyvgyf9yp83feyyug4505aa8cma5t6g9vql86w95")
            ),
            1 to ProfileMention(
                "@will",
                PubKey.parse("npub1tjkc9jycaenqzdc3j3wkslmaj4ylv3dqzxzx0khz7h38f3vc6mls4ys9w3")
            )
        )

        val result = parser.parse(plainText, mentions)

        assertThat(result.text).isEqualTo(
            "Best people to follow:@joe and @will. #followme3000"
        )

        assertThat(
            result.getStringAnnotations(
                RichTextTag.HASHTAG,
                0,
                result.length
            )
        ).containsExactly(
            AnnotatedString.Range(
                item = "#followme3000",
                start = 38,
                end = 51,
                tag = RichTextTag.HASHTAG
            )
        )
    }

    @Test
    fun `note with numeric hashtag`() = runTest {
        val plainText =
            "It's #420 💨"


        val result = parser.parse(plainText, emptyMap())

        assertThat(result.text).isEqualTo(
            "It's #420 💨"
        )

        assertThat(
            result.getStringAnnotations(
                RichTextTag.HASHTAG,
                0,
                result.length
            )
        ).containsExactly(
            AnnotatedString.Range(
                item = "#420",
                start = 5,
                end = 9,
                tag = RichTextTag.HASHTAG
            )
        )
    }

    @Test
    fun `note with nip-21 npub mention`() = runTest {
        val pubkey = SecKeyGenerator().generate().pubKey
        val plainText = "Hey, check out this thing:nostr:${pubkey.npub}, it's pretty cool!"
        val humanReadableName = "@joe"
        val mentions = mapOf(
            0 to ProfileMention(
                humanReadableName,
                pubkey
            ),
        )

        val result = parser.parse(plainText, mentions)

        val expectedOutput = "Hey, check out this thing:$humanReadableName, it's pretty cool!"
        assertThat(result.text).isEqualTo(expectedOutput)
        assertThat(
            result.getStringAnnotations(
                RichTextTag.PROFILE,
                0,
                result.length
            )
        ).containsExactly(
            AnnotatedString.Range(
                item = pubkey.hex(),
                start = expectedOutput.indexOf(humanReadableName),
                end = expectedOutput.indexOf(humanReadableName) + humanReadableName.length,
                tag = RichTextTag.PROFILE
            )
        )
    }

    @Test
    fun `nip-21 mention without any mention tags`() = runTest {
        val pubkey = SecKeyGenerator().generate().pubKey
        val plainText = "Hey, check out this thing: nostr:${pubkey.npub}, it's pretty cool!"
        val mentions = emptyMap<Int, ProfileMention>()

        val result = parser.parse(plainText, mentions)

        val expectedOutput = "Hey, check out this thing: ${pubkey.shortBech32()}, it's pretty cool!"
        assertThat(result.text).isEqualTo(expectedOutput)
    }

    @Test
    fun `note with invalid nip-21 npub mention`() = runTest {
        val plainText =
            "Hey, check out this thing: nostr:npub1mm910r3fkz436p4jgtkcqdy4uvelgx3xru6ej242ktx096flmmhjsfqrwg0, it's pretty cool!"
        val mentions = mapOf(
            0 to ProfileMention(
                "@joe",
                PubKey.parse("npub1mm90r3fkz436p4jgtkcqdy4uvelgx3xru6ej242ktx096flmmhjsfqrwg0")
            ),
        )

        val result = parser.parse(plainText, mentions)

        assertThat(result.text).isEqualTo(plainText)
    }

    @Test
    fun `note with multiple nip-21 npub mentions in the same "word"`() = runTest {
        val pubkey1 = SecKeyGenerator().generate().pubKey
        val pubkey2 = SecKeyGenerator().generate().pubKey
        val plainText =
            "Hey, check out this thing:nostr:${pubkey1.npub},nostr:${pubkey2.npub}, it's pretty cool!"
        val pubkey1HumanName = "@joe"
        val pubkey2HumanName = "@will"
        val mentions = mapOf(
            0 to ProfileMention(
                pubkey1HumanName,
                pubkey1
            ),
            1 to ProfileMention(
                pubkey2HumanName,
                pubkey2
            ),
        )

        val result = parser.parse(plainText, mentions)

        val expectedOutput = "Hey, check out this thing:@joe,@will, it's pretty cool!"
        assertThat(result.text).isEqualTo(expectedOutput)
        assertThat(
            result.getStringAnnotations(
                RichTextTag.PROFILE,
                0,
                result.length
            )
        ).containsExactly(
            AnnotatedString.Range(
                item = pubkey1.hex(),
                start = expectedOutput.indexOf(pubkey1HumanName),
                end = expectedOutput.indexOf(pubkey1HumanName) + pubkey1HumanName.length,
                tag = RichTextTag.PROFILE
            ),
            AnnotatedString.Range(
                item = pubkey2.hex(),
                start = expectedOutput.indexOf(pubkey2HumanName),
                end = expectedOutput.indexOf(pubkey2HumanName) + pubkey2HumanName.length,
                tag = RichTextTag.PROFILE
            )
        )
    }
}
