import javax.swing.*;
import java.awt.*;

public class Mainframe {
    public static JFrame mainFrame;
    public static MainPanel panel1 = new MainPanel();

    public static void createAndShowGUI() {
        ImageIcon logo = new ImageIcon("img/Logo.png");

        mainFrame = new JFrame();

        mainFrame.setTitle("Soundmon Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null); // ตั้งให้ตำแหน่ง frame อยู่ตรงกลาง screen
        mainFrame.setIconImage(logo.getImage());
        mainFrame.add(panel1, BorderLayout.CENTER);
        mainFrame.getContentPane().add(panel1);
        // Display the main frame
        mainFrame.setVisible(true);
    }

    public static void switchToPanel(JPanel panel) {
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(panel, BorderLayout.CENTER);
        mainFrame.revalidate(); // ตรวจสอบ
        mainFrame.repaint();
    }

    public static void switchToPanel1(JPanel panel) {
        switchToPanel(panel1);
    }

}
