import javax.swing.*;
import java.awt.*;

public class RoundedBoarderStart extends JButton {

    public RoundedBoarderStart(String text) {
        super(text);
      
        // To remove the border when focused
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50); 
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50); 
    }

    // For testing
    
}