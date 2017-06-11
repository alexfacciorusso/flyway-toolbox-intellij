package com.alexfacciorusso.flywaytoolbox

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.flywaydb.core.Flyway

/**
 * @author alexfacciorusso
 */
abstract class FlywayToolsAction : AnAction() {
    override fun update(actionEvent: AnActionEvent) {
        super.update(actionEvent)

        val project = actionEvent.project
        actionEvent.presentation.isVisible = project != null
        if (project == null) return

        val flywayService = ServiceManager.getService(project, FlywayService::class.java) ?: return
        //TODO actionEvent.presentation.isEnabled = flywayService.hasConfiguration
    }

    override fun actionPerformed(actionEvent: AnActionEvent) {
        val project = actionEvent.project ?: return
        val flywayService = ServiceManager.getService(project, FlywayService::class.java) ?: return

        flywayActionPerformed(project, flywayService.flyway)
    }

    abstract fun flywayActionPerformed(project: Project, flyway: Flyway)
}
