package com.alexfacciorusso.flywaytoolbox

import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.mysql.cj.jdbc.MysqlDataSource
import org.flywaydb.core.Flyway
import java.util.*

/**
 * @author alexfacciorusso
 */
class FlywayService(val project: Project) {
    val flyway: Flyway = Flyway()
    val flywayConfFilename = "flyway.conf"
    var hasConfiguration = false

    fun update() {
        val flywayConfFile = project.baseDir.findChild(flywayConfFilename)
        if (flywayConfFile != null && flywayConfFile.exists()) {
            hasConfiguration = true
            val properties = Properties().apply {
                load(flywayConfFile.inputStream)
            }
            val url = properties.getProperty("flyway.url")
            val user = properties.getProperty("flyway.user")
            val password = properties.getProperty("flyway.password", "")

            if (url != null && user != null) {
                val datasource = MysqlDataSource().apply {
                    setURL(url)
                    setUser(user)
                    setPassword(password)
                }
                flyway.dataSource = datasource
            } else flyway.dataSource = null
        } else {
            flywayNotification("Add flyway.config for Flyway", NotificationType.WARNING, project)
        }
    }
}
