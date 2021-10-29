package intellij_extension;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
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
        /*// Using the event, create and show a dialog
        Project currentProject = event.getProject();
        StringBuffer dialogMessage = new StringBuffer(event.getPresentation().getText() + " Selected!");
        String dialogTitle = event.getPresentation().getDescription();
        // If an element is selected in the editor, add info about it.
        Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
        if(nav != null) {
            dialogMessage.append(String.format("\nSelected Element: %s", nav.toString()));
        }
        Messages.showMessageDialog(currentProject, dialogMessage.toString(), dialogTitle, Messages.getInformationIcon());*/

        //Start the JavaFX window
        Application.launch(RootWindow.class);
    }
}