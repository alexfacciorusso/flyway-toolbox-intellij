package com.alexfacciorusso.flywaytoolbox

import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataKeys.MODULE
import com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.ui.components.dialog
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.CCFlags.growX
import com.intellij.ui.layout.panel
import com.intellij.util.PlatformIcons
import org.jdesktop.swingx.combobox.ListComboBoxModel
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JTextField


/**
 * @author alexfacciorusso
 */
class NewFlywayMigrationFileAction : com.intellij.openapi.actionSystem.AnAction() {
    val datestampMigrationName = "datestamp"
    val versionedMigrationName = "versioned"
    val repeatableMigrationName = "repeatable"

    val migrationTypes = listOf(
            MigrationType(datestampMigrationName, "Datestamp"),
            MigrationType(versionedMigrationName, "Versioned"),
            MigrationType(repeatableMigrationName, "Repeatable")
    )

    override fun actionPerformed(event: com.intellij.openapi.actionSystem.AnActionEvent) {
        val project = event.project ?: return
        val module = event.getData(MODULE) ?: return
        val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val directory = com.intellij.psi.PsiManager.getInstance(module.project).findDirectory(virtualFile) ?: return

        logger(NewFlywayMigrationFileAction::class.java.name).debug(directory.toString())

        createDialogAndShow(project, directory)
    }

    fun createDialogAndShow(project: Project, directory: PsiDirectory) {
        val typeSpinner = JComboBox(ListComboBoxModel<MigrationType>(migrationTypes))
        val descriptionField = JTextField(25)
        val updownImageIcon = JLabel(PlatformIcons.UP_DOWN_ARROWS).apply {
            toolTipText = "You can use up and down arrows to choose the migration type."
        }

        typeSpinner.registerUpDownListener(descriptionField)

        val content = panel {
            row("Description:") {
                descriptionField(CCFlags.pushX, growX)
                updownImageIcon()
            }
            row("Type:") {
                typeSpinner(CCFlags.pushX, growX)
            }
        }
        val dialog = dialog("New Flyway migration", content, project = project, resizable = true, ok = {
            createMigrationFile(project, typeSpinner.selectedItem as MigrationType, descriptionField.text, directory)
        })
        dialog.setSize(700, dialog.size.height)
        dialog.show()
    }

    private fun createMigrationFile(project: com.intellij.openapi.project.Project, migrationType: MigrationType,
                                    description: String, psiDirectory: PsiDirectory) {
        val lowercaseSnakeDescr = description.toLowerCase().replace("\\s+".toRegex(), "_")
        val filename = when (migrationType.name) {
            datestampMigrationName -> {
                val timestamp = java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(java.util.Date())
                "V${timestamp}__$lowercaseSnakeDescr.sql"
            }
            versionedMigrationName -> {
                "V1__$lowercaseSnakeDescr.sql"
            }
            repeatableMigrationName -> {
                "R__$lowercaseSnakeDescr.sql"
            }
            else -> return
        }

        runWriteCommandAction(project) { psiDirectory.createFile(filename) }
    }
}

private fun <E> JComboBox<E>.registerUpDownListener(textField: JTextField) {
    textField.addKeyListener(object : KeyListener {
        override fun keyPressed(e: KeyEvent) {
            val itemCount = itemCount

            when (e.keyCode) {
                KeyEvent.VK_UP -> {
                    val nextSelected = selectedIndex + 1
                    selectedIndex = if (nextSelected < itemCount) nextSelected else 0
                    e.consume()
                }
                KeyEvent.VK_DOWN -> {
                    val nextSelected = selectedIndex - 1
                    selectedIndex = if (nextSelected >= 0) nextSelected else itemCount - 1
                    e.consume()
                }
            }
        }

        override fun keyTyped(e: KeyEvent?) {}
        override fun keyReleased(e: KeyEvent?) {}
    })
}

data class MigrationType(
        val name: String,
        val title: String
) {
    override fun toString(): String = title
}