package com.tylerb.kmm.shared.cache

import com.tylerb.kmm.shared.entity.Links
import com.tylerb.kmm.shared.entity.Rocket
import com.tylerb.kmm.shared.entity.RocketLaunch

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries


    // DELETE ALL
    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllRockets()
            dbQuery.removeAllLaunches()
        }
    }

    // GET
    internal fun getAllLaunches(): List<RocketLaunch> {
        return dbQuery.selectAllLaunchesInfo(::mapLaunchSelecting).executeAsList()
    }

    // database RocketLaunch to data class RocketLaunch
    private fun mapLaunchSelecting(
        flightNumber: Long,
        missionName: String,
        launchYear: Int,
        rocketId: String,
        details: String?,
        launchSuccess: Boolean?,
        launchDateUTC: String,
        missionPatchUrl: String?,
        articleUrl: String?,
        rocket_id: String?,
        name: String?,
        type: String?
    ): RocketLaunch {
        return RocketLaunch(
            flightNumber = flightNumber.toInt(),
            missionName = missionName,
            launchYear = launchYear,
            details = details,
            launchDateUTC = launchDateUTC,
            launchSuccess = launchSuccess,
            rocket = Rocket(
                id = rocketId,
                name = name ?: "null",
                type = type ?: "null"
            ),
            links = Links(
                missionPatchUrl = missionPatchUrl,
                articleUrl = articleUrl
            )
        )
    }

    // INSERT
    internal fun createLaunches(launches: List<RocketLaunch>) {
        dbQuery.transaction {
            launches.forEach { launch ->
                val rocket = dbQuery.selectRocketById(launch.rocket.id).executeAsOneOrNull()
                rocket?.let { insertRocket(launch) }
                insertLaunch(launch)

            }
        }

    }

    private fun insertRocket(launch: RocketLaunch) {
        val rocket = launch.rocket
        dbQuery.insertRocket(
            id = rocket.id,
            name = rocket.id,
            type = rocket.type
        )
    }

    private fun insertLaunch(launch: RocketLaunch) {
        with(launch) {
            dbQuery.insertLaunch(
                flightNumber = flightNumber.toLong(),
                missionName = missionName,
                launchYear = launchYear,
                rocketId = rocket.id,
                details = details,
                launchSuccess = launchSuccess ?: false,
                launchDateUTC = launchDateUTC,
                missionPatchUrl = links.missionPatchUrl,
                articleUrl = links.articleUrl
            )
        }
    }






}