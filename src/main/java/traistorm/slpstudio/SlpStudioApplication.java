package traistorm.slpstudio;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import traistorm.slpstudio.slp.Slp;
import traistorm.slpstudio.slp.SlpVer3;
import traistorm.slpstudio.slp.SlpVer4_2;
import traistorm.slpstudio.utils.ImageUtils;
import traistorm.slpstudio.utils.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class SlpStudioApplication extends Application {
    private static final int NUM_BUTTONS = 50;
    private String paletteDefault = "Nature";
    private String slpVersionDefault = "Version 4.2P";
    private Color colorSelected = Color.WHITE;
    private Image icon;

    Slp slp = new Slp();
    public SlpStudioApplication() throws IOException {
        icon = new Image(ResourceUtils.loadFileFromPathInResource("icon/icon1.png").toURI().toString());
    }
    @Override
    public void start(Stage stage) throws IOException {
        OpenCV.loadShared();

        slp = new SlpVer4_2();
        List<Mat> frames = slp.decodeSlp();

        /*FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();*/
        // Setup

        //Creating a menu
        Menu fileMenu = new Menu("File");
        //Creating menu Items
        MenuItem item1 = new MenuItem("Add Files");
        MenuItem item2 = new MenuItem("Start Converting");
        MenuItem item3 = new MenuItem("Stop Converting");
        MenuItem item4 = new MenuItem("Remove File");
        MenuItem item5 = new MenuItem("Exit");
        //Adding all the menu items to the menu
        fileMenu.getItems().addAll(item1, item2, item3, item4, item5);
        //Creating a menu bar and adding menu to it.
        MenuBar menuBar = new MenuBar(fileMenu);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(0); // Khoảng cách giữa các button ngang
        gridPane.setVgap(0); // Khoảng cách giữa các button dọc

        // Tạo ColorPicker
        ColorPicker colorPickerBtn = new ColorPicker();
        colorPickerBtn.setOnAction(event -> colorSelected = colorPickerBtn.getValue());
        //gridPane.add(colorPickerBtn, 0, 0);

        // Tải ảnh từ tệp
/*        String imagePath = "sky.jpg";
        Image image = new Image(new FileInputStream(ResourceUtils.loadFileFromResource(imagePath)));

        // Lấy chiều rộng và chiều cao của ảnh
        int imageWidth = (int) image.getWidth();
        int imageHeight = (int) image.getHeight();*/

        // Duyệt qua từng pixel trong ảnh
        /*for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                // Lấy màu tương ứng từ pixel
                Color color = image.getPixelReader().getColor(x, y);

                // Tạo một ô hiển thị màu tương ứng trên GridPane
                javafx.scene.shape.Rectangle rectangle = new javafx.scene.shape.Rectangle(1, 1);
                rectangle.setFill(color);

                // Đặt ô vào vị trí tương ứng trên GridPane
                gridPane.add(rectangle, x, y + 1);
            }
        }*/
        ScrollPane scrollPane = new ScrollPane();
        Image imageTest1 = ImageUtils.openCvMatToJavaFxImage(frames.get(0));
        Image imageTest2 = ImageUtils.openCvMatToJavaFxImage(frames.get(1));
        Image imageTest3 = ImageUtils.openCvMatToJavaFxImage(frames.get(2));

        //Creating the image view
        ImageView imageView1 = new ImageView(imageTest1);
        imageView1.setFitWidth(500);
        imageView1.setPreserveRatio(true);
        ImageView imageView2 = new ImageView(imageTest2);
        ImageView imageView3 = new ImageView(imageTest3);
        Text text1 = new Text("Text 2");
        text1.setUnderline(true);
        //Setting image to the image view
        /*imageView.setImage(imageTest);
        //Setting the image view parameters
        imageView.setX(10);
        imageView.setY(10);
        imageView.setFitWidth(575);
        imageView.setPreserveRatio(true);*/
        //Setting the Scene object
        /*imageView1.setFitWidth(575);
        imageView2.setFitWidth(575);
        imageView3.setFitWidth(575);*/
        /*Group root = new Group();
        root.getChildren().addAll(imageView1, text1, imageView2, imageView3);
        scrollPane.setContent(root);*/
        Group group = new Group();
        group.getChildren().add(imageView1);
        group.getChildren().add(imageView2);
        group.getChildren().add(imageView3);
        ScrollPane scrollPane1 = new ScrollPane();
        scrollPane1.setPrefSize(400, 400);
        scrollPane1.setContent(group);
        gridPane.add(scrollPane1, 0, 0);
        // Tạo ComboBox và đặt danh sách các mục
        ComboBox<String> selectPalettesCB = new ComboBox<>();
        selectPalettesCB.setValue(paletteDefault);
        selectPalettesCB.setOnAction((e) -> handleSelectPalettes(selectPalettesCB.getSelectionModel().getSelectedItem()));
        selectPalettesCB.setItems(FXCollections.observableArrayList("Nature", "Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8"));

        ComboBox<String> selectSlpVersionCB = new ComboBox<>();
        selectSlpVersionCB.setValue(slpVersionDefault);
        selectSlpVersionCB.setOnAction((e) -> handleSelectSlpVersion(selectSlpVersionCB.getSelectionModel().getSelectedItem()));
        selectSlpVersionCB.setItems(FXCollections.observableArrayList("Version 3", "Version 4", "Version 4.2P"));

        HBox hBox = new HBox();
        hBox.getChildren().add(selectPalettesCB);
        hBox.getChildren().add(selectSlpVersionCB);
        hBox.setSpacing(10);

        scrollPane.setContent(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        GridPane gridPane1 = new GridPane();
        gridPane1.add(gridPane, 0, 0);
        gridPane1.add(hBox, 1, 0);
        gridPane1.setHgap(10); // Khoảng cách giữa các button ngang
        gridPane1.setVgap(10); // Khoảng cách giữa các button dọc
        // Thêm ComboBox vào lưới tại hàng 0, cột 0
        gridPane1.add(scrollPane, 0, 0);

        VBox vbox = new VBox(menuBar, gridPane1);
        Scene scene = new Scene(vbox, ApplicationConstant.WINDOW_WIDTH, ApplicationConstant.WINDOW_HEIGHT);
        stage.setTitle("SLP STUDIO");
        ClassLoader classLoader = SlpStudioApplication.class.getClassLoader();
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();

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

    public static void main(String[] args) {
        launch();
    }
}