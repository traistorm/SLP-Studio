package traistorm.slpstudio;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import traistorm.slpstudio.slp.SlpVer4_2;
import traistorm.slpstudio.utils.ImageUtils;
import traistorm.slpstudio.utils.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class SlpStudioApplication extends Application {
    private static final int NUM_BUTTONS = 50;
    private Color colorSelected = Color.WHITE;
    @Override
    public void start(Stage stage) throws IOException {
        OpenCV.loadShared();

        SlpVer4_2 slpVer4_2 = new SlpVer4_2();
        List<Mat> frames = slpVer4_2.decodeSlp();

        /*FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();*/
        // Setup

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
        gridPane.add(imageView1, 0, 0);
        gridPane.add(imageView2, 0, 1);
        gridPane.add(imageView3, 0, 2);
        // Tạo ComboBox và đặt danh sách các mục
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(
                "Mục 1", "Mục 2", "Mục 3", "Mục 4"));

        scrollPane.setContent(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        GridPane gridPane1 = new GridPane();
        gridPane1.add(gridPane, 0, 0);
        gridPane1.add(text1, 1, 0);
        gridPane1.setHgap(10); // Khoảng cách giữa các button ngang
        gridPane1.setVgap(10); // Khoảng cách giữa các button dọc
        // Thêm ComboBox vào lưới tại hàng 0, cột 0
        gridPane1.add(comboBox, 2, 0);
        gridPane1.add(scrollPane, 0, 0);

        VBox vbox = new VBox(gridPane1);

        Scene scene = new Scene(vbox, ApplicationConstant.WINDOW_WIDTH, ApplicationConstant.WINDOW_HEIGHT);
        stage.setTitle("SLP STUDIO");
        stage.setScene(scene);
        stage.show();

    }
    private String generateRandomColor() {
        String[] colors = {"FF0000", "00FF00", "0000FF", "FFFF00", "00FFFF", "FF00FF"}; // Một số màu ngẫu nhiên
        int randomIndex = (int) (Math.random() * colors.length);
        return colors[randomIndex];
    }

    public static void main(String[] args) {
        launch();
    }
}