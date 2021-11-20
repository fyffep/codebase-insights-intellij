package intellij_extension.views;

import intellij_extension.Constants;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

public class HeatFileContainer extends FlowPane
{
    private String title;

    /**
     * Creates a FlowPane that holds HeatFileComponents or other elements.
     * @param title a label to help identify this container
     */
    public HeatFileContainer(String title) {
        super();
        this.title = title;

        this.setPrefWidth(FlowPane.USE_COMPUTED_SIZE);
        this.setPrefHeight(FlowPane.USE_COMPUTED_SIZE);

        //Add a tooltip to the file pane (TEMP)
        Tooltip tooltip = new Tooltip(title);
        tooltip.setFont(Constants.TOOLTIP_FONT);
        tooltip.setShowDelay(Duration.seconds(0));
        Tooltip.install(this, tooltip);
    }
}
