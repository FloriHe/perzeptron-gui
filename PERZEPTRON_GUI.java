/*
* Copyright (C) 2024 by Florian Held
*
* Licensed under GPLv2 or later, see file LICENSE in this source tree.
* Copyright: F. Held, 2024
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PERZEPTRON_GUI extends JPanel {

    // Variablen der Grafik
    private int kreisDurchmesser = 200;
    private Graphics2D g2d;
    private int w1X, w1Y, w2X, w2Y, x1X, x1Y, x2X, x2Y, sX, sY, labelX, labelY;
    private String w1ResultatStr, w2ResultatStr, x1ResultatStr, x2ResultatStr, sResultatStr, labelResultatStr;
    // Zahlenwerte der Perzeptron Parameter
    private double x1, x2, w1, w2, s;
    private JLabel gleichungKonkretEingesetztLabel;
    // Java Swing Elemente
    private JLabel aFKonkretEingesetztLabel;
    private JTextField x1Feld, x2Feld, w1Feld, w2Feld, sFeld, labelFeld;
    private JPanel grafikPanel;
    boolean labelFarbeFlag;

    public PERZEPTRON_GUI() {
        x1 = x2 = w1 = w2 = s = 0.0;
        w1X = w1Y = w2X = w2Y = x1X = x1Y = x2X = x2Y = sX = sY = labelX = labelY = 0;
        w1ResultatStr = w2ResultatStr = w2ResultatStr = x1ResultatStr = x1ResultatStr = x2ResultatStr = x2ResultatStr = sResultatStr = sResultatStr = labelResultatStr = "";
        labelFarbeFlag = false;

        // hier werden die einzelnen Teile der GUI initialisiert
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Perzeptron GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            grafikPanel = erstelleGrafikPanel();

            JPanel gleichungPanel = erstelleGleichungPanel();
            JPanel eingabePanel = erstelleEingabePanel();
            JPanel knopfPanel = erstelleKnopfPanel();

            JPanel vereinigungsPanel = new JPanel(new BorderLayout());
            vereinigungsPanel.add(eingabePanel, BorderLayout.CENTER);
            vereinigungsPanel.add(knopfPanel, BorderLayout.SOUTH);
            frame.add(vereinigungsPanel, BorderLayout.SOUTH);
            frame.add(gleichungPanel, BorderLayout.NORTH);
            frame.add(grafikPanel, BorderLayout.CENTER);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // Fensterteil, wo die Gleichungen stehen (Oben)
    private JPanel erstelleGleichungPanel() {
        JPanel gleichungPanel = new JPanel(new GridLayout(2, 3, 64, 64));
        
        JLabel gleichungLabel = new JLabel("Gleichungen:", SwingConstants.RIGHT);
        gleichungLabel.setForeground(Color.blue);
        gleichungLabel.setFont(new Font("Dialog", Font.PLAIN, 24));        
        gleichungPanel.add(gleichungLabel);

        JLabel gleichungKonkretLabel = new JLabel("f = w_1*x_1 + w_2*x_2 - s", SwingConstants.LEFT);
        gleichungKonkretLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        gleichungPanel.add(gleichungKonkretLabel);
        
        JLabel aFLabel = new JLabel("Af(f)", SwingConstants.LEFT);
        aFLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        gleichungPanel.add(aFLabel);
        
        JLabel eingesetztLabel = new JLabel("Eingesetzt:", SwingConstants.RIGHT);
        eingesetztLabel.setForeground(Color.blue);
        eingesetztLabel.setFont(new Font("Dialog", Font.PLAIN, 24));        
        gleichungPanel.add(eingesetztLabel);
        
        double wertFNum = w1 * x1 + w2 * x2 - s;
        String wertF = Double.toString(wertFNum);
        gleichungKonkretEingesetztLabel = new JLabel("f = " + Double.toString(w1) + " * " + Double.toString(x1) + " + " + Double.toString(w2) + " * " + Double.toString(x2) + " - " + Double.toString(s) + " = " + wertF, SwingConstants.LEFT);
        gleichungKonkretEingesetztLabel.setFont(new Font("Dialog", Font.PLAIN, 20));         
        gleichungPanel.add(gleichungKonkretEingesetztLabel);
        
        String wertA = "0";
        if (wertFNum >= 0) {
            wertA = "1";
        }
        
        aFKonkretEingesetztLabel = new JLabel("A(" + wertF + ") = " + wertA, SwingConstants.LEFT);
        aFKonkretEingesetztLabel.setFont(new Font("Dialog", Font.PLAIN, 24));        
        gleichungPanel.add(aFKonkretEingesetztLabel);
        
        return gleichungPanel;
    }

    // Fensterteil, wo die Grafik steht (Mitte)
    private JPanel erstelleGrafikPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
    
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
                int breite = getWidth();
                int hoehe = getHeight();
    
                int kreisDurchmesser = 200;
                int kreisRadius = kreisDurchmesser / 2;
                int mittelpunktX = breite / 2;
                int mittelpunktY = hoehe / 2;
                g2d.setColor(Color.WHITE);
                g2d.fillOval(mittelpunktX - kreisRadius, mittelpunktY - kreisRadius, kreisDurchmesser, kreisDurchmesser);
    
                g2d.setColor(Color.BLACK);
                g2d.drawOval(mittelpunktX - kreisRadius, mittelpunktY - kreisRadius, kreisDurchmesser, kreisDurchmesser);

                int xOben = mittelpunktX - (int) (Math.cos(Math.toRadians(45)) * 2 * kreisRadius);
                int yTop = mittelpunktY - (int) (Math.sin(Math.toRadians(45)) * 2 * kreisRadius);

                int xUnten = mittelpunktX - (int) (Math.cos(Math.toRadians(-45)) * 2 * kreisRadius);
                int yUnten = mittelpunktY - (int) (Math.sin(Math.toRadians(-45)) * 2 * kreisRadius);

                zeichnePfeil(g2d, xOben, yTop, -45, kreisRadius); 
                zeichnePfeil(g2d, xUnten, yUnten, 45, kreisRadius);
                zeichnePfeil(g2d, mittelpunktX, mittelpunktY + 2 * kreisRadius, 90, kreisRadius);
                zeichnePfeil(g2d, mittelpunktX + kreisRadius, mittelpunktY, 0, kreisRadius);

                Font originalSchriftart = g2d.getFont();
                float originalSchriftartGroeße = originalSchriftart.getSize2D();
                float skalierteSchriftartGroeße = originalSchriftartGroeße * 1.5f;
                Font skalierteSchriftart = originalSchriftart.deriveFont(skalierteSchriftartGroeße);

                g2d.setFont(skalierteSchriftart);
                
                w1X = xOben + (int) (kreisRadius / (2 * Math.sqrt(2)));
                w1Y = yTop + (int) (kreisRadius / (2 * Math.sqrt(2)));
                g2d.drawString("w1", w1X, w1Y);

                w2X = xUnten + (int) (kreisRadius / (4 * Math.sqrt(2)));
                w2Y = yUnten - (int) (kreisRadius / (4 * Math.sqrt(2)));
                g2d.drawString("w2", w2X + 10, w2Y);

                x1X = xOben - 10;
                x1Y = yTop - 10;
                g2d.drawString("x1", x1X, x1Y);

                x2X = xUnten - 10;
                x2Y = yUnten - 10;
                g2d.drawString("x2", x2X , x2Y + 25);

                sX = mittelpunktX - 10;
                sY = mittelpunktY + 2 * kreisRadius;
                g2d.drawString("s", sX + 15, sY);

                labelX = mittelpunktX + 2 * kreisRadius;
                labelY = mittelpunktY - 10;
                g2d.drawString("label (Ist)", labelX, labelY);

                //set dynamic via values
                g2d.drawString(w1ResultatStr, w1X + 16, w1Y);
                g2d.drawString(w2ResultatStr, w2X + 28, w2Y);
                g2d.drawString(x1ResultatStr, x1X + 13, x1Y);
                g2d.drawString(x2ResultatStr, x2X + 13, x2Y + 25);
                g2d.drawString(sResultatStr, sX + 25, sY);
                
                if(labelFarbeFlag) {
                    g2d.setColor(new Color(50,205,50));
                } else {
                    g2d.setColor(Color.RED);
                }
                
                g2d.drawString(labelResultatStr, labelX + 75, labelY);
            }

            private void zeichnePfeil(Graphics2D g2d, int x, int y, double winkelInGrad, int laenge) {
                double winkelInRadians = Math.toRadians(winkelInGrad);

                int pfeilX = (int) (x + (laenge - 10) * Math.cos(winkelInRadians));
                int pfeilY = (int) (y - (laenge - 10) * Math.sin(winkelInRadians));

                g2d.drawLine(x, y, pfeilX, pfeilY);

                double pfeilWinkel1 = winkelInRadians + Math.toRadians(135);
                double pfeilWinkel2 = winkelInRadians - Math.toRadians(135);

                int pfeilX1 = pfeilX + (int) (Math.cos(pfeilWinkel1) * 10);
                int pfeilY1 = pfeilY - (int) (Math.sin(pfeilWinkel1) * 10);

                int pfeilX2 = pfeilX + (int) (Math.cos(pfeilWinkel2) * 10);
                int pfeilY2 = pfeilY - (int) (Math.sin(pfeilWinkel2) * 10);

                g2d.drawLine(pfeilX, pfeilY, pfeilX1, pfeilY1);
                g2d.drawLine(pfeilX, pfeilY, pfeilX2, pfeilY2);
            }
        };
    }

    // Fensterteil, wo die Eingabefelder stehen (Unten)
    private JPanel erstelleEingabePanel() {
        JPanel eingabePanel = new JPanel(new GridLayout(2, 7));

        JLabel datensatzLabel = new JLabel("Datensatz:", SwingConstants.RIGHT);
        datensatzLabel.setFont(new Font("Dialog", Font.PLAIN, 24));        
        datensatzLabel.setForeground(Color.blue);
        eingabePanel.add(datensatzLabel);

        JLabel x1Label = new JLabel("x1:", SwingConstants.RIGHT);
        x1Label.setMaximumSize(new Dimension(8, 4));
        x1Label.setFont(new Font("Dialog", Font.PLAIN, 24));        
        eingabePanel.add(x1Label);
        x1Feld = erstelleEingabeTextFeld();
        x1Feld.setFont(new Font("Dialog", Font.PLAIN, 24));        
        eingabePanel.add(x1Feld);

        JLabel x2Label = new JLabel("x2:", SwingConstants.RIGHT);
        x2Label.setMaximumSize(new Dimension(8, 4));
        x2Label.setFont(new Font("Dialog", Font.PLAIN, 24));        
        eingabePanel.add(x2Label);
        
        x2Feld = erstelleEingabeTextFeld();
        x2Feld.setFont(new Font("Dialog", Font.PLAIN, 24));        
        eingabePanel.add(x2Feld);

        JLabel labelLabel = new JLabel("label (Soll):", SwingConstants.RIGHT);
        labelLabel.setFont(new Font("Dialog", Font.PLAIN, 24));        
        eingabePanel.add(labelLabel);
        labelFeld = erstelleEingabeTextFeld();
        labelFeld.setFont(new Font("Dialog", Font.PLAIN, 24));        
        labelFeld.setForeground(new Color(50,205,50));
        eingabePanel.add(labelFeld);

        JLabel perzeptronWerteLabel = new JLabel("Perzeptron Werte:", SwingConstants.RIGHT);
        perzeptronWerteLabel.setForeground(Color.blue);
        perzeptronWerteLabel.setFont(new Font("Dialog", Font.PLAIN, 22));        
        eingabePanel.add(perzeptronWerteLabel);

        JLabel w1Label = new JLabel("w1:", SwingConstants.RIGHT);
        w1Label.setFont(new Font("Dialog", Font.PLAIN, 24));        
        eingabePanel.add(w1Label);
        w1Feld = erstelleEingabeTextFeld();
        w1Feld.setFont(new Font("Dialog", Font.PLAIN, 24));        
        eingabePanel.add(w1Feld);

        JLabel w2Label = new JLabel("w2:", SwingConstants.RIGHT);
        w2Label.setFont(new Font("Dialog", Font.PLAIN, 24));        
        eingabePanel.add(w2Label);
        w2Feld = erstelleEingabeTextFeld();
        w2Feld.setFont(new Font("Dialog", Font.PLAIN, 24));        
        eingabePanel.add(w2Feld);

        JLabel sLabel = new JLabel("s:", SwingConstants.RIGHT);
        sLabel.setFont(new Font("Dialog", Font.PLAIN, 24));        
        eingabePanel.add(sLabel);
        sFeld = erstelleEingabeTextFeld();
        sFeld.setFont(new Font("Dialog", Font.PLAIN, 24));        
        eingabePanel.add(sFeld);

        return eingabePanel;
    }

    // Fensterteil, wo der Save Knopf steht (Ganz unten)
    private JPanel erstelleKnopfPanel() {
        JPanel knopfPanel = new JPanel(new GridLayout(1, 1));
        JButton saveKnopf = new JButton("Save");
        knopfPanel.add(saveKnopf);

        saveKnopf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String x1Str = x1Feld.getText();
                String x2Str = x2Feld.getText();
                String w1Str = w1Feld.getText();
                String w2Str = w2Feld.getText();
                String sStr = sFeld.getText();
                String labelStr = labelFeld.getText();

                x1 = Double.parseDouble(x1Str);
                x2 = Double.parseDouble(x2Str);
                w1 = Double.parseDouble(w1Str);
                w2 = Double.parseDouble(w2Str);
                s = Double.parseDouble(sStr);

                double wertFNum = w1 * x1 + w2 * x2 - s;
                String wertF = Double.toString(wertFNum);
                gleichungKonkretEingesetztLabel.setText("f = " + w1 + " * " + x1 + " + " + w2 + " * " + x2 + " - " + s + " = " + wertF);
                String wertA = "0";
                if (wertFNum >= 0) {
                    wertA = "1";
                }
                aFKonkretEingesetztLabel.setText("A(" + wertF + ") = " + wertA);

                w1ResultatStr = " = " + w1Str;
                w2ResultatStr = " = " + w2Str;
                x1ResultatStr = " = " + x1Str;
                x2ResultatStr = " = " + x2Str;
                sResultatStr = " = " + sStr;
                
                if(labelStr.equals(wertA)) {
                    labelFarbeFlag = true;
                } else {
                    labelFarbeFlag = false;
                }
                
                labelResultatStr = " = " + wertA;

                // Zeichne Grafik Panel mit neuen Werten
                grafikPanel.repaint();
            }
        });
        return knopfPanel;
    }

    private JTextField erstelleEingabeTextFeld() {
        JTextField Feld = new JTextField();
        Feld.setHorizontalAlignment(JTextField.LEFT);
        return Feld;
    }
}
