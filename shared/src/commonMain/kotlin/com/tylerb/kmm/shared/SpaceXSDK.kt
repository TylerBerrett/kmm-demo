package com.tylerb.kmm.shared

import com.tylerb.kmm.shared.cache.Database
import com.tylerb.kmm.shared.cache.DatabaseDriverFactory
import com.tylerb.kmm.shared.entity.RocketLaunch
import com.tylerb.kmm.shared.network.SpaceXApi
import io.ktor.client.features.*

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()


    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }

}