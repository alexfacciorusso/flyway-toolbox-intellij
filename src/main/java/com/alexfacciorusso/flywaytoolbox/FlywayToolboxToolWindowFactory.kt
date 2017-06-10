package com.alexfacciorusso.flywaytoolbox

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBList
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import com.intellij.ui.treeStructure.Tree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.DefaultTreeModel


/**
 * @author alexfacciorusso
 */
class FlywayToolboxToolWindowFactory : ToolWindowFactory {
    private lateinit var toolWindow: ToolWindow
    private val rootNode = DefaultMutableTreeNode("Ciao")
    private val treeModel = DefaultTreeModel(rootNode)

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        this.toolWindow = toolWindow
        val contentFactory = ContentFactory.SERVICE.getInstance()

        val tree = createTree()
        val listComponent = JBList<String>()
        val content = contentFactory.createContent(JBSplitter().apply {
            firstComponent = tree
            secondComponent = panel(LCFlags.noGrid, LCFlags.fill, title = "Properties") {
                row {
                    listComponent(CCFlags.grow)
                }
            }
            proportion = 0.4f
        }, "", false)

        updateTree()

        toolWindow.contentManager.addContent(content)
    }

    override fun init(window: ToolWindow) {
        window.title = "Flyway"
    }

    private fun updateTree() {
        rootNode.userObject = "<<  >>"
        treeModel.nodeChanged(rootNode)
    }

    private fun createTree() = Tree().apply {
        model = treeModel
        cellRenderer = DefaultTreeCellRenderer().apply {
            icon = FlywayToolboxIcons.ICON
        }
    }
}