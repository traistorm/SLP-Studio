package traistorm.slpstudio;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import traistorm.slpstudio.constant.ApplicationConstant;
import traistorm.slpstudio.slp.Slp;
import traistorm.slpstudio.slp.SlpVer3;
import traistorm.slpstudio.slp.SlpVer4_2;
import traistorm.slpstudio.utils.ImageUtils;
import traistorm.slpstudio.utils.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SlpStudioApplication extends Application {
    private static final int NUM_BUTTONS = 50;
    private String paletteDefault = "Nature";
    private String slpVersionDefault = "Version 4.2P";
    private Color colorSelected = Color.WHITE;
    private Image icon;

    Slp slp = new Slp();
    File slpFileSelected = null;
    public SlpStudioApplication() throws IOException {
        icon = new Image(ResourceUtils.loadFileFromPathInResource("icon/icon1.png").toURI().toString());
    }
    @Override
    public void start(Stage stage) throws IOException {
        OpenCV.loadShared();

        slp = new SlpVer4_2();
        //slp.decodeSlp();
        //List<Mat> frames = slp.getFrames();

        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");
        //Creating menu Items
        MenuItem loadSlpFileItem = new MenuItem("Load Slp File");
        MenuItem exportImageItem = new MenuItem("Export to image");
        MenuItem closeAppItem = new MenuItem("Close");

        MenuItem aboutItem = new MenuItem("About");

        //Adding all the menu items to the menu
        fileMenu.getItems().addAll(loadSlpFileItem, exportImageItem, closeAppItem);
        helpMenu.getItems().addAll(aboutItem);
        //Creating a menu bar and adding menu to it.
        MenuBar menuBar = new MenuBar(fileMenu, helpMenu);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(0); // Khoảng cách giữa các button ngang
        gridPane.setVgap(0); // Khoảng cách giữa các button dọc

        /*Image imageTest1 = ImageUtils.openCvMatToJavaFxImage(frames.get(0));
        Image imageTest2 = ImageUtils.openCvMatToJavaFxImage(frames.get(1));
        Image imageTest3 = ImageUtils.openCvMatToJavaFxImage(frames.get(2));

        //Creating the image view
        ImageView imageView1 = new ImageView(imageTest1);
        imageView1.setFitWidth(500);
        imageView1.setPreserveRatio(true);
        ImageView imageView2 = new ImageView(imageTest2);
        imageView2.setFitWidth(500);
        imageView2.setPreserveRatio(true);
        ImageView imageView3 = new ImageView(imageTest3);
        imageView3.setFitWidth(500);
        imageView3.setPreserveRatio(true);
        Text text1 = new Text("Text 2");
        text1.setUnderline(true);

        VBox vBox = new VBox();
        vBox.getChildren().add(imageView1);
        vBox.getChildren().add(imageView2);
        vBox.getChildren().add(imageView3);
        vBox.setPrefSize(400, 400);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(ApplicationConstant.SHOW_FRAME_WIDTH, ApplicationConstant.PROGRAM_HEIGHT);
        scrollPane.setContent(vBox);
        scrollPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");*/

        // Khai báo các button, combobox
        ComboBox<String> selectPalettesCB = new ComboBox<>();
        selectPalettesCB.setPrefWidth(ApplicationConstant.COMBOBOX_WIDTH);
        selectPalettesCB.setValue(paletteDefault);
        selectPalettesCB.setOnAction((e) -> handleSelectPalettes(selectPalettesCB.getSelectionModel().getSelectedItem()));
        selectPalettesCB.setItems(FXCollections.observableArrayList("Nature", "Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8"));

        ComboBox<String> selectSlpVersionCB = new ComboBox<>();
        selectSlpVersionCB.setPrefWidth(ApplicationConstant.COMBOBOX_WIDTH);
        selectSlpVersionCB.setValue(slpVersionDefault);
        selectSlpVersionCB.setOnAction((e) -> handleSelectSlpVersion(selectSlpVersionCB.getSelectionModel().getSelectedItem()));
        selectSlpVersionCB.setItems(FXCollections.observableArrayList("Version 3", "Version 4", "Version 4.2P"));

        Button decodeSlpButton = new Button("Decode Slp");
        decodeSlpButton.setPrefWidth(ApplicationConstant.COMBOBOX_WIDTH);
        decodeSlpButton.setOnAction((e) -> decodeSlp());

        GridPane buttonGridPane = new GridPane();
        buttonGridPane.add(selectPalettesCB, 0, 0);
        buttonGridPane.add(selectSlpVersionCB, 1, 0);
        buttonGridPane.add(decodeSlpButton, 0, 1);
        Text fileChosenInfo = new Text("File chosenaaaaaaaaaaaaa");
        Label textLabelFileChosen = new Label("This is a very long text that will be truncated with ellipsis when it exceeds the width of the label.");
        textLabelFileChosen.setMaxWidth(200);
        textLabelFileChosen.setFont(Font.font(12));
        textLabelFileChosen.setTextOverrun(OverrunStyle.WORD_ELLIPSIS);
        GridPane.setColumnSpan(textLabelFileChosen, 2);
        buttonGridPane.add(textLabelFileChosen, 0, 2);

        buttonGridPane.setHgap(5);
        buttonGridPane.setVgap(5);

        GridPane gridPane1 = new GridPane();
        gridPane1.setPadding(new Insets(0, 5, 0, 5));
        gridPane1.add(buttonGridPane, 0, 0);
        //gridPane1.add(scrollPane, 1, 0);
        //gridPane1.add(hBox, 1, 0);
        gridPane1.setHgap(10); // Khoảng cách giữa các button ngang
        gridPane1.setVgap(0); // Khoảng cách giữa các button dọc
        // Thêm ComboBox vào lưới tại hàng 0, cột 0
        //gridPane1.add(scrollPane, 0, 0);

        ProgressBar progressBar = new ProgressBar(); // Set giá trị mặc định là 50%
        progressBar.setPrefWidth(200);
        // Giả lập quá trình giải mã bức ảnh
        Task<Void> decodeTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Thực hiện quá trình giải mã ở đây
                int total = 100; // Số lượng công việc cần thực hiện
                for (int i = 0; i <= total; i++) {
                    //updateProgress(i, total);
                    Thread.sleep(50); // Giả lập thời gian giải mã
                }
                return null;
            }
        };

        // Liên kết ProgressBar với Task
        progressBar.progressProperty().bind(decodeTask.progressProperty());

        // Khi quá trình giải mã hoàn thành, ẩn ProgressBar
        decodeTask.setOnSucceeded(event -> progressBar.setVisible(false));

        // Khởi chạy Task
        Thread thread = new Thread(decodeTask);
        thread.start();

        GridPane gridPane2 = new GridPane(); // GridPane chứa Status bar
        gridPane2.add(progressBar, 0, 0);
        VBox vbox = new VBox(menuBar, gridPane1, gridPane2);
        vbox.setSpacing(5);
        //vbox.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(vbox, ApplicationConstant.WINDOW_WIDTH, ApplicationConstant.WINDOW_HEIGHT);
        stage.setTitle("SLP STUDIO");
        ClassLoader classLoader = SlpStudioApplication.class.getClassLoader();
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
        // Định nghĩa hành động cho menu item "Add Files"
        loadSlpFileItem.setOnAction((ActionEvent event) -> handleChooseSlpFile(stage, textLabelFileChosen));

    }
    private String generateRandomColor() {
        String[] colors = {"FF0000", "00FF00", "0000FF", "FFFF00", "00FFFF", "FF00FF"}; // Một số màu ngẫu nhiên
        int randomIndex = (int) (Math.random() * colors.length);
        return colors[randomIndex];
    }
    private void handleSelectPalettes(String selectedItem) {
        switch (selectedItem) {
            case "Nature" -> {
                slp.loadPalettes(Slp.PALETTE_NATURE_FILENAME);
            }
            case "Player 1" -> {
                slp.loadPalettes(Slp.PALETTE_PLAYER_1_FILENAME);
            }
            default -> {

            }
        }
    }
    private void handleSelectSlpVersion(String selectedItem) {
        switch (selectedItem) {
            case "Version 3" -> {
                slp = new SlpVer3();
            }
            case "Version 4.2P" -> {
                slp = new SlpVer4_2();
            }
            default -> {
            }
        }
    }
    private void handleChooseSlpFile(Stage stage, Label textFileChosen) {
        // Tạo FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files");

        // Hiển thị cửa sổ chọn file

        // Lấy danh sách các file được chọn
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Xử lý danh sách các file đã chọn
            slpFileSelected = selectedFile;
            textFileChosen.setText(selectedFile.getName());
        }
    }
    private void decodeSlp() {
        if (slpFileSelected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            // Header Text: null
            alert.setHeaderText(null);
            alert.setContentText("Please select a Slp file!");

            alert.showAndWait();
            return;
        }
        try {
            slp.decodeSlp(slpFileSelected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}