import javax.swing.*;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Implements a test GUI for the position and distance tracking.
 */
public class FollowTest {

    public static void main(String[] args) throws InterruptedException {

        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(320, 240));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        WebcamPanel webcamPanel = new WebcamPanel(webcam);
        webcamPanel.setFPSDisplayed(true);
        webcamPanel.setDisplayDebugInfo(false);
        webcamPanel.setImageSizeDisplayed(true);
        webcamPanel.setMirrored(false);

        JPanel positionPanel = new JPanel();

        JLabel positionText = new JLabel("Position: ", JLabel.LEFT);
        positionText.setFont(new Font("Sans Serif", Font.BOLD, 20));
        JLabel positionActual = new JLabel("", JLabel.RIGHT);
        positionActual.setFont(new Font("Sans Serif", Font.PLAIN, 20));

        JPanel sizePanel = new JPanel();

        JLabel sizeText = new JLabel("Size: ", JLabel.LEFT);
        sizeText.setFont(new Font("Sans Serif", Font.BOLD, 20));
        JLabel sizeActual = new JLabel("", JLabel.RIGHT);
        sizeActual.setFont(new Font("Sans Serif", Font.PLAIN, 20));

        positionPanel.setLayout(new FlowLayout());
        positionPanel.add(positionText);
        positionPanel.add(positionActual);

        sizePanel.setLayout(new FlowLayout());
        sizePanel.add(sizeText);
        sizePanel.add(sizeActual);

        container.add(webcamPanel);
        container.add(positionPanel);
        container.add(sizePanel);

        Timer timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                BufferedImage image = webcam.getImage();
                ImageTools imageTools = new ImageTools(image);
                positionActual.setText(imageTools.getPositionOfTarget());
                sizeActual.setText(String.valueOf(imageTools.getSizeOfTarget()));
            }
        });

        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.start();

        JFrame window = new JFrame("FollowTest");
        window.add(container);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);


    }
}