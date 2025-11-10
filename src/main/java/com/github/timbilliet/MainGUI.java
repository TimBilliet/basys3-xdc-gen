package com.github.timbilliet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class MainGUI {
    private JPanel contentPanel;
    private JPanel imagePanel;
    private JTextField signalTextField;
    private JButton saveButton;
    private BufferedImage background;

    private int switchY = 425;
    private int switchX = 76;
    private int switchSpacing = 47;
    private List<HoverToggleRect> switches;
    private Path outputFile = Paths.get("./basys3.xdc");
    private String[] switchPorts = new String[]{"R2", "T1", "U1", "W2", "R3", "T2", "T3", "V2", "W13", "W14", "V15", "W15", "W17", "W16", "V16", "V17"};

    public MainGUI() {

        saveButton.addActionListener(_ -> {
            ArrayList<Integer> indeces = new ArrayList<>();

            String text = signalTextField.getText();
            if (text.isEmpty()) return;
            for (HoverToggleRect rect : switches) {
                if (rect.isSelected()) {
                    indeces.add(switches.indexOf(rect));
                }
            }
            if (indeces.size() == 1) {//only 1 component selected
                try (FileWriter fw = new FileWriter(outputFile.toFile())){
                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write(("set_property IOSTANDARD LVCMOS33 [get_ports " + text + "]"));
                    bw.newLine();
                    bw.write(("set_property PACKAGE_PIN " + switchPorts[indeces.getFirst()] + " [get_ports " + text + "]"));
                    bw.newLine();
                    bw.close();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                for(int i = 0; i < indeces.size(); i++){
//                    try (FileWriter fw = new FileWriter(outputFile.toFile())){
//                        BufferedWriter bw = new BufferedWriter(fw);
//
//                        bw.write(("set_property IOSTANDARD LVCMOS33 [get_ports " + text + "]"));
//                        bw.newLine();
//                        bw.write(("set_property PACKAGE_PIN " + switchPorts[indeces.getFirst()] + " [get_ports " + text + "]"));
//                        bw.newLine();
//                        bw.close();
//
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
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
