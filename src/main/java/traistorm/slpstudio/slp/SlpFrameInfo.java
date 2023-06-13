package traistorm.slpstudio.slp;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class SlpFrameInfo {
    private String slpVer; // Phiên bản SLP
    private Mat mat; // Ma trận Opencv chứa data
    private int commandTableOffset;
    private List<SlpCommandOffset> commandOffsets = new ArrayList<>();
    private int outlineTableOffset;
    private List<SlpFrameRowEdge> frameRowEdges = new ArrayList<>();
    private int paletteOffset;
    private int properties;
    private int widthImage;
    private int heightImage;
    private int centreSpriteX;
    private int centreSpriteY;

    public String getSlpVer() {
        return slpVer;
    }

    public void setSlpVer(String slpVer) {
        this.slpVer = slpVer;
    }

    public Mat getMat() {
        return mat;
    }

    public void setMat(Mat mat) {
        this.mat = mat;
    }

    public int getCommandTableOffset() {
        return commandTableOffset;
    }

    public void setCommandTableOffset(int commandTableOffset) {
        this.commandTableOffset = commandTableOffset;
    }

    public int getOutlineTableOffset() {
        return outlineTableOffset;
    }

    public void setOutlineTableOffset(int outlineTableOffset) {
        this.outlineTableOffset = outlineTableOffset;
    }

    public int getPaletteOffset() {
        return paletteOffset;
    }

    public void setPaletteOffset(int paletteOffset) {
        this.paletteOffset = paletteOffset;
    }

    public int getProperties() {
        return properties;
    }

    public void setProperties(int properties) {
        this.properties = properties;
    }

    public int getWidthImage() {
        return widthImage;
    }

    public void setWidthImage(int widthImage) {
        this.widthImage = widthImage;
    }

    public int getHeightImage() {
        return heightImage;
    }

    public void setHeightImage(int heightImage) {
        this.heightImage = heightImage;
    }

    public int getCentreSpriteX() {
        return centreSpriteX;
    }

    public void setCentreSpriteX(int centreSpriteX) {
        this.centreSpriteX = centreSpriteX;
    }

    public int getCentreSpriteY() {
        return centreSpriteY;
    }

    public void setCentreSpriteY(int centreSpriteY) {
        this.centreSpriteY = centreSpriteY;
    }

    public List<SlpCommandOffset> getCommandOffsets() {
        return commandOffsets;
    }

    public void setCommandOffsets(List<SlpCommandOffset> commandOffsets) {
        this.commandOffsets = commandOffsets;
    }

    public List<SlpFrameRowEdge> getFrameRowEdges() {
        return frameRowEdges;
    }

    public void setFrameRowEdges(List<SlpFrameRowEdge> frameRowEdges) {
        this.frameRowEdges = frameRowEdges;
    }

}
