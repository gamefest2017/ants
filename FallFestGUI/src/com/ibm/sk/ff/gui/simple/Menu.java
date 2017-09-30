package com.ibm.sk.ff.gui.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;

public class Menu extends JPanel {

	private static final long serialVersionUID = 6974035742626961138L;
	
	private Container pane = null;
	private Image backgroundImage = null;
	
	private ComboBoxModel<String> replays_model = null;
	private JComboBox<String> combo_replays = null;
	
	private ComboBoxModel<String> implementations = null;
	private JComboBox<String> combo_implementations1 = null;
	private ComboBoxModel<String> implementations2 = null;
	private JComboBox<String> combo_implementations2 = null;
	
	private JButton button_singlePlayer = null;
	private JButton button_twoPlayer = null;
	private JButton button_replay = null;
	
	private GuiEventListener listener;
	
	private InitMenuData data = null;
	
	private JButton buttonStart = null;
	
	public Menu(InitMenuData data, GuiEventListener listener, Container pane) {
		super(new BorderLayout());
		
		this.data = data;
		this.listener = listener;
		this.pane = pane;
		
		init();
	}
	
	protected void init() {
		setLayout(new BorderLayout());

		// Title
        JLabel labelTitle = new JLabel("ANTHILL", JLabel.CENTER);
        labelTitle.setOpaque(true);
        labelTitle.setFont(new Font(labelTitle.getFont().getName(), labelTitle.getFont().getStyle(), 80));
        labelTitle.setBorder(new LineBorder(Color.GRAY));
        labelTitle.setBackground(Color.BLACK);
        labelTitle.setForeground(Color.WHITE);
		
		// Images
		ImageIcon menuAntImage1 = new ImageIcon("res/menu_ant1.png");
        JLabel menuAntLabel1 = new JLabel("", menuAntImage1, JLabel.LEFT);
        ImageIcon menuAntImage2 = new ImageIcon("res/menu_ant2.png");
        JLabel menuAntLabel2 = new JLabel("", menuAntImage2, JLabel.RIGHT);
		ImageIcon menuSkullImage = new ImageIcon("res/menu_skull.png");
        JLabel menuSkullLabel = new JLabel("", menuSkullImage, JLabel.CENTER);
		ImageIcon menuStartButton1 = new ImageIcon("res/menu_start_button_normal.png");
		ImageIcon menuStartButton2 = new ImageIcon("res/menu_start_button_hover.png");
		ImageIcon menuStartButton3 = new ImageIcon("res/menu_start_button_selected.png");
		ImageIcon menuCredits = new ImageIcon("res/menu_credits.gif");
        
		// 1st list of anthills
        DefaultListModel<String> listOfAnthillNames = new DefaultListModel<>(); 
        listOfAnthillNames.addElement("Dummy Anthill");
        listOfAnthillNames.addElement("Loosers");
        listOfAnthillNames.addElement("Chuck Norris");
        listOfAnthillNames.addElement("Peter Sagan");
        listOfAnthillNames.addElement("Killers");
        listOfAnthillNames.addElement("IBM SK");
        listOfAnthillNames.addElement("Slovakia");
        listOfAnthillNames.addElement("Winners");
        JList<String> firstListOfAnthills = new JList<>(listOfAnthillNames);
        firstListOfAnthills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane firstListOfAnthillsScrollable = new JScrollPane(firstListOfAnthills);

        // 2nd list of anthills
        JList<String> secondListOfAnthills = new JList<>(listOfAnthillNames);
        secondListOfAnthills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane secondListOfAnthillsScrollable = new JScrollPane(secondListOfAnthills);

		// 3rd list of anthills
        JList<String> thirdListOfAnthills = new JList<>(listOfAnthillNames);
        thirdListOfAnthills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane thirdListOfAnthillsScrollable = new JScrollPane(thirdListOfAnthills);
		 
		// Sheets
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (tabbedPane.getSelectedIndex() == 3) {
					if (buttonStart != null) {
						buttonStart.setEnabled(false);
					}
				} else {
					if (buttonStart != null) {
						buttonStart.setEnabled(true);
					}
				}
	        }
		});

		// 1st Sheet: Single Player
        JPanel panelSinglePlayer = new JPanel(false);
        panelSinglePlayer.setLayout(new GridLayout(1, 1));
        panelSinglePlayer.add(firstListOfAnthillsScrollable);
        tabbedPane.addTab("Single Player", panelSinglePlayer);
 
        // 2nd Sheet: Duel
        JPanel panelDuel = new JPanel(false);
        panelDuel.setLayout(new GridLayout(1, 2));
        panelDuel.add(secondListOfAnthillsScrollable);
        panelDuel.add(thirdListOfAnthillsScrollable);
        tabbedPane.addTab("Duel", panelDuel);
 
        // 3rd Sheet: Tournament
        JPanel panelTournament = new JPanel();
        panelTournament.setLayout(new GridLayout(1, 1));
        panelTournament.add(menuSkullLabel);
        tabbedPane.addTab("Tournament", panelTournament);
 
        // 4th Sheet: About
        JPanel panelAbout = new JPanel(false);
        panelAbout.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        JTextPane textPane = new JTextPane();
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        textPane.setText(
        		"\n" +
        		"Gabriel Scerbak\n" +
        		"Gabriel Szabo\n" +
        		"Lenka Hudecova\n" +
        		"Omar Josue Hernandez Valdes\n" +
        		"Peter Prazenica\n" +
        		"Robert Hahn\n" +
        		"Robert Sevcik\n" +
        		"Tibor Schvartz\n" +
        		"\n" +
        	    "© Copyright IBM Slovakia 2017\n"
        	);
        textPane.setFont(new Font("Serif", Font.ITALIC, 16));
        textPane.setOpaque(false);
        JLabel labelCredits = new JLabel(menuCredits);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(40, 0, 20, 0);
        c.anchor = GridBagConstraints.CENTER;
        panelAbout.add(labelCredits, c);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 10;
        c.gridheight = 5;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        panelAbout.add(textPane, c);
        tabbedPane.addTab("About", panelAbout);

		// Title
        JLabel labelFooter = new JLabel("IBM Slovakia / Fall Fest 2017", JLabel.CENTER);
        labelFooter.setOpaque(true);
        labelFooter.setFont(new Font(labelTitle.getFont().getName(), labelTitle.getFont().getStyle(), 40));
        labelFooter.setBorder(new LineBorder(Color.GRAY));
        labelFooter.setBackground(Color.BLACK);
        labelFooter.setForeground(Color.WHITE);

        add(labelTitle, BorderLayout.PAGE_START);
        add(menuAntLabel1, BorderLayout.LINE_START);
        JPanel panelCenter = new JPanel(false);
        panelCenter.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 10;
        c.gridheight = 5;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(50, 0, 0, 0);
        c.anchor = GridBagConstraints.PAGE_END;
        panelCenter.add(tabbedPane, c);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 12;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(50, 0, 30, 0);
        c.anchor = GridBagConstraints.CENTER;
        buttonStart = new JButton(menuStartButton1);
        buttonStart.setContentAreaFilled(false);
        buttonStart.setBorderPainted(false);
        buttonStart.setFocusPainted(false);
        buttonStart.setOpaque(true);
        buttonStart.setRolloverEnabled(true);
        buttonStart.setRolloverIcon(menuStartButton2);
        buttonStart.setPressedIcon(menuStartButton3);
        panelCenter.add(buttonStart, c);
        add(panelCenter, BorderLayout.CENTER);
        add(menuAntLabel2, BorderLayout.LINE_END);
        add(labelFooter, BorderLayout.PAGE_END);
        
