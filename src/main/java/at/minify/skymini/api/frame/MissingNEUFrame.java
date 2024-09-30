package at.minify.skymini.api.frame;

import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class MissingNEUFrame {

    public MissingNEUFrame() {
        try {
            Class.forName("io.github.moulberry.notenoughupdates.NotEnoughUpdates");
        } catch (ClassNotFoundException e) {
            createFrame();
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void createFrame() {
        JFrame frame = new JFrame("SkyMini is missing mods!");
        frame.setSize(500, 350);
        frame.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(0x181818));
        JLabel imageLabel = new JLabel();

        try {
            URL url = new URL("http://1minify.com/newrp1/navbar/image1.png");
            BufferedImage image = ImageIO.read(url);
            int scaledWidth = 350;
            Image scaledImage = image.getScaledInstance(scaledWidth, -1, Image.SCALE_SMOOTH);

            ImageIcon imageIcon = new ImageIcon(scaledImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imagePanel.add(imageLabel);
        frame.add(imagePanel, BorderLayout.NORTH);


        JPanel textPanel = new JPanel();
        textPanel.setBackground(new Color(0x181818));
        JLabel textLabel = new JLabel("Skymini is missing the mod NotEnoughUpdates!\nThis mod requires NotEnoughUpdates to work");
        textLabel.setForeground(Color.WHITE);
        textPanel.add(textLabel);
        textPanel.setFont(Font.getFont("Inter"));
        frame.add(textPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0x181818));
        buttonPanel.setLayout(new FlowLayout());
        JButton button1 = new JButton("Install via Modrinth");
        button1.addActionListener(event -> {
            if(Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://modrinth.com/mod/notenoughupdates/"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        buttonPanel.add(button1);

        JButton button2 = new JButton("Close");
        button2.addActionListener(event -> {
            frame.dispose();
            FMLCommonHandler.instance().handleExit(-1);
            FMLCommonHandler.instance().expectServerStopped();
        });
        buttonPanel.add(button2);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                FMLCommonHandler.instance().handleExit(-1);
                FMLCommonHandler.instance().expectServerStopped();
            }
        });

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.getContentPane().setBackground(new Color(0x181818));
        frame.setResizable(false);
        frame.setVisible(true);
    }

}
