package com.alexfacciorusso.flywaytoolbox

import com.intellij.notification.NotificationType
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBList
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import groovy.swing.impl.ListWrapperListModel


/**
 * @author alexfacciorusso
 */
class FlywayToolboxToolWindowFactory : ToolWindowFactory {
    private lateinit var toolWindow: ToolWindow
    private val attributesList = mutableListOf<String>()
    private val listModel = ListWrapperListModel<String>(attributesList)

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        this.toolWindow = toolWindow
        val contentFactory = ContentFactory.SERVICE.getInstance()

        val listComponent = JBList<String>(listModel)
        val content = contentFactory.createContent(
                panel(LCFlags.noGrid, LCFlags.fill, title = "Properties") {
                    row {
                        listComponent(CCFlags.grow)
                    }
                }, "", false)

        updateFlywayInfo(project)

        project.getComponent(FlywayProjectComponent::class.java)
    }

    override fun init(window: ToolWindow) {
        window.title = "Flyway"
    }

    private fun updateFlywayInfo(project: Project) {
        val flywayService = ServiceManager.getService(project, FlywayService::class.java) ?: return
        if (!flywayService.hasConfiguration) return
        val info = try {
            flywayService.flyway.info() ?: return
        } catch (e: Throwable) {
            flywayNotification(e.message!!, NotificationType.ERROR)
            null
        } ?: return

        listModel.add("Cazz")
        listModel.add("<b>Description:</b> ${info.current().description}")
    }
}