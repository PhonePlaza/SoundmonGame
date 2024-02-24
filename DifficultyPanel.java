import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class DifficultyPanel extends JPanel {

    JLayeredPane layer;
    JButton button;
    JButton button2;
    JLabel label;
    JLabel label2;
    private Image backgroundImage;

    public DifficultyPanel() {
        ImageIcon backBIcon = new ImageIcon("img/backButton.png");
        Image backB = backBIcon.getImage(); // ดึงรูปภาพออกมาจาก ImageIcon
        Image newbackB = backB.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // ปรับขนาดรูปภาพใหม่
        ImageIcon newbackBIcon = new ImageIcon(newbackB);
        JButton backButton = new JButton(newbackBIcon);
        Font customFont = FontManager.customDPanelFont(Font.BOLD, 100);
        // backgroundImage
        try {
            backgroundImage = ImageIO.read(new File("img/DifficultyBG2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        backButton.setBounds(20, 20, newbackBIcon.getIconWidth(), newbackBIcon.getIconHeight());
        backButton.setOpaque(false); // ปรับพื้นหลังทึบแสง หรือ โปร่งใส
        backButton.setBackground(null);

        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setBorder(null);

        ;
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playClickSound();
 
                Mainframe.switchToPanel1(Mainframe.panel1);
            }
        });
        add(backButton);

        setLayout(null);
        setBackground(Color.BLACK);

        layer = new JLayeredPane();
        add(layer);
        layer.setSize(1280, 720);

        label = new JLabel();
        label.setText("EASY");
        label.setForeground(Color.WHITE);
        label.setFont(customFont);
        label.setBounds(170, 175, 300, 300);
        layer.add(label, JLayeredPane.DEFAULT_LAYER);
        layer.setLayer(label, JLayeredPane.MODAL_LAYER);

        label2 = new JLabel();
        label2.setText("HARD");
        label2.setForeground(Color.WHITE);
        label2.setFont(customFont);
        label2.setBounds(840, 175, 300, 300);
        layer.add(label2, JLayeredPane.DEFAULT_LAYER);
        layer.setLayer(label2, JLayeredPane.MODAL_LAYER);

        button = new EzButton("EzButton");
        button.setBounds(120, 165, 350, 350);
        button.setFocusable(true);
        button.setBorderPainted(true);
        button.setContentAreaFilled(false);
        button.setBorder(new RoundedBorder(50));

        layer.add(button, JLayeredPane.PALETTE_LAYER);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playClickSound();
                Mainframe.switchToPanel(new EzMode());
            }
        });

        button2 = new HardButton("HardButton");
        button2.setBounds(795, 165, 350, 350);
        button2.setFocusable(true);
        button2.setBorderPainted(true);
        button2.setContentAreaFilled(false);
        button2.setBorder(new RoundedBorder(50));
        layer.add(button2, JLayeredPane.PALETTE_LAYER);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playClickSound();
                Mainframe.switchToPanel(new HardMode());
            }
        });

    }

    // backgroundImage -----------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (backgroundImage != null) {
            return new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this));
        }
        return super.getPreferredSize();
    }

    // ----------------------------------------------------

    class EzButton extends JButton {
        public EzButton(String text) {
            super(text);
            setForeground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            Point2D start = new Point2D.Float(300, 10);
            Point2D end = new Point2D.Float(getWidth(), getHeight());

            Color color1 = new Color(61, 227, 117);
            Color color2 = new Color(11, 67, 59);

            GradientPaint gradient = new GradientPaint(start, color1, end, color2);

            g2d.setPaint(gradient);

            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    class HardButton extends JButton {
        public HardButton(String text) {
            super(text);
            setForeground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            Point2D start = new Point2D.Float(300, 10);
            Point2D end = new Point2D.Float(getWidth(), getHeight());

            Color color1 = new Color(234, 101, 96);
            Color color2 = new Color(113, 55, 77);

            GradientPaint gradient = new GradientPaint(start, color1, end, color2);

            g2d.setPaint(gradient);

            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

}
