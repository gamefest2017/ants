package com.ibm.sk.ff.gui.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.TableColumn;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.qualification.QualificationCandidate;
import com.ibm.sk.dto.qualification.QualificationTable;
import com.ibm.sk.dto.tournament.TournamentMatch;
import com.ibm.sk.dto.tournament.TournamentTable;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class Menu extends JPanel {

	private static final long serialVersionUID = 6974035742626961138L;

	// Constructor parameters
	private InitMenuData initMenuData = null; // TODO: use its data
	private GuiEventListener sharedGuiListener = null;
	private JFrame mainFrame = null;

	/**
	 * Constructor of the main GUI page.
	 *
	 * @param initMenuData
	 *            - input information about players, recorded games, etc.
	 * @param sharedGuiListener
	 *            - API for communication between GUI components
	 * @param mainContainer
	 *            - the container of the main window
	 */
	public Menu(final InitMenuData initMenuData, final GuiEventListener sharedGuiListener, final JFrame mainFrame) {
		super(new BorderLayout());

		this.initMenuData = initMenuData;
		this.sharedGuiListener = sharedGuiListener;
		this.mainFrame = mainFrame;

		init();
	}

	protected String getStyle(final Player player, final Player winner) {
		return getStyle("rectangle", winner != null && player != null && player.getId() != null
				&& winner.getId() != null && player.getId() == winner.getId());
	}

	protected String getStyle(final String shape, final boolean forWinner) {
		return forWinner ? String.format("shape=%s;strokeColor=#5472a9;fillColor=#c3d9ff;fontColor=#5472a9", shape)
				: String.format("shape=%s;strokeColor=#878787;fillColor=#e7e7e7;fontColor=#878787", shape);
	}

	protected void init() {
		setLayout(new BorderLayout());

		// Components MenuListener will need
		final JButton buttonStart;
		final JCheckBox runInBackgroundCheckbox;
		final JTabbedPane tabbedPane;
		final JList<String> firstListOfAnthills;
		final JList<String> secondListOfAnthills;
		final JList<String> thirdListOfAnthills;
		final JList<String> replays;

		// Title
		final JLabel labelTitle = new JLabel("ANTHILL", SwingConstants.CENTER);
		labelTitle.setOpaque(true);
		labelTitle.setFont(new Font(labelTitle.getFont().getName(), labelTitle.getFont().getStyle(), 80));
		labelTitle.setBorder(new LineBorder(Color.GRAY));
		labelTitle.setBackground(Color.BLACK);
		labelTitle.setForeground(Color.WHITE);

		// Images
		final ImageIcon menuAntImage1 = new ImageIcon("res/menu_ant1.png");
		final JLabel menuAntLabel1 = new JLabel("", menuAntImage1, SwingConstants.LEFT);
		final ImageIcon menuAntImage2 = new ImageIcon("res/menu_ant2.png");
		final JLabel menuAntLabel2 = new JLabel("", menuAntImage2, SwingConstants.RIGHT);
		final ImageIcon menuSkullImage = new ImageIcon("res/menu_skull.png");
		final JLabel menuSkullLabel = new JLabel("", menuSkullImage, SwingConstants.CENTER);
		final ImageIcon menuStartButton1 = new ImageIcon("res/menu_start_button_normal.png");
		final ImageIcon menuStartButton2 = new ImageIcon("res/menu_start_button_hover.png");
		final ImageIcon menuStartButton3 = new ImageIcon("res/menu_start_button_selected.png");
		final ImageIcon menuCredits = new ImageIcon("res/menu_credits.gif");

		// 1st list of anthills
		final DefaultListModel<String> listOfAnthillNames = new DefaultListModel<>();
		addPlayers(listOfAnthillNames);
		firstListOfAnthills = new JList<>(listOfAnthillNames);
		firstListOfAnthills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JScrollPane firstListOfAnthillsScrollable = new JScrollPane(firstListOfAnthills);

		// 2nd list of anthills
		secondListOfAnthills = new JList<>(listOfAnthillNames);
		secondListOfAnthills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JScrollPane secondListOfAnthillsScrollable = new JScrollPane(secondListOfAnthills);

		// 3rd list of anthills
		thirdListOfAnthills = new JList<>(listOfAnthillNames);
		thirdListOfAnthills.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JScrollPane thirdListOfAnthillsScrollable = new JScrollPane(thirdListOfAnthills);

		// Replays
		final DefaultListModel<String> listSavedGames = new DefaultListModel<>();
		addReplays(listSavedGames);
		replays = new JList<>(listSavedGames);
		replays.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final JScrollPane replaysScrollable = new JScrollPane(replays);

		// Sheets
		tabbedPane = new JTabbedPane(SwingConstants.TOP);

		// 1st Sheet: Single Player
		final JPanel panelSinglePlayer = new JPanel(false);
		panelSinglePlayer.setLayout(new GridLayout(1, 1));
		panelSinglePlayer.add(firstListOfAnthillsScrollable);
		tabbedPane.addTab("Single Player", panelSinglePlayer);

		// 2nd Sheet: Duel
		final JPanel panelDuel = new JPanel(false);
		panelDuel.setLayout(new GridLayout(1, 2));
		panelDuel.add(secondListOfAnthillsScrollable);
		panelDuel.add(thirdListOfAnthillsScrollable);
		tabbedPane.addTab("Duel", panelDuel);

		// 3rd Sheet: Tournament
		JScrollPane scrollPaneQualification = null;
		JScrollPane scrollPaneTournament = null;
		boolean selectPanelTournament = false;
		final JPanel panelTournament = new JPanel();
		panelTournament.setLayout(new BoxLayout(panelTournament, BoxLayout.PAGE_AXIS));

		final JPanel panelTournamentRadioButtons = new JPanel();
		panelTournamentRadioButtons.setLayout(new BoxLayout(panelTournamentRadioButtons, BoxLayout.LINE_AXIS));
		final JRadioButton radioQualification = new JRadioButton("Qualification");
		final JRadioButton radioTournamentSemiFinals = new JRadioButton("Tournament (Semi / Finals)");
		final ButtonGroup group = new ButtonGroup();
		group.add(radioQualification);
		group.add(radioTournamentSemiFinals);
		panelTournamentRadioButtons.add(radioQualification);
		panelTournamentRadioButtons.add(radioTournamentSemiFinals);
		panelTournament.add(panelTournamentRadioButtons);
		panelTournament.add(Box.createRigidArea(new Dimension(0, 5)));

		if (this.initMenuData.getQualification() == null && this.initMenuData.getTournament() == null) {
			selectPanelTournament = false;
			radioQualification.setEnabled(false);
			radioTournamentSemiFinals.setEnabled(false);
			panelTournament.add(menuSkullLabel);
		}
		if (this.initMenuData.getQualification() != null) {
			selectPanelTournament = true;
			radioQualification.setSelected(true);
			final JTable tableQualification = new JTable(new MenuQualificationTableModel(this.initMenuData.getQualification()));
			for (int i = 0; i < tableQualification.getModel().getColumnCount(); i++) {
				final TableColumn tableColumn = tableQualification.getColumnModel().getColumn(i);
				tableColumn.setCellRenderer(new MenuQualificationTableCellRenderer(this.initMenuData.getQualification()));
				if (i == 1) {
					tableColumn.setPreferredWidth(300);
				} else {
					tableColumn.setPreferredWidth(10);
				}
			}
			scrollPaneQualification = new JScrollPane(tableQualification);
			tableQualification.setFillsViewportHeight(true);
			tableQualification.setBorder(BorderFactory.createEmptyBorder());
			tableQualification.setOpaque(false);
			scrollPaneQualification.setBorder(BorderFactory.createEmptyBorder());
			panelTournament.add(scrollPaneQualification, BorderLayout.CENTER);
		}
		if (this.initMenuData.getTournament() != null) {
			selectPanelTournament = true;
			if (scrollPaneQualification != null) {
				scrollPaneQualification.setVisible(false);
			}
			radioTournamentSemiFinals.setSelected(true);
			Player lastWinner = null;
			final List<Player> winnersInPreviousRound = new ArrayList<>();
			final mxGraph graph = new mxGraph();
			final Object parent = graph.getDefaultParent();
			graph.getModel().beginUpdate();
			final Map<String, Object> matchIcons = new HashMap<>();
			try {
				int x = 20;
				int y = 20;
				for (int round = 0; round < this.initMenuData.getTournament().getRounds(); round++) {
					int index = 0;
					for (final TournamentMatch match : this.initMenuData.getTournament().getMatches(round)) {
						lastWinner = match.isFinished() ? match.getWinner() : null;
						final Player player1 = match.getPlayers().get(0);
						final Player player2 = match.getPlayers().get(1);

						final Object player1Icon = graph.insertVertex(parent, null, player1 == null ? "" : player1.getName(),
								x, y, 80, 30, getStyle(player1, lastWinner));
						final Object player2Icon = graph.insertVertex(parent, null, player2 == null ? "" : player2.getName(),
								x, y + 40, 80, 30, getStyle(player2, lastWinner));
						final Object matchIcon = graph.insertVertex(parent, null, match.printScore(), x + 60, y + 15, 40, 30,
								getStyle("ellipse", true));
						matchIcons.put(round + "_" + index, matchIcon);

						if (round > 0 && matchIcons.containsKey(round - 1 + "_" + 2 * index)) {
							graph.insertEdge(parent, null, null, matchIcons.get(round - 1 + "_" + 2 * index),
									player1Icon);
						}
						if (round > 0 && matchIcons.containsKey(round - 1 + "_" + (2 * index + 1))) {
							graph.insertEdge(parent, null, null, matchIcons.get(round - 1 + "_" + (2 * index + 1)),
									player2Icon);
						}
						y += 100 + round * 100;
						index++;
					}
					x += 150;
					y = 20 + (2 * round + 1) * 50;
					winnersInPreviousRound.clear();
				}
				final int rounds = this.initMenuData.getTournament().getRounds();
				x += 50;
				y = 20 + (2 * (rounds - 1) - 1) * 50 + 20;
				final List<TournamentMatch> lastRound = this.initMenuData.getTournament().getMatches(2);
				if (lastRound != null && !lastRound.isEmpty()) {
					final TournamentMatch lastMatch = lastRound.size() == 1 && lastRound.get(0).isFinished() ? lastRound.get(0) : null;
					if (lastMatch != null && matchIcons.containsKey("2_0")) {
						final Object winner = graph.insertVertex(parent, null, lastMatch.getWinner().getName(), x, y,
								80, 30, getStyle("rectangle", true));
						graph.insertEdge(parent, null, null, matchIcons.get("2_0"), winner);
					}
				}

			} finally {
				graph.getModel().endUpdate();
			}
			final mxGraphComponent graphComponent = new mxGraphComponent(graph);
			graphComponent.setEnabled(false);
			scrollPaneTournament = new JScrollPane(graphComponent);
			graphComponent.setBorder(BorderFactory.createEmptyBorder());
			scrollPaneTournament.setBorder(BorderFactory.createEmptyBorder());
			panelTournament.add(scrollPaneTournament, BorderLayout.CENTER);
		}
		tabbedPane.addTab("Tournament", panelTournament);
		if (selectPanelTournament) {
			tabbedPane.setSelectedComponent(panelTournament);
		}

		// 4th Sheet: Replays
		final JPanel panelReplays = new JPanel(false);
		panelReplays.setLayout(new GridLayout(1, 1));
		panelReplays.add(replaysScrollable);
		tabbedPane.addTab("Replays", panelReplays);

		// 5th Sheet: About
		final JPanel panelAbout = new JPanel(false);
		panelAbout.setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();

		final JTextPane textPane = new JTextPane();
		final StyledDocument doc = textPane.getStyledDocument();
		final SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		textPane.setText("\n" + "Gabriel Scerbak\n" + "Gabriel Szabo\n" + "Lenka Hudecova\n"
				+ "Omar Josue Hernandez Valdes\n" + "Peter Prazenica\n" + "Robert Hahn\n" + "Robert Sevcik\n"
				+ "Tibor Schvartz\n" + "Vladimir Martinka\n"+ "\n"
				+ "The logic game Anthill has been designed and developed for this year's IBM Fall Festival. "
				+ "The festival creates a unique platform, connecting the theme of technological development "
				+ "with experiencing real working environment of an international company. "
				+ "It combines the opportunity to participate in workshops with progressive content "
				+ "in real offices, with the possibility to discover the core of IBM business in Slovakia "
				+ "© all this in a warm atmosphere of a starting autumn accompanied by local music bands. "
				+ "We really encourage you to take part.\n\n"
				+ "© Copyright IBM Slovakia 2017");
		textPane.setEditable(false);
		textPane.setFont(new Font("Serif", Font.ITALIC, 16));
		textPane.setOpaque(false);
		final JLabel labelCredits = new JLabel(menuCredits);
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
		final JScrollPane scrollPanelAbout = new JScrollPane(panelAbout,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelAbout.setBorder(BorderFactory.createEmptyBorder());
		panelAbout.setAutoscrolls(true);
		scrollPanelAbout.setBorder(BorderFactory.createEmptyBorder());
		tabbedPane.addTab("About", panelAbout);

		// Title
		final JLabel labelFooter = new JLabel("IBM Slovakia / Fall Fest 2017", SwingConstants.CENTER);
		labelFooter.setOpaque(true);
		labelFooter.setFont(new Font(labelTitle.getFont().getName(), labelTitle.getFont().getStyle(), 40));
		labelFooter.setBorder(new LineBorder(Color.GRAY));
		labelFooter.setBackground(Color.BLACK);
		labelFooter.setForeground(Color.WHITE);

		add(labelTitle, BorderLayout.PAGE_START);
		add(menuAntLabel1, BorderLayout.LINE_START);
		final JPanel panelCenter = new JPanel(false);
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
		runInBackgroundCheckbox = new JCheckBox("Invisible game");
		runInBackgroundCheckbox.setSelected(this.initMenuData.isRunInBackground());
		final JPanel panelStartGame = new JPanel(false);
		panelStartGame.setLayout(new GridLayout(1, 2));
		panelStartGame.add(buttonStart);
		panelStartGame.add(runInBackgroundCheckbox);
		panelCenter.add(panelStartGame, c);

		add(panelCenter, BorderLayout.CENTER);
		add(menuAntLabel2, BorderLayout.LINE_END);
		add(labelFooter, BorderLayout.PAGE_END);

		// Setup Events
		new MenuListener(this.mainFrame, this.sharedGuiListener, tabbedPane, firstListOfAnthills, secondListOfAnthills,
				thirdListOfAnthills, replays, buttonStart, radioQualification, radioTournamentSemiFinals,
				scrollPaneQualification, scrollPaneTournament, runInBackgroundCheckbox);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(final String path) {
		final java.net.URL imgURL = Menu.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Adds all known competitors to the provided list.
	 * @see InitMenuData#getCompetitors()
	 * @param list - the list we would like to initialize
	 */
	protected void addPlayers(final DefaultListModel<String> list) {
		if (this.initMenuData != null && this.initMenuData.getCompetitors() != null
				&& this.initMenuData.getCompetitors().length > 0) {
			for (final String competitor : this.initMenuData.getCompetitors()) {
				list.addElement(competitor);
			}
		}
	}

	/**
	 * Adds all known replays to the provided list.
	 * @see InitMenuData#getReplays()
	 * @param list - the list we would like to initialize
	 */
	protected void addReplays(final DefaultListModel<String> list) {
		if (this.initMenuData != null && this.initMenuData.getReplays() != null
				&& this.initMenuData.getReplays().length > 0) {
			for (final String replay : this.initMenuData.getReplays()) {
				list.addElement(replay);
			}
		}
	}

	/**
	 * Initializes GUI lists (the list of players in Single Mode / Duel / Tournament, the list of saved games) with dummy data.
	 *
	 * @param initMenuData - the input parameter for {@link Menu}
	 */
	private static void initDummyData(final InitMenuData initMenuData) {

		// Dummy list of players
		initMenuData.setCompetitors(new String[] {
				"Dummy Anthill",
				"Loosers",
				"Chuck Norris",
				"Peter Sagan",
				"Killers",
				"IBM SK",
				"Slovakia",
		"Winners" });

		// Dummy list of replays
		initMenuData.setReplays(new String[] {
				"[2017-10-01 10:00:05, Duel] Chuck Norris - Superman",
				"[2017-10-01 12:10:45, Duel] Batman - Superman",
				"[2017-10-01 17:20:11, Duel] Chuck Norris - Lady Gaga",
				"[2017-10-01 19:10:44, Duel] Lady Gaga - Justin Bieber",
				"[2017-10-02 10:00:05, Single Player] Chuck Norris",
				"[2017-10-02 12:10:45, Single Player] Superman",
				"[2017-10-02 17:20:11, Single Player] Lady Gaga",
				"[2017-10-02 19:10:44, Single Player] Justin Bieber",
				"[2017-10-03 10:00:05, Qualification] Chuck Norris",
				"[2017-10-03 12:10:45, Qualification] Superman",
				"[2017-10-03 17:20:11, Qualification] Lady Gaga",
				"[2017-10-03 19:10:44, Qualification] Justin Bieber",
				"[2017-10-03 20:00:05, Tournament] Chuck Norris - Superman",
				"[2017-10-03 22:10:45, Tournament] Batman - Superman",
				"[2017-10-03 23:20:11, Tournament] Chuck Norris - Lady Gaga",
				"[2017-10-03 23:30:44, Tournament] Lady Gaga - Justin Bieber"
		});

		// Dummy qualification results
		final QualificationTable qualificationTable = new QualificationTable();
		qualificationTable.addCandidate(new QualificationCandidate(1, "Player 1", true, 34L, 45L, 27L));
		qualificationTable.addCandidate(new QualificationCandidate(2, "Player 2", true, 32L, 46L, 23L));
		qualificationTable.addCandidate(new QualificationCandidate(3, "Player 3", true, 31L, 23L, 13L));
		qualificationTable.addCandidate(new QualificationCandidate(4, "Player 4", true, 30L, 21L, 12L));
		qualificationTable.addCandidate(new QualificationCandidate(5, "Player 5", true, 29L, 19L, 13L));
		qualificationTable.addCandidate(new QualificationCandidate(6, "Player 6", true, 28L, 17L, 14L));
		qualificationTable.addCandidate(new QualificationCandidate(7, "Player 7", true, 27L, 15L, 15L));
		qualificationTable.addCandidate(new QualificationCandidate(8, "Player 8", true, 26L, 13L, 13L));
		qualificationTable.addCandidate(new QualificationCandidate(9, "Player 9", false, 25L, 11L));
		qualificationTable.addCandidate(new QualificationCandidate(10, "Player 10", false, 24L));
		initMenuData.setQualification(qualificationTable);

		// Dummy tournament results
		final TournamentTable tournamentTable = new TournamentTable();
		//		tournamentTable.addMatch(0, new Match(new PlayerStatus(new Player(1, "aPlayer 1"), 15),
		//				new PlayerStatus(new Player(2, "aPlayer 2"), 30), true));
		//		tournamentTable.addMatch(0, new Match(new PlayerStatus(new Player(3, "aPlayer 3"), 11),
		//				new PlayerStatus(new Player(4, "aPlayer 4"), 27), true));
		//		tournamentTable.addMatch(0, new Match(new PlayerStatus(new Player(5, "aPlayer 5"), 42),
		//				new PlayerStatus(new Player(6, "aPlayer 6"), 12), true));
		//		tournamentTable.addMatch(0, new Match(new PlayerStatus(new Player(7, "aPlayer 7"), 25),
		//				new PlayerStatus(new Player(8, "aPlayer 8"), 32), true));
		//		tournamentTable.addMatch(1, new Match(new PlayerStatus(new Player(2, "bPlayer 2"), 55),
		//				new PlayerStatus(new Player(5, "bPlayer 4"), 20), true));
		//		tournamentTable.addMatch(1, new Match(new PlayerStatus(new Player(5, "bPlayer 5"), 11),
		//				new PlayerStatus(new Player(8, "bPlayer 8"), 60), true));
		//		tournamentTable.addMatch(2, new Match(new PlayerStatus(new Player(2, "cPlayer 2"), 32),
		//				new PlayerStatus(new Player(8, "cPlayer 8"), 23), true));
		initMenuData.setTournament(tournamentTable);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		final JFrame frame = new JFrame("IBM Slovakia / Fall Fest 2017 / Anthill");
		frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		// frame.add(new Menu(new InitMenuData(), new GameMenuHandler(), frame),
		// BorderLayout.CENTER);
		final InitMenuData initMenuData = new InitMenuData();
		initDummyData(initMenuData);
		frame.add(new Menu(initMenuData, null, frame), BorderLayout.CENTER);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(final String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
