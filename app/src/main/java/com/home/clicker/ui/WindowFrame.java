package com.home.clicker.ui;

import com.home.clicker.shared.events.*;
import com.home.clicker.shared.events.custom.*;
import com.home.clicker.ui.components.panel.HistoryContainerPanel;
import com.home.clicker.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Exslims
 * 07.12.2016
 */
public class WindowFrame extends OverlaidFrame {
    private JPopupMenu settingsMenu;
    private HistoryContainerPanel history;
    private MessageFrame frame;

    private int x;
    private int y;


    public WindowFrame() {
        super("PoeShortCast");
        UIManager.getLookAndFeelDefaults().put("Menu.arrowIcon", null);
    }

    @Override
    protected void init() {
        super.init();
        setLayout(null);
        setSize(50,50);
        setBackground(AppThemeColor.TRANSPARENT);

        initSettingsContextMenu();
        initAppButton();
        initHistoryContainer();
        frame = new MessageFrame();
    }

    private void initHistoryContainer() {
        history = new HistoryContainerPanel();
        history.setLocation(800,300);
        add(history);
    }

    private void initAppButton(){
        JButton button = componentsFactory.getIconButton("app/chatImage.png",56,new Dimension(50,50));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setLocation(0,0);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        button.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                e.translatePoint(WindowFrame.this.getLocation().x - x,WindowFrame.this.getLocation().y - y);
                WindowFrame.this.setLocation(e.getX(),e.getY());
            }
        });
        button.setComponentPopupMenu(settingsMenu);
        button.setFocusable(false);

        add(button);
    }
    private void initSettingsContextMenu(){
        settingsMenu = new JPopupMenu("Popup");
        JMenuItem item = new JMenu("settings");
        item.setHorizontalTextPosition(JMenuItem.CENTER);
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                        SettingsFrame settingsFrame = new SettingsFrame();
                        settingsFrame.setVisible(true);
                }
            }
        });

        JMenuItem exit = new JMenu("Exit program");
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        exit.setHorizontalTextPosition(JMenuItem.CENTER);
        exit.setArmed(false);
        settingsMenu.add(item);
        settingsMenu.add(exit);
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(NewPatchSCEvent.class, event -> {
            JFrame frame = new JFrame("New patch");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JLabel label = new JLabel(((NewPatchSCEvent)event).getPatchTitle());
            frame.getContentPane().add(label);
            frame.pack();
            frame.setVisible(true);
        });

        EventRouter.registerHandler(RepaintEvent.class, event -> {
            WindowFrame.this.revalidate();
            WindowFrame.this.repaint();
        });

        EventRouter.registerHandler(ChangeFrameVisibleEvent.class, new SCEventHandler<ChangeFrameVisibleEvent>() {
            @Override
            public void handle(ChangeFrameVisibleEvent event) {
                switch (event.getStates()){
                    case SHOW:{
                        if(!WindowFrame.this.isShowing()) {
                            WindowFrame.this.setVisible(true);
                        }
                    }
                    break;
                    case HIDE:{
                        if(WindowFrame.this.isShowing()) {
                            WindowFrame.this.setVisible(false);
                        }
                    }
                    break;
                }
            }
        });
    }
}
