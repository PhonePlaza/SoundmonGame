import java.io.File; //import Class File จากแพ็คเกจ java.io เพื่อใช้ในการจัดการไฟล์เสียง
import javax.sound.sampled.AudioInputStream; //import Class AudioInputStream จากแพ็คเกจ javax.sound.sampled เพื่อใช้ในการอ่านข้อมูลเสียง
import javax.sound.sampled.AudioSystem; //import Class AudioSystem จากแพ็คเกจ javax.sound.sampled เพื่อใช้ในการจัดการระบบเสียง
import javax.sound.sampled.Clip; //import Class Clip จากแพ็คเกจ javax.sound.sampled เพื่อใช้ในการเล่นไฟล์เสียง
import javax.sound.sampled.FloatControl;

public class SoundPlayer {
    private static Clip clip;

    public static void playClickSound() {

        try {
            File soundFile = new File("sound/click.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            double gain = 0.3; // ลดระดับเสียงลงเหลือ 30%
            float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);

            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void playBGMusic(boolean soundPlaying) {
        try {
            if (soundPlaying) {
                File soundFile = new File("sound/BackGroundMusic.wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                clip = AudioSystem.getClip();
                clip.open(audioIn);
    
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                double gain = 0.05; // ลดระดับเสียงลงเหลือ 0.05%
                float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
    
                clip.start();
            } else {
                if (clip != null) { // ตรวจสอบว่า clip ไม่เป็น null ก่อนเรียกใช้งาน
                    clip.stop();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
