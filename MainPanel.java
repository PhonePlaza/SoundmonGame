import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainPanel extends JPanel {
    private boolean soundPlaying;

    public MainPanel() {
        soundPlaying = true;
        SoundPlayer.playBGMusic(soundPlaying);
        setLayout(new BorderLayout());

        ImageIcon image2 = new ImageIcon("img/NewLogo.png");
        JLabel label = new JLabel();
        label.setIcon(image2);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        // กำหนดขอบว่างรอบ JLabel เพื่อลดระยะห่างระหว่างภาพและปุ่ม
        label.setBorder(new EmptyBorder(50, 0, 0, 0));
        Font customFont_start = FontManager.customFontStart(Font.BOLD, 100);
        Font customFont_startResize = FontManager.customFontStart(Font.BOLD, 120);
        JButton button = new JButton("START");
        button.setFont(customFont_start);
        button.setForeground(new Color(255, 255, 255));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        int buttonWidth = 375;
        int buttonHeight = 200;
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

        // กำหนดขอบว่างรอบ JButton เพื่อลดระยะห่างระหว่างภาพและปุ่ม
        button.setBorder(new EmptyBorder(0, 0, 100, 0));

        button.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                SoundPlayer.playClickSound();
                soundPlaying = false;
                Mainframe.switchToPanel(new DifficultyPanel());
                button.setFont(customFont_start);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setFont(customFont_start);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setFont(customFont_startResize);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setFont(customFont_start);

            }

        });
        add(button, BorderLayout.SOUTH);

        add(label, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            // อ่านไฟล์รูปภาพ
            BufferedImage image = ImageIO.read(new File("img/MainBG.jpg"));
            // วาดรูปภาพลงบนพื้นหลังของ JPanel
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };
}