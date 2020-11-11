//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package impl;

import javax.swing.*;
import java.awt.*;

public class GUIFrame extends JFrame {
    private static final long serialVersionUID = 8266293222544181493L;
    private JPanel jContentPane = null;
    private GUIPanel gamePanel = null;
    private Game game = null;

    public GUIFrame(Game game) {
        this.game = game;
        this.initialize();
    }

    private void initialize() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("../icon.png")));
        this.setName("mainFrame");
        this.setDefaultCloseOperation(3);
        this.setContentPane(this.getJContentPane());
        this.setTitle("Alcatraz v1.0");
        this.setResizable(false);
        this.pack();
    }

    private JPanel getJContentPane() {
        if (this.jContentPane == null) {
            this.jContentPane = new JPanel();
            this.jContentPane.setLayout(new BorderLayout());
            this.jContentPane.setPreferredSize(new Dimension(540, 660));
            this.jContentPane.add(this.getGamePanel(), "North");
        }

        return this.jContentPane;
    }

    public GUIPanel getGamePanel() {
        if (this.gamePanel == null) {
            this.gamePanel = new GUIPanel();
            this.gamePanel.setPreferredSize(new Dimension(540, 660));
            this.gamePanel.setGame(this.game);
        }

        return this.gamePanel;
    }
}
