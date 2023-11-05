package jp.co.yumemi.android.code_check

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit4 TestRule for overriding the main dispatcher used by Kotlin Coroutines.
 *
 * This TestRule allows you to change the main dispatcher to a specified [testDispatcher]
 * during a test's execution and then reset it back to the original dispatcher when the test
 * is finished. It's useful for testing coroutines that use the main dispatcher.
 *
 * @param testDispatcher The test dispatcher to be used during the test.
 */
@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    /**
     * Overrides the main dispatcher with the [testDispatcher] when the test is starting.
     *
     * @param description The description of the test that is starting.
     */
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Resets the main dispatcher back to the original dispatcher when the test is finished.
     *
     * @param description The description of the test that is finished.
     */
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}