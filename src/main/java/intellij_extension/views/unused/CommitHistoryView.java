package intellij_extension.views.unused;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class CommitHistoryView extends Scene {

    private static CommitHistoryView instance;

    private CommitHistoryView(Parent root, double width, double height) {
        super(root, width, height);
    }

    public static CommitHistoryView createInstance(Parent root, double width, double height) {
        if(instance == null) {
            instance = new CommitHistoryView(root, width, height);
        } else {
            // TODO - convert to logger
            System.out.println("Instance already created. You should use getInstance() instead.");
        }
       return instance;
    }

    public static CommitHistoryView getInstance() {
        if (instance == null) {
            // TODO - convert to logger
            System.out.println("Please createInstance before trying to get view instance. Returning null! (Call createView())");
            return null;
        } else {
            return instance;
        }
    }
}
