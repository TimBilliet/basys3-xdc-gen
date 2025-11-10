package com.github.timbilliet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainGUI {
    private JPanel contentPanel;
    private JPanel imagePanel;
    private JTextField signalTextField;
    private JButton saveButton;
    private JLabel typeLabel;
    private BufferedImage background;

    private int switchY = 425;
    private int switchX = 76;
    private int switchSpacing = 47;
    private List<HoverToggleRect> switches;
    private Path outputFile = Paths.get("./output.xdc");
    private String[] switchPorts = new String[]{"V17", "V16", "W16", "W17", "W15", "V15", "W14", "W13", "V2", "T3", "T2", "R3", "W2", "U1", "T1", "R2"};

    public MainGUI() {

        saveButton.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Integer> indeces = new ArrayList<>();

                String text = signalTextField.getText();
                if (text.isEmpty()) return;
                for( HoverToggleRect rect : switches ) {
                    if (rect.isSelected()){
                        indeces.add(switches.indexOf(rect));
                    }
                }
                if(indeces.size() == 1){

                    try {
                        Files.write(outputFile,"");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (indeces.size() == 1){

                }
            }
        });
    }

    private void createUIComponents() {
        background = loadImage("2dview");

        imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                double imageAspectRation = (double) background.getWidth() / (double) background.getHeight();
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int drawWidth = panelWidth;
                int drawHeight = (int) (panelWidth / imageAspectRation);

                if (drawHeight > panelHeight) {
                    drawHeight = panelHeight;
                    drawWidth = (int) (panelHeight * imageAspectRation);
                }
                g.drawImage(background, 0, 0, drawWidth, drawHeight, this);
                repaint();
            }

        };
        imagePanel.setLayout(null);
        int startX = switchX;
        switches = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            switches.add(new HoverToggleRect(30, 50));
            switches.get(i).setBounds(startX, switchY, 39, 83);
            startX += switchSpacing;
            imagePanel.add(switches.get(i));
        }
    }

    private static BufferedImage loadImage(String path) {
        try {
            URL resource = MainGUI.class.getResource("/" + path + ".jpg");
            if (resource == null) return null;
            return ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("XDC generator");
        frame.setContentPane(new MainGUI().contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
