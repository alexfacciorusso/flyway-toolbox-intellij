package com.alexfacciorusso.flywaytoolbox

import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

/**
 * @author alexfacciorusso
 */
class FlywayProjectComponent(val project: Project) : ProjectComponent {
    init {
        project.messageBus.connect(project).subscribe(VirtualFileManager.VFS_CHANGES, object : BulkFileListener {
            override fun before(p0: MutableList<out VFileEvent>) {}

            override fun after(events: MutableList<out VFileEvent>) {
                val flywayService = ServiceManager.getService(project, FlywayService::class.java)
                flywayService?.update()
            }
        })
    }

    override fun getComponentName(): String = "FlywayProjectComponent"

    override fun disposeComponent() {}

    override fun projectClosed() {}

    override fun initComponent() {}

    override fun projectOpened() {}
}