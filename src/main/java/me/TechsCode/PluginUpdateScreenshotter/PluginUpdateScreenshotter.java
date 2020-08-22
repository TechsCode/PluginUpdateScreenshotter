package me.TechsCode.PluginUpdateScreenshotter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
public class PluginUpdateScreenshotter {

    public static void main(String[] args){
        JFrame frame = new JFrame("Plugin Update Screenshotter");
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(300, 80));

        JTextField field = new JTextField("Plugin Name");
        frame.add(field, BorderLayout.NORTH);


        JButton button = new JButton("Retrieve");
        frame.add(button, BorderLayout.SOUTH);
        button.addActionListener(e -> {
            frame.setVisible(false);
            String plugin = field.getText();

            Screenshotter screenshotter = new Screenshotter();
            BufferedImage image = screenshotter.createScreen(plugin);

            frame.setVisible(true);

            if(image == null){
                JOptionPane.showMessageDialog(frame, "Could not find an update",frame.getTitle(), JOptionPane.PLAIN_MESSAGE);
                return;
            }

            Imgur imgur = new Imgur("28794d68afb601c");
            String url = imgur.upload(image);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(url), null);

            JOptionPane.showMessageDialog(frame, "Successfully copied the image to your Clipboard",frame.getTitle(), JOptionPane.PLAIN_MESSAGE);
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


}
