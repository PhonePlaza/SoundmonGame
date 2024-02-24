import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class HardMode extends JPanel {

    private List<Color> sequence;
    private List<Color> playerInput;

    private int round;
    private Timer timer;
    private int timeLeft;
    private JButton[] buttons;

    private JLabel timerLabel;
    private JLabel roundLabel;

    private boolean backButtonPressed = false;
    private boolean soundPlaying;
    private boolean gameOver;

    private Map<JButton, String> soundFiles;

    public HardMode() {
        setSize(1280, 720);
        setLayout(null);
        setVisible(true);
        SoundPlayer.playBGMusic(soundPlaying);

        setAboutButtons();
        gameStart();
        setAboutSoundFiles();

    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    private void setAboutButtons() {
        ImageIcon backBIcon = new ImageIcon("img/backButton2.png");
        Image backB = backBIcon.getImage(); // ดึงรูปภาพออกมาจาก ImageIcon
        Image newbackB = backB.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // ปรับขนาดรูปภาพใหม่
        ImageIcon newbackBIcon = new ImageIcon(newbackB);
        JButton backButton = new JButton(newbackBIcon);

        backButton.setBounds(20, 20, newbackBIcon.getIconWidth(), newbackBIcon.getIconHeight());
        backButton.setOpaque(true);
        backButton.setBackground(Color.BLACK);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);

        ;
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                backButtonPressed = true;
                SoundPlayer.playClickSound();

                soundPlaying = true;
                SoundPlayer.playBGMusic(soundPlaying);

                timer.stop();
                Mainframe.switchToPanel(new DifficultyPanel());

            }
        });
        add(backButton);

        buttons = new JButton[6];

        // Create buttons with colors
        buttons[0] = createButton(new Color(236, 150, 137));
        buttons[1] = createButton(new Color(201, 163, 223));
        buttons[2] = createButton(new Color(119, 181, 255));
        buttons[3] = createButton(new Color(135, 216, 254));
        buttons[4] = createButton(new Color(135, 234, 234));
        buttons[5] = createButton(new Color(255, 221, 147));

        int buttonWidth = 250; // กำหนดความกว้างของปุ่ม
        int buttonHeight = 250; // กำหนดความสูงของปุ่ม
        int buttonGap = 30; // กำหนดระยะห่างระหว่างปุ่มม

        int x = 230; // ปรับตำแหน่ง X เริ่มต้น
        int y = 120; // ปรับตำแหน่ง Y เริ่มต้น

        for (JButton button : buttons) {
            button.setBounds(x, y, buttonWidth, buttonHeight);
            x += buttonWidth + buttonGap;

            // After every third button, reset x and adjust y
            if ((x - 230) / (buttonWidth + buttonGap) == 3) {
                x = 230; // Reset x-coordinate
                y += buttonHeight + buttonGap; // Adjust y-coordinate
            }
            add(button); // Add button to frame
        }
    }

    private JButton createButton(Color color) {
        JButton button = new JButton();
        button.setContentAreaFilled(true);
        button.setBorder(new RoundedBorder(50));
        button.setBackground(color);
        button.setOpaque(true);
        button.addActionListener(new ButtonClickListener());
        return button;

    }

    private void setAboutSoundFiles() {
        soundFiles = new HashMap<>();
        soundFiles.put(buttons[0], "sound/red_note.wav");
        soundFiles.put(buttons[1], "sound/blue_note.wav");
        soundFiles.put(buttons[2], "sound/green_note.wav");
        soundFiles.put(buttons[3], "sound/yellow_note.wav");
        soundFiles.put(buttons[4], "sound/orange_note.wav");
        soundFiles.put(buttons[5], "sound/magenta_note.wav");
    }

    public void gameStart() {

        setTimeLeft(20);

        timerLabel = new JLabel("Time Left: 20");
        timerLabel.setFont(new Font("DialogInput", Font.BOLD, 32));
        timerLabel.setForeground(Color.WHITE);

        timerLabel.setHorizontalAlignment(JLabel.LEFT);
        timerLabel.setBounds(180, 40, 300, 30); 
        add(timerLabel);

        roundLabel = new JLabel("Round: 1");
        roundLabel.setFont(new Font("DialogInput", Font.BOLD, 32));
        roundLabel.setForeground(Color.WHITE);
        roundLabel.setHorizontalAlignment(JLabel.RIGHT);
        roundLabel.setBounds(860, 40, 200, 30); 
        add(roundLabel);

        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        sequence = new ArrayList<>();
        playerInput = new ArrayList<>();
        round = 1;
        if (gameOver == false) {
            timer = new Timer(1000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (timeLeft > 0) {
                        System.out.println("Timeleft form Hard : " + timeLeft);
                        timeLeft--;
                        timerLabel.setText("Time Left: " + timeLeft);
                    } else {
                        timerLabel.setText("  Time Out");
                        handleTimeout();

                    }
                }
            });
            timer.start();
        }

        generateSequence();
        gameOver = false;

        playSequence();

    }

    private void handleTimeout() {

        JOptionPane.showMessageDialog(this, "Game Over! Timeout. You reached round " + round, "Game Over",
                JOptionPane.PLAIN_MESSAGE);
        roundLabel.setText("");
        timerLabel.setText("");
        gameStart();
    }

    private void generateSequence() {

        int randomIndex = (int) (Math.random() * 6);
        sequence.add(buttons[randomIndex].getBackground());

    }

    private void playSequence() {
        enableButtons(false); // ตั้งให้ปุ่มไม่สามารถกดได้
        new Thread(() -> {
            try {
                for (Color color : sequence) {
                    Thread.sleep(500);
                    highlightButton(color, 200);
                }
                enableButtons(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setTimer(gameOver);

        }).start();
    }

    private void highlightButton(Color color, int duration) {
        if (backButtonPressed == false) {
            JButton clickedButton = null;

            for (JButton button : buttons) {
                if (button.getBackground().equals(color)) {
                    button.setBackground(Color.WHITE);
                    clickedButton = button;

                    try {
                        Thread.sleep(duration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    button.setBackground(color);
                }
            }
            if (clickedButton != null) {
                playSound(clickedButton);
            }
        }
    }

    private void playSound(JButton clickedButton) {
        try {
            String soundFilePath = soundFiles.get(clickedButton);
            if (soundFilePath != null) {
                AudioInputStream audioInputStream = AudioSystem
                        .getAudioInputStream(new File(soundFilePath).getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableButtons(boolean enable) {
        for (JButton button : buttons) {
            button.setEnabled(enable);
        }
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            playerInput.add(clickedButton.getBackground());
            highlightButton(clickedButton.getBackground(), 0);

            if (playerInput.size() == sequence.size()) {
                checkRound();
            }

        }
    }

    private void checkRound() {

        if (playerInput.equals(sequence)) {

            timer.stop();
            round++;

            roundLabel.setText("Round: " + round);
            playerInput.clear();
            generateSequence();
            gameOver = false;

            playSequence();

            setTimeLeft(20);
            timerLabel.setText("Time Left: " + timeLeft);

        } else {
            gameOver = true;
            setTimer(gameOver);

            JOptionPane.showMessageDialog(this, "Game Over! You reached round "
                    + round, "Game Over", JOptionPane.INFORMATION_MESSAGE);

            roundLabel.setText(null);
            timerLabel.setText(null);
            gameStart();

        }
    }

    private void setTimer(boolean gameOver) {
        if (backButtonPressed == false && gameOver == false) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    // ส่วนของ BackGround
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            // อ่านไฟล์รูปภาพ
            BufferedImage image = ImageIO.read(new File("img/HardBG.jpg"));
            // วาดรูปภาพลงบนพื้นหลังของ JPanel
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

}
