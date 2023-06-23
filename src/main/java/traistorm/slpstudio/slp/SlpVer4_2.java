package traistorm.slpstudio.slp;

import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import traistorm.slpstudio.entities.BGRAColor;
import traistorm.slpstudio.utils.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
public class SlpVer4_2 extends Slp {
    private String version = "Version 4.2";
    private List<Mat> frames = new ArrayList<>();
    List<BGRAColor> unitPalette = new ArrayList<>();
    List<BGRAColor> playerColorPalette = new ArrayList<>();
    int playerColorIndex = 0;

    public SlpVer4_2() {
        loadUnitPalettes();
        loadPlayerPalettes(PALETTE_PLAYER_1_FILENAME);
    }

    public void decodeSlp(File file) {
        try {
            frames = extractFramesFromSLP(0, 0, file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    public void loadUnitPalettes() {
        try {
            // Load nature Palettes
            unitPalette = new ArrayList<>();
            File file = ResourceUtils.loadFileFromPathInResource("palettes/01_units.pal");
            Scanner scanner = new Scanner(file);
            int currentLine = 0;
            while (scanner.hasNextLine()) {
                currentLine += 1;
                String data = scanner.nextLine();
                String[] valueArray = data.split("\\s+");
                if (valueArray.length == 3) {
                    BGRAColor bgraColor = new BGRAColor();
                    bgraColor.takeValue(valueArray);
                    unitPalette.add(bgraColor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerPalettes(String filename) {
        try {
            // Load nature Palettes
            playerColorPalette = new ArrayList<>();
            File file = ResourceUtils.loadFileFromPathInResource("palettes/" + filename);
            Scanner scanner = new Scanner(file);
            int currentLine = 0;
            while (scanner.hasNextLine()) {
                currentLine += 1;
                String data = scanner.nextLine();
                String[] valueArray = data.split("\\s+");
                if (valueArray.length == 3) {
                    BGRAColor bgraColor = new BGRAColor();
                    bgraColor.takeValue(valueArray);
                    playerColorPalette.add(bgraColor);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Mat> extractFramesFromSLP(int frameIndex, int colorIndex, File slpFile)
    {
        try {
            int currentIndex = 0;
            byte[] bytes = Files.readAllBytes(slpFile.toPath());

            // 4 bytes đầu cho Version, 4 bytes tiếp theo là kích thước SLP format ở dạng chưa nén bằng Lz4, các bytes còn lại là của tệp Slp đã nén

            String version = new String(Arrays.copyOfRange(bytes, 0, 4), StandardCharsets.UTF_8);
            System.out.println(version);
            ByteBuffer bb = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 4, 8));
            bb.order( ByteOrder.LITTLE_ENDIAN);
            int length = bb.getInt();
            System.out.println(length);

            // Giải nén đoạn bytes đã nén bằng Lz4 này ra, ta được một file SLP ở dạng không nén, tức là file SLP nguyên vẹn như ở các version trước,
            // lại bắt đầu bằng header

            byte[] restored = new byte[length];
            byte[] compressed = Arrays.copyOfRange(bytes, 8, bytes.length);


            LZ4Factory factory = LZ4Factory.fastestInstance();

            LZ4FastDecompressor decompressor = factory.fastDecompressor();
            int compressedLength2 = decompressor.decompress(compressed, 0, restored, 0, length);

            bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, 4, 8));
            bb.order(ByteOrder.LITTLE_ENDIAN);
            int numberOfFrames = bb.getInt();
            System.out.println("Number of frame : " + numberOfFrames);
            /*bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, 48, 52));
            bb.order(ByteOrder.LITTLE_ENDIAN);
            widthCurrentFrame = bb.getInt();
            bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, 52, 56));
            bb.order(ByteOrder.LITTLE_ENDIAN);
            heightCurrentFrame = bb.getInt();
            System.out.println(widthCurrentFrame);
            System.out.println(heightCurrentFrame);
*/
            // File SLP sau giải nén : (Thứ tự lần lượt : ví dụ hết outline mới đến command offset)
            // Header : 32 bytes
            // Slp frame info : 32 bytes * số frame
            // Outline table offset : 4 bytes * height (slp_frame_row_edge)
            // Command offset : 4 bytes * height (Byte bắt đầu có index = 0) (slp_command_offset)
            //System.out.println(bytesToHex(Arrays.copyOfRange(restored, 66, 67)));
            //String s1 = String.format("%8s", Integer.toBinaryString(restored[68] & 0xFF)).replace(' ', '0');
            //System.out.println(height); // 10000001
            System.out.println(numberOfFrames);
            bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, 32, 36));
            bb.order(ByteOrder.LITTLE_ENDIAN);
            int frame1Offset = bb.getInt();
            bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, 64, 68));
            bb.order(ByteOrder.LITTLE_ENDIAN);
            int frame2Offset = bb.getInt();
            List<SlpFrameInfo> slpFrameInfos = new ArrayList<>();
            currentIndex = 32;
            for (int i = 0; i < numberOfFrames; i++) {
                SlpFrameInfo slpFrameInfo = new SlpFrameInfo();
                bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, currentIndex, currentIndex + 4));
                bb.order(ByteOrder.LITTLE_ENDIAN);
                slpFrameInfo.setCommandTableOffset(bb.getInt());
                currentIndex += 4;
                bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, currentIndex, currentIndex + 4));
                bb.order(ByteOrder.LITTLE_ENDIAN);
                slpFrameInfo.setOutlineTableOffset(bb.getInt());
                currentIndex += 4;
                bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, currentIndex, currentIndex + 4));
                bb.order(ByteOrder.LITTLE_ENDIAN);
                slpFrameInfo.setProperties(bb.getInt());
                currentIndex += 4;
                bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, currentIndex, currentIndex + 4));
                bb.order(ByteOrder.LITTLE_ENDIAN);
                slpFrameInfo.setProperties(bb.getInt());
                currentIndex += 4;
                bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, currentIndex, currentIndex + 4));
                bb.order(ByteOrder.LITTLE_ENDIAN);
                slpFrameInfo.setWidthImage(bb.getInt());
                currentIndex += 4;
                bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, currentIndex, currentIndex + 4));
                bb.order(ByteOrder.LITTLE_ENDIAN);
                slpFrameInfo.setHeightImage(bb.getInt());
                currentIndex += 4;
                bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, currentIndex, currentIndex + 4));
                bb.order(ByteOrder.LITTLE_ENDIAN);
                slpFrameInfo.setCentreSpriteX(bb.getInt());
                currentIndex += 4;
                bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, currentIndex, currentIndex + 4));
                bb.order(ByteOrder.LITTLE_ENDIAN);
                slpFrameInfo.setCentreSpriteY(bb.getInt());
                currentIndex += 4;
                slpFrameInfos.add(slpFrameInfo);
            }
            List<Mat> frames = new ArrayList<>();
            for (SlpFrameInfo slpFrameInfo : slpFrameInfos) {
                currentIndex = slpFrameInfo.getCommandTableOffset();
                int widthCurrentFrame = slpFrameInfo.getWidthImage();
                int heightCurrentFrame = slpFrameInfo.getHeightImage();

                List<List<SlpFrameRowEdge>> SLPFrameRowEdgeOfAllFrameList = new ArrayList<>();
                List<List<SlpCommandOffset>> SLPCommandOffsetOfAllFrameList = new ArrayList<>();

                List<SlpFrameRowEdge> SlpFrameRowEdgeOfAFrameList = new ArrayList<>();
                List<SlpCommandOffset> SlpCommandOffsetOfAFrameList = new ArrayList<>();
                int outlineOffset = slpFrameInfo.getOutlineTableOffset();
                for (int i = 0; i < heightCurrentFrame; i++) {
                    bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, outlineOffset, outlineOffset + 2));
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    int leftSpace = bb.getShort();
                    bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, outlineOffset + 2, outlineOffset + 4));
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    int rightSpace = bb.getShort();
                    SlpFrameRowEdge slpFrameRowEdge = new SlpFrameRowEdge();
                    slpFrameRowEdge.setLeftSpace(leftSpace);
                    slpFrameRowEdge.setRightSpace(rightSpace);
                    slpFrameInfo.getFrameRowEdges().add(slpFrameRowEdge);
                    //System.out.println(leftSpace + " " + rightSpace);
                    outlineOffset += 4;
                }
                //System.out.println("Next");
                int commandOffset = slpFrameInfo.getCommandTableOffset();
                SLPFrameRowEdgeOfAllFrameList.add(SlpFrameRowEdgeOfAFrameList);
                for (int i = 0; i < heightCurrentFrame; i++) {
                    bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, commandOffset, commandOffset + 4));
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    int offset = bb.getInt();
                    commandOffset += 4;
                    SlpCommandOffset slpCommandOffset = new SlpCommandOffset();
                    slpCommandOffset.setOffset(offset);
                    slpFrameInfo.getCommandOffsets().add(slpCommandOffset);
                    //System.out.println(offset);
                }
                SLPCommandOffsetOfAllFrameList.add(SlpCommandOffsetOfAFrameList);
                //System.out.println(SLPCommandOffsetOfAllFrameList.get(0).get(0).getOffset());
                //String s1 = String.format("%8s", Integer.toBinaryString(restored[SLPCommandOffsetOfAllFrameList.get(0).get(0).getOffset()] & 0xFF)).replace(' ', '0');
                //System.out.println(s1); // 10000001

                String commandCaseValue = SlpCommandCase.LesserDraw;
                Mat frame = new Mat(heightCurrentFrame, widthCurrentFrame, CvType.CV_8UC4);

                for (int i = 0; i < widthCurrentFrame; i++) {
                    for (int j = 0; j < heightCurrentFrame; j++)
                    {
                        frame.put(j, i, 255, 255, 255, 0);
                    }
                }

                int numberEOF = 0;
                int currentLine = 0;
                int currentRow = 0;
                int currentCol = 0;

                try (FileOutputStream fos = new FileOutputStream("E:\\Hust Ondrive\\OneDrive - Hanoi University of Science and Technology\\Documents\\SLP file\\output.txt")) {
                    fos.write(restored);
                    //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
                }

                currentIndex = slpFrameInfo.getCommandOffsets().get(currentRow).getOffset();
                while (currentRow < heightCurrentFrame) {
                    boolean addLeftSpace = false;
                    //System.out.println("test");
                    while (currentCol <= widthCurrentFrame) {
                        if (!addLeftSpace) {
                            int leftSpace = slpFrameInfo.getFrameRowEdges().get(currentRow).getLeftSpace();
                            int rightSpace = slpFrameInfo.getFrameRowEdges().get(currentRow).getRightSpace();
                            for (int i = 0; i < leftSpace; i++)
                            {
                                frame.put(currentRow, currentCol, 255, 255, 255, 0);
                                currentCol ++;
                            }
                            //System.out.println(currentCol);
                            addLeftSpace = true;
                        }


                        //System.out.println(SLPCommandOffsetOfAllFrameList.get(0).get(currentRow + 1).getOffset());
                        commandCaseValue = SlpCommandCase.getCommandCase(restored, currentIndex);
                        //System.out.println("Command :" + commandCaseValue);
                        //System.out.println("Row :" + currentRow);
                        if (commandCaseValue == SlpCommandCase.LesserDraw) {
                            //System.out.println(currentIndex);
                            int lengthValue = (restored[currentIndex] & 0xff) >> 2;
                            String s1 = String.format("%8s", Integer.toBinaryString(restored[currentIndex] & 0xFF)).replace(' ', '0');
                            for (int i = 0; i < lengthValue; i++)
                            {
                                currentIndex += 1;
                                int paletteIndex = restored[currentIndex] & 0xff;
                                BGRAColor bgraColor = unitPalette.get(paletteIndex);
                                frame.put(currentRow, currentCol, bgraColor.getBlue(), bgraColor.getGreen(), bgraColor.getRed(), 255);
                                currentCol ++;
                            }
                            currentIndex += 1;
                            // System.out.println("Current col : " + currentCol);
                        }
                        else if (commandCaseValue == SlpCommandCase.LesserSkip) {
                            int lengthValue = (restored[currentIndex] & 0xff) >> 2;
                            for (int i = 0; i < lengthValue; i++)
                            {
                                frame.put(currentRow, currentCol, 255, 255, 255, 0);
                                currentCol ++;
                            }
                            currentIndex += 1;
                        }
                        else if (commandCaseValue == SlpCommandCase.GreaterDraw) {
                            int lengthValue = (restored[currentIndex] & 0xf0) << 4 + (restored[currentIndex + 1] & 0xff);
                            String s1 = String.format("%8s", Integer.toBinaryString(restored[currentIndex] & 0xFF)).replace(' ', '0');
                        /*for (int i = 0; i < lengthValue; i++)
                        {
                            BGRAColor bgraColor = unit_palette.get(paletteIndex);
                            frame.put(currentRow, currentCol, bgraColor.getBlue(), bgraColor.getGreen(), bgraColor.getRed());
                            currentCol ++;
                        }*/
                            currentIndex += (lengthValue + 1);
                        }
                        else if (commandCaseValue == SlpCommandCase.GreaterSkip) {
                            int lengthValue = (restored[currentIndex] & 0xf0) << 4 + (restored[currentIndex + 1] & 0xff);
                        /*for (int i = 0; i < lengthValue; i++)
                        {
                            frame.put(currentRow, currentCol, 255, 255, 255);
                            currentCol ++;
                        }*/
                            currentIndex += 1;
                        }
                        else if (commandCaseValue == SlpCommandCase.Fill) {
                            int lengthValue = (restored[currentIndex] & 0xff) >> 4;
                            if (lengthValue == 0) {
                                currentIndex += 1;
                                lengthValue = restored[currentIndex] & 0xff;
                            }
                            //currentIndex += 1;

                            currentIndex += 1;
                            int paletteIndex = restored[currentIndex] & 0xff;
                            for (int i = 0; i < lengthValue; i++) {
                                BGRAColor bgraColor = unitPalette.get(paletteIndex);
                                frame.put(currentRow, currentCol, bgraColor.getBlue(), bgraColor.getGreen(), bgraColor.getRed(), 255);
                                currentCol ++;
                            }
                            currentIndex += 1;
                        }
                        else if (commandCaseValue == SlpCommandCase.FillPlayerColor) {
                            int lengthValue = (restored[currentIndex] & 0xff) >> 4;
                            if (lengthValue == 0) {
                                currentIndex += 1;
                                lengthValue = restored[currentIndex] & 0xff;
                            }

                            currentIndex += 1;
                            int paletteIndex = restored[currentIndex] & 0xff;
                            for (int i = 0; i < lengthValue; i++) {
                                BGRAColor bgraColor = playerColorPalette.get(paletteIndex);
                                frame.put(currentRow, currentCol, bgraColor.getBlue(), bgraColor.getGreen(), bgraColor.getRed(), 255);
                                currentCol ++;
                            }
                            currentIndex += 1;
                        }
                        else if (commandCaseValue == SlpCommandCase.PlayerColorDraw) {
                            int lengthValue = (restored[currentIndex] & 0xff) >> 4;
                            if (lengthValue == 0) {
                                currentIndex += 1;
                                lengthValue = restored[currentIndex] & 0xff;
                            }

                            for (int i = 0; i < lengthValue; i++) {
                                currentIndex += 1;
                                int paletteIndex = restored[currentIndex] & 0xff;
                                BGRAColor bgraColor = playerColorPalette.get(paletteIndex);
                                frame.put(currentRow, currentCol, bgraColor.getBlue(), bgraColor.getGreen(), bgraColor.getRed(), 255);
                                currentCol ++;
                            }
                            currentIndex += 1;
                        }
                        else if (commandCaseValue == SlpCommandCase.ShadowDraw) {
                            int lengthValue = (restored[currentIndex] & 0xff) >> 4;
                            if (lengthValue == 0) {
                                currentIndex += 1;
                                lengthValue = restored[currentIndex] & 0xff;
                            }

                        /*for (int i = 0; i < lengthValue; i++)
                        {
                            BGRAColor bgraColor = unit_palette.get(paletteIndex);
                            frame.put(currentRow, currentCol, bgraColor.getBlue(), bgraColor.getGreen(), bgraColor.getRed());
                            currentCol ++;
                        }*/
                            currentIndex += 1;
                        }
                        else if (commandCaseValue == SlpCommandCase.EOF) {
                            currentIndex += 1;
                            numberEOF ++;
                            currentRow ++;
                            currentCol = 0;
                            break;
                        }
                        else {
                            currentIndex += 1;
                        }
                    }
                    //System.out.println(numberEOF);
                }
                frames.add(frame);
            }
            bb = ByteBuffer.wrap(Arrays.copyOfRange(restored, currentIndex + 4, currentIndex + 8));
            bb.order(ByteOrder.LITTLE_ENDIAN);
            Integer test = bb.getInt();

            return frames;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getVersion() {
        return version;
    }

    public List<Mat> getFrames() {
        return frames;
    }

    public void setFrames(List<Mat> frames) {
        this.frames = frames;
    }
}
