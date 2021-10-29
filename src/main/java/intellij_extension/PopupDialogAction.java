package intellij_extension;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import javafx.application.Application;

public class PopupDialogAction extends AnAction {

    @Override
    public void update(AnActionEvent event) {
        // Set the availability based on whether a project is open
        Project aProject = event.getProject();
        event.getPresentation().setEnabledAndVisible(aProject != null);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        System.out.println("actionPerformed triggered!");
        /*// Using the event, create and show a dialog
        Project currentProject = event.getProject();
        StringBuilder dialogMessage = new StringBuilder(event.getPresentation().getText() + " Selected!");
        String dialogTitle = event.getPresentation().getDescription();
        // If an element is selected in the editor, add info about it.
        Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
        if(nav != null) {
            dialogMessage.append(String.format("\nSelected Element: %s", nav));
        }
        Messages.showMessageDialog(currentProject, dialogMessage.toString(), dialogTitle, Messages.getInformationIcon());*/


        Application.launch(ParentWindow.class);

    }
}