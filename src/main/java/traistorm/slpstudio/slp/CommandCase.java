package traistorm.slpstudio.slp;

public class CommandCase {
    public static String LesserDraw = "LesserDraw";
    public static String LesserSkip = "LesserSkip";
    public static String GreaterDraw = "GreaterDraw";
    public static String GreaterSkip = "GreaterSkip";
    public static String PlayerColorDraw = "PlayerColorDraw";
    public static String Fill = "Fill";
    public static String FillPlayerColor = "FillPlayerColor";
    public static String ShadowDraw = "ShadowDraw";
    public static String ExtendedCommands = "ExtendedCommands";
    public static String EOF = "EOF";

    public static String getCommandCase(byte[] array, int index)
    {
        String s1 = String.format("%8s", Integer.toBinaryString(array[index] & 0xFF)).replace(' ', '0');
        String command = s1.substring(4, 8);
        //System.out.println(s1);
        return switch (command) {
            case "0000", "1000", "0100", "1100" -> LesserDraw;
            case "0001", "1001", "0101", "1101" -> LesserSkip;
            case "0010" -> GreaterDraw;
            case "0011" -> GreaterSkip;
            case "0110" -> PlayerColorDraw;
            case "0111" -> Fill;
            case "1010" -> FillPlayerColor;
            case "1011" -> ShadowDraw;
            case "1110" -> ExtendedCommands;
            default -> EOF;
        };
    }
}
