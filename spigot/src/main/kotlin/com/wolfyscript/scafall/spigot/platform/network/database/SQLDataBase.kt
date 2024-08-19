/*
 *       WolfyUtilities, APIs and Utilities for Minecraft Spigot plugins
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scafall.spigot.platform.network.database

import com.wolfyscript.scafall.Scafall
import java.sql.*
import java.util.*

class SQLDataBase(
    private val api: Scafall, host: String, database: String,
    private val username: String,
    private val password: String, port: Int
) {

    private var connection: Connection? = null

    private val dataBaseURL = "jdbc:mysql://$host:$port/$database?useSSL=false&useUnicode=true&characterEncoding=utf8"

    private val maxPool = "250"
    private var properties: Properties? = null

    /**
     * Creates the properties for the connection.
     */
    private fun getProperties(): Properties {
        if (properties == null) {
            properties = Properties()
            properties!!.setProperty("user", username)
            properties!!.setProperty("password", password)
            properties!!.setProperty("MaxPooledStatements", maxPool)
        }
        return properties!!
    }

    /**
     * Opens the connection on the main thread. If the connection is already established this will just return it.
     * Attention! When using this on the main thread, it will cause it to hold till the connection is ready or times out!
     *
     * @return The established connection to the database
     */
    fun open(): Connection? {
        if (connection == null) {
            try {
                synchronized(this) {
                    Class.forName(DATABASE_DRIVER)
                    connection = DriverManager.getConnection(dataBaseURL, getProperties())
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        return connection
    }

    fun close() {
        if (connection != null) {
            try {
                connection!!.close()
                connection = null
            } catch (ex: SQLException) {
                ex.printStackTrace()
            }
        }
    }

    fun executeUpdate(preparedStatement: PreparedStatement) {
        try {
            open() //Makes sure that the connection is available. If not, it tries to connect.
            preparedStatement.executeUpdate()
            preparedStatement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun executeAsyncUpdate(preparedStatement: PreparedStatement) {
        Thread { executeUpdate(preparedStatement) }.start()
    }

    fun executeQuery(preparedStatement: PreparedStatement): ResultSet? {
        try {
            open() //Makes sure that the connection is available. If not, it tries to connect.
            return preparedStatement.executeQuery()
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
    }

    companion object {
        private const val DATABASE_DRIVER = "com.mysql.jdbc.Driver"
    }
}
