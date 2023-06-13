module traistorm.slpstudio {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencv;
    requires lz4;
                            
    opens traistorm.slpstudio to javafx.fxml;
    exports traistorm.slpstudio;
}