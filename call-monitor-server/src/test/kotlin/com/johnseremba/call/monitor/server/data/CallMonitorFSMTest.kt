package com.johnseremba.call.monitor.server.data

import com.johnseremba.call.monitor.server.data.CallState.CallEnded
import com.johnseremba.call.monitor.server.data.CallState.MissedCall
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CallMonitorFSMTest {

    private lateinit var callMonitorFSM: CallMonitorFSM

    @Before
    fun setUp() {
        callMonitorFSM = CallMonitorFSM()
    }

    @Test
    fun `default fsm state should be idle`() {
        // Given an instance of CallMonitorFSM
        // When getCurrentState() is called
        val state = callMonitorFSM.getCurrentState()

        // Then the default state is Idle
        assert(state is CallState.Idle)
    }

    @Test
    fun `fsm state should be Ringing when setCallRingingState() is called`() {
        // Given an instance of CallMonitorFSM
        // When setCallRingingState() is called
        callMonitorFSM.setCallRingingState("1234", "Doe", false)

        // Then the current state is Ringing
        val state = callMonitorFSM.getCurrentState()
        assert(state is CallState.Ringing)
    }

    @Test
    fun `fsm state should be CallAnswered when setCallAnsweredState is called`() {
        // Given an instance of CallMonitorFSM
        // When setCallAnsweredState() is called
        callMonitorFSM.setCallAnsweredState(true)

        // Then the current is state is CallAnswered
        val state = callMonitorFSM.getCurrentState()
        assert(state is CallState.CallAnswered)
    }

    @Test
    fun `fsm state should be CallEnded when setCallEndedState is called`() {
        // Given an instance of CallMonitorFSM
        // When setCallEndedState() is called
        callMonitorFSM.setCallEndedState()

        // Then the current state is CallEnded
        val state = callMonitorFSM.getCurrentState()
        assert(state is CallEnded)
    }

    @Test
    fun `fsm state should be MissedCall when setMissedCallState is called`() {
        // Given an instance of CallMonitorFSM
        // When setMissedCallState() is called
        callMonitorFSM.setMissedCallState()

        // Then the current state is MissedCall
        val state = callMonitorFSM.getCurrentState()
        assert(state is MissedCall)
    }

    @Test
    fun `queryStatus() should increment the queryCount flag when called`() {
        // Given the current state is CallAnswered
        callMonitorFSM.setCallAnsweredState(true)

        // When queryStatus() is called
        callMonitorFSM.queryStatus()

        // Then the queryCount flag should be incremented to 1
        val callLog = callMonitorFSM.getCallLog()

        assertEquals(1, callLog.timesQueried)
    }

    @Test
    fun `queryStatus() should not increment when CallStatus is CallEnded`() {
        // Given the current state is CallEnded
        callMonitorFSM.setCallEndedState()

        // When queryStatus() is called
        callMonitorFSM.queryStatus()

        // Then the queryCount flag should not be incremented
        val callLog = callMonitorFSM.getCallLog()

        assertEquals(0, callLog.timesQueried)
    }

    @Test
    fun `callEntry should be populated appropriately for a call session`() {
        // Given an incoming call
        callMonitorFSM.setCallRingingState(
            number = "+49 0174 123456",
            name = "John Doe",
            isIncoming = true
        )

        // When the call gets answered
        callMonitorFSM.setCallAnsweredState(true)

        // And the State is queried two times
        callMonitorFSM.queryStatus()
        callMonitorFSM.queryStatus()

        // And the call ends
        callMonitorFSM.setCallEndedState()

        // Then the call Log entry must be populated appropriately
        val callLog = callMonitorFSM.getCallLog()

        assertNotNull(callLog.beginning)
        assertEquals("+49 0174 123456", callLog.number)
        assertEquals("John Doe", callLog.name)
        assertEquals(2, callLog.timesQueried)
        assertTrue(callLog.isIncoming)
    }
}
