package traistorm.slpstudio.slp;

import org.opencv.core.Mat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Slp {
    private String version = "Version 1.0";
    private List<Mat> frames = new ArrayList<>();

    // DEFINE FILENAME PALETTES
    public static String PALETTE_NATURE_FILENAME = "02_nature.pal";
    public static String PALETTE_PLAYER_1_FILENAME = "playercolor_blue.pal";
    public static String PALETTE_PLAYER_2_FILENAME = "playercolor_red.pal";
    public Slp() {
    }
    public void decodeSlp(File file) {
    }
    public String getVersion() {
        return version;
    }
    public void loadPlayerPalettes(String filename) {

    }

    public List<Mat> getFrames() {
        return frames;
    }

    public void setFrames(List<Mat> frames) {
        this.frames = frames;
    }
}
