package traistorm.slpstudio.utils;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;

public class ImageUtils {
    public static Image openCvMatToJavaFxImage(Mat mat) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", mat, matOfByte);
        return new Image(new ByteArrayInputStream(matOfByte.toArray()));
    }
}
