package com.bismark.core_network.fake

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FakeJPGNetworkTest {

    private lateinit var subject: FakeJPGNetwork

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        subject = FakeJPGNetwork(
            ioDispatcher = testDispatcher,
            networkJson = Json { ignoreUnknownKeys = true }
        )
    }

    @Test
    fun testDeserializationOfTopics() = runTest(testDispatcher) {
        assertEquals(
            FakeDataSource.sampleTopic,
            subject.getTopics().first()
        )
    }

    @Test
    fun testDeserializationOfNewsResources() = runTest(testDispatcher) {
        assertEquals(
            FakeDataSource.sampleResource,
            subject.getNewsResources().first()
        )
    }
}