//
//        c.gridx = 2;
//        c.gridy = 1;
//        c.weightx = 1;
//        c.weighty = 1;
//        gridbag.setConstraints(menuAntLabel2, c);
//        add(menuAntLabel2);
//
//        gridBagConstraints.gridx = 1;
//        gridBagConstraints.gridy = 1;
//        add(tabbedPane, BorderLayout.CENTER);
			
//
//		listPane.add(label);
//		listPane.add(Box.createRigidArea(new Dimension(0,5)));
//		listPane.add(listScroller);
//		listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
//
//	
//		//Lay out the buttons from left to right.
//		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
//		buttonPane.add(Box.createHorizontalGlue());
//		JButton cancelButton = new JButton("Cancel");
//		cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//		JButton setButton = new JButton("Set");
//		setButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
//		buttonPane.add(cancelButton);
//		buttonPane.add(setButton);
//
//		// image of the left/right ant
//		ImageIcon menuAntImage1 = new ImageIcon("res/menu_ant1.png");
//        JLabel menuAntLabel1 = new JLabel("", menuAntImage1, JLabel.LEFT);
//        ImageIcon menuAntImage2 = new ImageIcon("res/menu_ant2.png");
//        JLabel menuAntLabel2 = new JLabel("", menuAntImage2, JLabel.RIGHT);

        
        
//        pane.add(label);
//		pane.add(label);
//		pane.add(listPane);

		//		
