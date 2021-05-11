import app.cash.turbine.test
import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineStart.*
import kotlinx.coroutines.flow.*
import kotlin.test.*
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@ExperimentalTime
class WithTurbine {

    @Test
    fun verySlowFlow() = runBlocking {
        val flow = flow {
            emit("hello")
            delay(Long.MAX_VALUE)
        }

        flow.test {
            assertEquals("hello", expectItem())
            assertEquals("goodbye", expectItem())
        }
    }

    @Test
    fun flowWithExpectedError() = runBlocking {
        val flow = flow<Nothing> {
            throw RuntimeException()
        }

        flow.test {
            assertTrue(expectError() is RuntimeException)
        }
    }

    @Test
    fun flowThatReactsToExternalTriggers(): Unit = runBlocking {
        val triggersFlow = MutableSharedFlow<String>()
        val flow = triggersFlow.map { "Received $it" }

        flow.test {
            expectNoEvents()
            repeat(2) { i ->
                triggersFlow.emit("trigger $i")
                assertEquals("Received trigger $i", expectItem())
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}