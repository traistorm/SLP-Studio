package traistorm.slpstudio.utils;

public class BGRAColor {
    Integer blue;
    Integer green;
    Integer red;
    Integer alpha;
    public void takeValue(String[] value)
    {
        red = Integer.parseInt(value[0]);
        green = Integer.parseInt(value[1]);
        blue = Integer.parseInt(value[2]);

    }

    public Integer getBlue() {
        return blue;
    }

    public void setBlue(Integer blue) {
        this.blue = blue;
    }

    public Integer getGreen() {
        return green;
    }

    public void setGreen(Integer green) {
        this.green = green;
    }

    public Integer getRed() {
        return red;
    }

    public void setRed(Integer red) {
        this.red = red;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }
}
