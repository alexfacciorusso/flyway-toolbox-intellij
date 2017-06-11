package com.alexfacciorusso.flywaytoolbox

import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import com.intellij.ui.table.JBTable
import org.flywaydb.core.Flyway
import java.awt.Dimension

/**
 * @author alexfacciorusso
 */
class FlywayInfoAction : FlywayToolsAction() {
    override fun flywayActionPerformed(project: Project, flyway: Flyway) {
        val infos = try {
            flyway.info()?.all()
        } catch(e: Exception) {
            flywayNotification(e.message!!, NotificationType.ERROR, project)
            null
        } ?: return

        val table = JBTable().apply {
            emptyText.text = "No migrations to show."
            preferredSize = Dimension(400, 200)
        }
        DialogBuilder(project).centerPanel(panel(LCFlags.fill) {
            row {
                table(CCFlags.grow)
            }
        }).okActionEnabled(false).show()
    }
}
