package com.alexfacciorusso.flywaytoolbox

import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import org.flywaydb.core.Flyway

/**
 * @author alexfacciorusso
 */
class FlywayCleanAction : FlywayToolsAction() {
    override fun flywayActionPerformed(project: Project, flyway: Flyway) {
        try {
            flyway.clean()
            flywayNotification("The db has been initializated.", NotificationType.INFORMATION, project)
        } catch(e: Exception) {
            flywayNotification(e.message!!, NotificationType.ERROR, project)
        }
    }
}
