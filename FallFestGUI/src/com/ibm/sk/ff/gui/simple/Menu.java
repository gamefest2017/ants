package com.ibm.sk.ff.gui.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;

public class Menu extends JPanel {

	private static final long serialVersionUID = 6974035742626961138L;
	
	// Constructor parameters
	private InitMenuData initMenuData = null; // TODO: use its data
	private GuiEventListener sharedGuiListener = null;
	private JFrame mainFrame = null;
	
	/**
	 * Constructor of the main GUI page.
	 * 
	 * @param initMenuData - input information about players, recorded games, etc.
	 * @param sharedGuiListener - API for communication between GUI components
	 * @param mainContainer - the container of the main window
	 */
	public Menu(InitMenuData initMenuData, GuiEventListener sharedGuiListener, JFrame mainFrame) {
		super(new BorderLayout());
		
		this.initMenuData = initMenuData;
		this.sharedGuiListener = sharedGuiListener;
		this.mainFrame = mainFrame;
		
		init();
	}
	
	protected void init() {
		setLayout(new BorderLayout());
		
		// Components MenuListener will need
		final JButton buttonStart;
		final JTabbedPane tabbedPane;
		final JList<String> firstListOfAnthills;
		final JList<String> secondListOfAnthills;
		final JList<String> thirdListOfAnthills;
		

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
        firstListOfAnthills = new JList<>(listOfAnthillNames);
        firstListOfAnthills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane firstListOfAnthillsScrollable = new JScrollPane(firstListOfAnthills);

        // 2nd list of anthills
        secondListOfAnthills = new JList<>(listOfAnthillNames);
        secondListOfAnthills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane secondListOfAnthillsScrollable = new JScrollPane(secondListOfAnthills);

		// 3rd list of anthills
        thirdListOfAnthills = new JList<>(listOfAnthillNames);
        thirdListOfAnthills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane thirdListOfAnthillsScrollable = new JScrollPane(thirdListOfAnthills);
		 
		// Sheets
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
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
        textPane.setEditable(false);
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
		
        // Setup Events
		new MenuListener(mainFrame, sharedGuiListener, tabbedPane, firstListOfAnthills, secondListOfAnthills, thirdListOfAnthills, buttonStart);
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
        frame.add(new Menu(new InitMenuData(), null, frame), BorderLayout.CENTER);
 
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