//		//Put everything together, using the content pane's BorderLayout.
//		listPane.add(listPane, BorderLayout.CENTER);
//		listPane.add(buttonPane, BorderLayout.PAGE_END);
//		
//		try {
//			backgroundImage = ImageIO.read(new File("res/grass2.jpg"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        
//        
//        add(listPane);
        
        
//		replays_model = new DefaultComboBoxModel<>(data.getReplays());
//		combo_replays = new JComboBox<>(replays_model);
//		
//		implementations = new DefaultComboBoxModel<>(data.getCompetitors());
//		combo_implementations1 = new JComboBox<>(implementations);
//		implementations2 = new DefaultComboBoxModel<>(data.getCompetitors());
//		combo_implementations2 = new JComboBox<>(implementations2);
//		
//		button_singlePlayer = new JButton("Single player");
//		button_twoPlayer = new JButton("Two players");
//		button_replay = new JButton("Replay");
//		
//		setLayout(null);
//		
//		setLocation(0, 0);
//		setSize(800, 600);
//		setPreferredSize(new Dimension(800, 600));
//		
//		int w = 280;
//		int h = 30;
//		int x = (getWidth() / 2) - (w / 2);
//		int y = 300;
//		
//		combo_replays.setSize(w, h);
//		combo_implementations1.setSize(w/2, h);
//		combo_implementations2.setSize(w/2, h);
//		
//		button_singlePlayer.setSize(w, h);
//		button_twoPlayer.setSize(w, h);
//		button_replay.setSize(w, h);
//		
//		/////////////////////////////////////////////
//		
//		/////////////////////////////////////////////
//		button_singlePlayer.setLocation(x, y);
//		button_twoPlayer.setLocation(x, y + h);
//		combo_implementations1.setLocation(x, y + (2 * h));
//		combo_implementations2.setLocation(x + (w / 2), y + (2 * h));
//		button_replay.setLocation(x, y + (3 * h));
//		combo_replays.setLocation(x, y + (4 * h));
//		
//		add(button_singlePlayer);
//		add(button_twoPlayer);
//		add(combo_implementations1);
//		add(combo_implementations2);
//		add(button_replay);
//		add(combo_replays);
		
//		setupEvents();
	}
	
	public void paintComponent(Graphics g) {
//		pane.paintComponent(g);
//		
//		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
	}
	
	protected JPanel createButtonRow(boolean changeAlignment) {
        JButton button1 = new JButton("A JButton",
                                      createImageIcon("images/middle.gif"));
        button1.setVerticalTextPosition(AbstractButton.BOTTOM);
        button1.setHorizontalTextPosition(AbstractButton.CENTER);
 
        JButton button2 = new JButton("Another JButton",
                                      createImageIcon("images/geek-cght.gif"));
        button2.setVerticalTextPosition(AbstractButton.BOTTOM);
        button2.setHorizontalTextPosition(AbstractButton.CENTER);
 
        String title;
        if (changeAlignment) {
            title = "Desired";
            button1.setAlignmentY(BOTTOM_ALIGNMENT);
            button2.setAlignmentY(BOTTOM_ALIGNMENT);
        } else {
            title = "Default";
        }
 
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(button1);
        pane.add(button2);
        return pane;
    }
 
    protected JPanel createLabelAndComponent(boolean doItRight) {
        JPanel pane = new JPanel();
 
        JComponent component = new JPanel();
        Dimension size = new Dimension(150,100);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
        TitledBorder border = new TitledBorder(
                                  new LineBorder(Color.black),
                                  "A JPanel",
                                  TitledBorder.CENTER,
                                  TitledBorder.BELOW_TOP);
        border.setTitleColor(Color.black);
        component.setBorder(border);
 
        JLabel label = new JLabel("This is a JLabel");
        String title;
        if (doItRight) {
            title = "Matched";
            label.setAlignmentX(CENTER_ALIGNMENT);
        } else {
            title = "Mismatched";
        }
 
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        pane.add(component);
        return pane;
    }
 
    protected JPanel createYAlignmentExample(boolean doItRight) {
        JPanel pane = new JPanel();
        String title;
 
        JComponent component1 = new JPanel();
        Dimension size = new Dimension(100, 50);
        component1.setMaximumSize(size);
        component1.setPreferredSize(size);
        component1.setMinimumSize(size);
        TitledBorder border = new TitledBorder(
                                  new LineBorder(Color.black),
                                  "A JPanel",
                                  TitledBorder.CENTER,
                                  TitledBorder.BELOW_TOP);
        border.setTitleColor(Color.black);
        component1.setBorder(border);
 
        JComponent component2 = new JPanel();
        size = new Dimension(100, 50);
        component2.setMaximumSize(size);
        component2.setPreferredSize(size);
        component2.setMinimumSize(size);
        border = new TitledBorder(new LineBorder(Color.black),
                                  "A JPanel",
                                  TitledBorder.CENTER,
                                  TitledBorder.BELOW_TOP);
        border.setTitleColor(Color.black);
        component2.setBorder(border);
 
        if (doItRight) {
            title = "Matched";
        } else {
            component1.setAlignmentY(TOP_ALIGNMENT);
            title = "Mismatched";
        }
 
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(component1);
        pane.add(component2);
        return pane;
    }
 
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Menu.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
	private void setupEvents() {
		//TODO
		button_singlePlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.SINGLE_PLAY_START, ""));
			}
		});
		button_twoPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.DOUBLE_PLAY_START, ""));
			}
			
		});
		button_replay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.REPLAY_SELECTED, ""));
			}
		});
		combo_replays.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.REPLAY_SELECTED, combo_replays.getSelectedItem().toString()));
			}
		});
		combo_implementations1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.PLAYER_1_SELECTED, combo_implementations1.getSelectedItem().toString()));
			}
		});
		combo_implementations2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.PLAYER_2_SELECTED, combo_implementations2.getSelectedItem().toString()));
			}
		});
	}
	
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("IBM Slovakia / Fall Fest 2017 / Anthill");
        frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        frame.add(new Menu(null, null, frame.getContentPane()), BorderLayout.CENTER);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
