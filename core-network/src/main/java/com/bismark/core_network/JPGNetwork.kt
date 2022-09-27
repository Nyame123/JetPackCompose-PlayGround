package com.bismark.core_network

import com.bismark.core_network.model.NetworkTopic

/**
 * Interface representing network calls to the JPG backend
 */
interface JPGNetwork {

    suspend fun getTopics(ids: List<String>? = null): List<NetworkTopic>
}
