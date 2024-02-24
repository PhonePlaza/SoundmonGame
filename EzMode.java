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

public class EzMode extends JPanel {
    private List<Color> sequence; // ลำดับ
    private List<Color> playerInput;
    private int round;
    private Timer timer;
    private int timeLeft;
    private JButton[] buttons;
    private JLabel timerLabel; // ข้อความ Timeleft ซ้ายบน
    private JLabel roundLabel; // ข้อความ Round ขวาบน

    private boolean backButtonPressed = false;
    private boolean soundPlaying;
    private boolean gameOver;

    private Map<JButton, String> soundFiles;

    public EzMode() {
        setSize(1280, 720);
        setLayout(null);
        setBackground(Color.BLACK);

        aboutButtons();
        gameStart();

        ImportSoundFiles();
        SoundPlayer.playBGMusic(soundPlaying);

        setVisible(true);

    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    private void aboutButtons() { // method ที่เกี่ยวกับ button ทั้งหมด

        // ----- BackButton ----
        ImageIcon backBIcon = new ImageIcon("img/backButton2.png");
        Image backB = backBIcon.getImage(); // ดึงรูปภาพออกมาจาก ImageIcon
        Image newbackB = backB.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // ปรับขนาดรูปภาพใหม่
        ImageIcon newbackBIcon = new ImageIcon(newbackB);
        JButton backButton = new JButton(newbackBIcon);

        backButton.setBounds(20, 20, newbackBIcon.getIconWidth(), newbackBIcon.getIconHeight());
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);

        backButton.setBackground(Color.BLACK);
        backButton.setBorderPainted(false);
        backButton.setFocusable(false);

        ;
        backButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                backButtonPressed = true;
                soundPlaying = true;
                SoundPlayer.playBGMusic(soundPlaying);
                SoundPlayer.playClickSound();
                timer.stop();
                Mainframe.switchToPanel(new DifficultyPanel());

            }
        });
        add(backButton);
        // -----------------------

        buttons = new JButton[4];
        // Create buttons with colors
        buttons[0] = createButton(Color.GREEN);
        buttons[1] = createButton(Color.BLUE);
        buttons[2] = createButton(Color.YELLOW);
        buttons[3] = createButton(Color.RED);

        // Position buttons
        int buttonWidth = 250; 
        int buttonHeight = 250; 
        int buttonGap = 30; 

        int x = 370; 
        int y = 120; 

        for (JButton button : buttons) {
            button.setBounds(x, y, buttonWidth, buttonHeight);
            x += buttonWidth + buttonGap;

            if ((x - 370) / (buttonWidth + buttonGap) == 2) {
                x = 370; 
                y += buttonHeight + buttonGap; 
            }
            add(button);
        }

    }

    private JButton createButton(Color color) {
        JButton button = new JButton();
        button.setBorder(new RoundedBorder(50));

        button.setBackground(color);
        button.setOpaque(true);
        button.addActionListener(new ButtonClickListener());
        return button;
    }

    private void ImportSoundFiles() {
        soundFiles = new HashMap<>();
        soundFiles.put(buttons[0], "sound/red_note.wav");
        soundFiles.put(buttons[1], "sound/blue_note.wav");
        soundFiles.put(buttons[2], "sound/green_note.wav");
        soundFiles.put(buttons[3], "sound/yellow_note.wav");
    }

    public void gameStart() {

        setTimeLeft(30);
        // Initialize timerLabel
        timerLabel = new JLabel("Time Left: 30");
        timerLabel.setFont(new Font("DialogInput", Font.BOLD, 32));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setHorizontalAlignment(JLabel.LEFT);
        timerLabel.setBounds(180, 40, 300, 30); // Position the timerLabel in the upper left
        add(timerLabel);

        // Initialize roundLabel
        roundLabel = new JLabel("Round: 1");
        roundLabel.setFont(new Font("DialogInput", Font.BOLD, 32));
        roundLabel.setForeground(Color.WHITE);
        roundLabel.setHorizontalAlignment(JLabel.RIGHT);
        roundLabel.setBounds(860, 40, 200, 30); // Position the roundLabel on the right
        add(roundLabel);

        // Stop the timer if it's running
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        sequence = new ArrayList<>();
        playerInput = new ArrayList<>();
        round = 1;

        // Create a new timer for the current round

        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    System.out.println("Timeleft form EZ : " + timeLeft);

                    timeLeft--;
                    timerLabel.setText("Time Left: " + timeLeft);
                } else {
                    timerLabel.setText("  Time Out");
                    TimeOut();

                }
            }
        });

        generateSequence();
        gameOver = false;

        playSequence();

    }

    private void TimeOut() {

        JOptionPane.showMessageDialog(this, "Game Over! Timeout. You reached round " + round, "Game Over",
                JOptionPane.PLAIN_MESSAGE);
        roundLabel.setText("");
        timerLabel.setText("");
        gameStart();
    }

    private void generateSequence() {

        int randomIndex = (int) (Math.random() * 4);
        sequence.add(buttons[randomIndex].getBackground());

    }

    private void playSequence() {
        enableButtons(false);

        new Thread(() -> {
            try {
                for (Color color : sequence) {
                    Thread.sleep(800);
                    highlightButton(color, 400);

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
            JButton clickedButton = (JButton) e.getSource(); // ใช้ getSource()
                                                             // เพื่อดึงข้อมูลเกี่ยวกับอ็อบเจ็กต์ที่เป็นตัวเรียกเหตุการณ์นั้นออกมา
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
            setTimeLeft(30);
            timerLabel.setText("Time Left: " + timeLeft);

        } else {
            gameOver = true;
            setTimer(gameOver);
            JOptionPane.showMessageDialog(this, "Game Over! You reached round " + round, "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);

            roundLabel.setText(null);
            timerLabel.setText(null);

            gameStart();

        }
    }

    public void setTimer(Boolean gameOver) {
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
            BufferedImage image = ImageIO.read(new File("img/EzBG.jpg"));
            // วาดรูปภาพลงบนพื้นหลังของ JPanel
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };
}
