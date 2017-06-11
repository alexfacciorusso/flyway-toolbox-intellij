package com.alexfacciorusso.flywaytoolbox

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project

/**
 * @author alexfacciorusso
 */
object FlywayToolbox {
    const val NOTIFICATION_ID = "flyway-toolbox"
    const val PLUGIN_NAME = "Flyway Toolbox"


}

fun flywayNotification(message: String, notificationType: NotificationType, project: Project? = null) {
    Notifications.Bus.notify(Notification(
            FlywayToolbox.NOTIFICATION_ID,
            FlywayToolbox.PLUGIN_NAME,
            message,
            notificationType
    ), project)
}