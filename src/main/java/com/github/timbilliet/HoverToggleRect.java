package com.github.timbilliet;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class HoverToggleRect extends JPanel {
    private final Color baseColor = new Color(100, 100, 100, 100);
    private final Color hoverColor = new Color(180, 200, 255, 50);
    private final Color borderColor = new Color(66, 135, 245);
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public HoverToggleRect(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(baseColor);
        setOpaque(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Border selectedBorder = BorderFactory.createLineBorder(borderColor, 2);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(baseColor);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Container parent = getParent();

                boolean otherSelected = false;
                if (parent == null) return;
//                for (Component c : parent.getComponents()) {
//                    if (c instanceof HoverToggleRect sibling && c != HoverToggleRect.this) {
//                        if (sibling.isSelected()) {
//                            otherSelected = true;
//                            break;
//                        }
//                    }
//                }
//                if (otherSelected) {
//                    for (Component c : parent.getComponents()) {
//                        if (c instanceof HoverToggleRect sibling && c != HoverToggleRect.this) {
//                            if (sibling.isSelected()) {
//                                return;
//                            }
//                        }
//                    }
//                }
                selected = !selected;
                setBorder(selected ? selectedBorder : null);
                repaint();
            }
        });


    }
}