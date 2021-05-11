import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineStart.*
import kotlinx.coroutines.flow.*
import kotlin.test.*

private const val TEST_TIMEOUT = 1000L

class ToList {

    @Test
    fun verySlowFlow() = runBlocking {
        val flow = flow {
            emit("hello")
            delay(Long.MAX_VALUE)
        }

        val emissions = withTimeout(TEST_TIMEOUT) {
            flow.toList().toSet()
        }

        assertEquals(setOf("hello", "goodbye"), emissions)
    }

    @Test
    fun flowWithExpectedError() = runBlocking {
        val flow = flow<Nothing> {
            throw RuntimeException()
        }

        try {
            flow.collect()
            throw AssertionError("Didn't throw exception")
        } catch (_: RuntimeException) {
        }
    }

    @Test
    fun flowThatReactsToExternalTriggers(): Unit = runBlocking {
        val triggersFlow = MutableSharedFlow<String>()
        val resultsFlow = triggersFlow.map { "Received $it" }

        coroutineScope {
            launch(start = UNDISPATCHED) {
                resultsFlow.collect {
                    assertEquals("Received trigger $it", it)
                }
            }
            launch {
                repeat(2) {
                    triggersFlow.emit("trigger $it")
                }
            }
        }
    }

}