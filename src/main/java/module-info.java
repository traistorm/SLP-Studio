module traistorm.slpstudio {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencv;
    requires lz4;
    requires org.apache.commons.io;
                            
    opens traistorm.slpstudio to javafx.fxml;
    exports traistorm.slpstudio;
}