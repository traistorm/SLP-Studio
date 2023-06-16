package traistorm.slpstudio.slp;

import org.opencv.core.Mat;

import java.util.List;

public class Slp {
    private String version = "Version 1.0";

    // DEFINE FILENAME PALETTES
    public static String PALETTE_NATURE_FILENAME = "02_nature.pal";
    public static String PALETTE_PLAYER_1_FILENAME = "playercolor_blue.pal";
    public Slp() {
    }
    public List<Mat> decodeSlp() {
        return null;
    }
    public String getVersion() {
        return version;
    }
    public void loadPalettes(String filename) {

    }
}
