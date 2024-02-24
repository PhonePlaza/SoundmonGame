import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontManager{
    public static Font customFontStart(int style,int size){
        try {
            // Load the font file
            File fontFile = new File("font/ProtestRiot.ttf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            return customFont.deriveFont(style, size);

            // Derive a font with desired style and size
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Font customDPanelFont(int style,int size){
        try {
            // Load the font file
            File fontFile = new File("font/FC Krung Thep Bold.ttf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            return customFont.deriveFont(style, size);

            // Derive a font with desired style and size
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}