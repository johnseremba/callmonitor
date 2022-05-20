package com.johnseremba.call.monitor.server.routes.call_logs

import com.johnseremba.call.monitor.server.data.CallEntry
import com.johnseremba.call.monitor.server.data.Response
import com.johnseremba.call.monitor.server.data.repo.Repository
import com.nhaarman.mockitokotlin2.whenever
import java.util.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class GetCallLogsUseCaseTest {

    private lateinit var closeable: AutoCloseable

    @Mock
    private lateinit var repository: Repository

    @InjectMocks
    private lateinit var getCallLogsUseCase: GetCallLogsUseCase

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
    }

    @After
    fun tearDown() {
        closeable.close()
    }

    @Test
    fun `getCallLogsUseCase should return a response with call logs`() = runBlocking {
        // Given a list of call logs
        val logs: List<CallEntry> = listOf(
            CallEntry(Date(), "10", "+49 174 123456", "John Doe", 2, true),
            CallEntry(Date(), "5", "+49 154 123456", "Jane Doe", 2, true),
        )
        whenever(repository.getCallLogs()).thenReturn(logs)

        // When getCallLogsUseCase() is invoked
        val response: Response<List<CallEntry>> = getCallLogsUseCase()

        // Then 2 call logs are returned
        val data = response.data
        assert(data.size == 2)

        // And the second entry is Jane Doe
        assert(data[1].name == "Jane Doe")
    }
}
