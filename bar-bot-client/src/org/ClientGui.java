//package org;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.*;



public class ClientGui extends JFrame implements ActionListener, KeyListener{
	
	DrinkList drinkList;
	private Set<Drink> drinkArray;
	Drink currentDrink;
	
	static ConfigInterface ci;
	
	ArrayList<String> userIngredients;
	
	/*
	 * Overall main panel
	 */
	JPanel barbotPanel;
	
	/*
	 * Menu items
	 */
	JMenuItem createDrink;
	JMenuItem selectDrink;
	JMenuItem configWindow;
	
	/*
	 * These are the panels for Drink Selection and 
	 * for Editing Drinks
	 */
	JPanel selectionPanel;
	JPanel editPanel;
	JPanel configPanel;
	
	/*
	 * Selection Panels, Buttons, etc.
	 */
	JButton selectionAcceptButton;
	JButton selectionResetButton;
	JButton buttonArray[];
	JTextField selectInfoNameTextField;
	JTextField selectIngredientNameArray[];
	JTextField selectAmountTextArray[];
	
	/*
	 * Search 
	 */
	JTextField searchTextField;
	JTextArea searchResultsTextArea;
	JRadioButton searchByNameButton;
    JRadioButton searchByIngredientButton;
	JList searchResultsList;
	DefaultListModel<String> searchListModel;
	
	/*
	 * Edit Panels, Buttons, etc.
	 */
	JButton editAcceptButton;
	JButton editResetButton;
	
	JPanel ingredientPanelArray[];
	
	JTextField infoNameTextField;
	
	JTextField ingredientNameArray[];
	JTextField amountTextArray[];
	JButton removeIngredientArray[];
	
	JTextField addIngredientDataField;
	JButton addIngredientDataButton;
	JButton addIngredientDrinkButton;
	
	JComboBox<String> addIngredientDrinkBox;
	
	/*
	 * Config Panels, Buttons, etc.
	 */
	JButton configAcceptButton;
	JButton configResetButton;
	
	JPanel configPanelArray[];
	
	JComboBox<String> ingredientConfigComboBox[];
	
	JTextField ipConfigTextField;
	
	
	
	ClientGui(){
		//General window initialization
		setTitle("Bar-Bot");
		setSize(1000, 700);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		setBackground(Color.gray);
		//setResizable(false);
		
		//This array list contains all ingredients the user has input
		userIngredients = new ArrayList<String>();
		
		//Initializing the drink list
		drinkList = new DrinkList();
		drinkList.loadFromFile("DrinkDatabase");
    	drinkArray = drinkList.getDrinkSet();
		
    	/*
    	 * These are the overall base panels.
    	 * barbotPanel contains everything, and 
    	 * editPanel and selectionPanel handle both
    	 * the Creation and Selection screens,
    	 * respectively
    	 */
		barbotPanel = new JPanel();
		barbotPanel.setLayout(new BorderLayout());
		getContentPane().add(barbotPanel);
		
		editPanel = new JPanel();
		editPanel.setLayout( new BorderLayout());
		editPanel.setVisible(false);
		barbotPanel.add(editPanel);
		
		configPanel = new JPanel();
		configPanel.setLayout(new BorderLayout());
		barbotPanel.add(configPanel);
		
		selectionPanel = new JPanel();
		selectionPanel.setLayout( new BorderLayout()); 
		barbotPanel.add(selectionPanel);
		
		
		
		//*************************************************************************************
		/*
		 * Beginning of Selection Panel code
		 */
		//*************************************************************************************
		
		
		/*
		 * Accept and Cancel Button Panel
		 * for Drink Selection
		 */
		JPanel selectionAcceptPanel = new JPanel();
		selectionAcceptPanel.setLayout(new BorderLayout());
		selectionPanel.add(selectionAcceptPanel, BorderLayout.SOUTH);
		
		selectionAcceptButton = new JButton("Accept");
		selectionAcceptButton.addActionListener(this);
		selectionAcceptPanel.add(selectionAcceptButton, BorderLayout.EAST);
		
		selectionResetButton = new JButton("Reset");
		selectionAcceptButton.addActionListener(this);
		selectionAcceptPanel.add(selectionResetButton, BorderLayout.WEST);
		
		/*
		 * Panel for holding the Drink Grid
		 * and the Search box
		 */
		JPanel drinkSelectionPanel = new JPanel();
		drinkSelectionPanel.setLayout(new BorderLayout());
		selectionPanel.add(drinkSelectionPanel, BorderLayout.CENTER);
		
		
		/*
		 * Panel for Drink Grid
		 */
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(4,3,3,3));
		drinkSelectionPanel.add(gridPanel, BorderLayout.CENTER);
		
		buttonArray = new JButton[12];
		for (int i = 0; i < 12; i++){
			buttonArray[i] = new JButton("" + i);
			buttonArray[i].addActionListener(this);
			gridPanel.add(buttonArray[i]);
		}
		int n = 0;
		for(Drink d:drinkArray){
			buttonArray[n].setText(d.getName());
			n++;
		}
		
		/*
		 * Panel for Search box
		 */
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		searchPanel.setBorder(BorderFactory.createTitledBorder("Search Drink Database"));
		drinkSelectionPanel.add(searchPanel, BorderLayout.NORTH);
		
		searchTextField = new JTextField();
		searchTextField.addKeyListener(this);
		searchPanel.add(searchTextField, BorderLayout.NORTH);
		
		searchResultsTextArea = new JTextArea();
		searchResultsTextArea.setEditable(false);
		searchResultsTextArea.setRows(5);
		searchResultsTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JPanel searchResultPanel = new JPanel();
		searchResultPanel.setLayout(new BorderLayout());
		searchPanel.add(searchResultPanel, BorderLayout.WEST);
		
		//Create the radio buttons.
	    searchByNameButton = new JRadioButton("Name");
	    searchByNameButton.setSelected(true);

	    searchByIngredientButton = new JRadioButton("Ingredients");
	    ButtonGroup searchByGroup = new ButtonGroup();
	    
	    searchByGroup.add(searchByNameButton);
	    searchByGroup.add(searchByIngredientButton);
	    
	    JPanel searchByPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    searchByPanel.add(new JLabel("Search by: "));
	    searchByPanel.add(searchByNameButton);
	    searchByPanel.add(searchByIngredientButton);
	    searchResultPanel.add(searchByPanel, BorderLayout.NORTH);
		
		searchListModel = new DefaultListModel();
		searchResultsList = new JList(searchListModel);
		searchResultsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		searchResultsList.addKeyListener(this);
		JScrollPane searchScrollPane = new JScrollPane();
		searchScrollPane.setPreferredSize(new Dimension(50,100));
		searchScrollPane.setViewportView(searchResultsList);
		searchResultPanel.add(searchScrollPane, BorderLayout.CENTER);
		
		/*
		 * Information Panel that resides on the right side of the screen
		 * Contains the drink's DRINK NAME, INGREDIENTS, and DESCRIPTION 
		 */
		JPanel selectionInformationPanel = new JPanel();
		selectionInformationPanel.setLayout(new BorderLayout());
		selectionInformationPanel.setPreferredSize(new Dimension(400, MAXIMIZED_VERT));
		selectionInformationPanel.setBorder(BorderFactory.createTitledBorder("Drink Information"));
		selectionPanel.add(selectionInformationPanel, BorderLayout.EAST);
		
		
		JPanel selectNamePanel = new JPanel();
		selectNamePanel.setLayout(new BorderLayout());
		selectNamePanel.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 0));
		JLabel selectNameLabel = new JLabel("Drink Name:");
		selectInfoNameTextField = new JTextField(20);
		selectInfoNameTextField.setEditable(false);
		selectNamePanel.add(selectNameLabel, BorderLayout.WEST);
		selectNamePanel.add(selectInfoNameTextField, BorderLayout.CENTER);
		selectionInformationPanel.add(selectNamePanel, BorderLayout.NORTH);
		
		
		JPanel selectIngredientPanel = new JPanel();
		selectIngredientPanel.setLayout(new BoxLayout(selectIngredientPanel, BoxLayout.PAGE_AXIS));
		selectIngredientPanel.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 10));
		selectionInformationPanel.add(selectIngredientPanel, BorderLayout.CENTER);
		
		
		/*
		 * Header text for the Drink information field
		 */
		JPanel selectIngredientPanelArray[] = new JPanel[13];
		
		JLabel selectNameHeaderLabel = new JLabel("Ingredient");
		JPanel selectNameHeaderPanel = new JPanel();
		selectNameHeaderPanel.add(selectNameHeaderLabel);
		selectNameHeaderLabel.setPreferredSize(new Dimension(225,20));
		JLabel selectSizeHeaderLabel = new JLabel("Size (mL)");
		JPanel selectSizeHeaderPanel = new JPanel();
		selectSizeHeaderPanel.add(selectSizeHeaderLabel);
		
		selectIngredientPanelArray[12] = new JPanel(new BorderLayout());
		selectIngredientPanelArray[12].add(selectNameHeaderPanel, BorderLayout.WEST);
		selectIngredientPanelArray[12].add(selectSizeHeaderPanel, BorderLayout.CENTER);
		selectIngredientPanel.add(selectIngredientPanelArray[12]);
		
		
		/*
		 * Handles the Labels and textfields
		 * in the select panel's information field
		 */
		JPanel selectIngredientNamePanel[] = new JPanel[12];
		selectIngredientNameArray = new JTextField[12];
		JPanel selectTextPanelArray[] = new JPanel[12];
		selectAmountTextArray = new JTextField[12];
		
		for (int i = 0; i < 12; i++){
			
			selectIngredientPanelArray[i] = new JPanel(new BorderLayout());
			selectIngredientNamePanel[i] = new JPanel();
			selectIngredientNameArray[i] = new JTextField(20);
			selectIngredientNameArray[i].setEditable(false);
			selectIngredientNamePanel[i].add(selectIngredientNameArray[i]);
			
			selectAmountTextArray[i] = new JTextField(3);
			selectAmountTextArray[i].setEditable(false);
			selectTextPanelArray[i] = new JPanel();
			selectTextPanelArray[i].add(selectAmountTextArray[i]);
	
			selectIngredientPanelArray[i].add(selectIngredientNamePanel[i],BorderLayout.WEST);
			selectIngredientPanelArray[i].add(selectTextPanelArray[i],BorderLayout.CENTER);
			selectIngredientPanel.add(selectIngredientPanelArray[i]);
		}
		
		/*
		 * This is the layout for the Description text box.
		 * It is currently not being used
		 */
		JPanel selectDescriptionPanel = new JPanel(new BorderLayout());
		JLabel selectDescriptionLabel = new JLabel("Drink Description:");
		JTextArea selectInfoDescriptionTextArea = new JTextArea();
		selectInfoDescriptionTextArea.setEditable(false);
		selectInfoDescriptionTextArea.setRows(5);
		selectInfoDescriptionTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		selectDescriptionPanel.add(selectDescriptionLabel, BorderLayout.NORTH);
		selectDescriptionPanel.add(selectInfoDescriptionTextArea, BorderLayout.CENTER);
		selectionInformationPanel.add(selectDescriptionPanel, BorderLayout.SOUTH);
		
		
		
		//*************************************************************************************
		/*
		 * End of Select Panel code. Beginning of Edit Panel code
		 */
		//*************************************************************************************
		
		
		/*
		 * Accept and Cancel Button Panel
		 */
		
		JPanel acceptPanel = new JPanel();
		acceptPanel.setLayout(new BorderLayout());
		editPanel.add(acceptPanel, BorderLayout.SOUTH);
		
		editAcceptButton = new JButton("Accept");
		editAcceptButton.addActionListener(this);
		acceptPanel.add(editAcceptButton, BorderLayout.EAST);
		
		editResetButton = new JButton("Reset");
		editResetButton.addActionListener(this);
		acceptPanel.add(editResetButton, BorderLayout.WEST);
		
		
		/*
		 * Panel for both Ingredient addition (database and drink)
		 * and for making drinks
		 */
		JPanel editDrinkPanel = new JPanel();
		editDrinkPanel.setLayout(new BoxLayout(editDrinkPanel, BoxLayout.LINE_AXIS));

		/*
		 * Contains all Ingredient addition
		 */
		JPanel addIngredientPanel = new JPanel();
		addIngredientPanel.setLayout(new BoxLayout(addIngredientPanel, BoxLayout.PAGE_AXIS));
		addIngredientPanel.setPreferredSize(new Dimension(400, MAXIMIZED_VERT));
		
		/*
		 * Contains items for adding ingredients
		 * to the drink creator
		 */
		JPanel addIngredientDrinkPanel = new JPanel();
		addIngredientDrinkPanel.setBorder(BorderFactory.createTitledBorder("Add Ingredient to Drink"));
	
		
		try {
			loadIngredients("ingredients.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		addIngredientDrinkBox = new JComboBox<String>();
		addIngredientDrinkBox.addItem(new String("                    "));
		
		for(String s:userIngredients){
			addIngredientDrinkBox.addItem(s);
		}
		
		addIngredientDrinkPanel.add(addIngredientDrinkBox);
		addIngredientDrinkButton = new JButton("Accept");
		addIngredientDrinkButton.addActionListener(this);
		addIngredientDrinkPanel.add(addIngredientDrinkButton);
		addIngredientPanel.add(addIngredientDrinkPanel);
		
		/*
		 * Contains items for adding ingredients
		 * to the database
		 */
		JPanel addIngredientDataPanel = new JPanel();
		addIngredientDataPanel.setBorder(BorderFactory.createTitledBorder("Add Ingredient to Database"));
		
		addIngredientDataField = new JTextField(20);
		addIngredientDataPanel.add(addIngredientDataField);
		addIngredientDataButton = new JButton("Accept");
		addIngredientDataButton.addActionListener(this);
		addIngredientDataPanel.add(addIngredientDataButton);
		addIngredientPanel.add(addIngredientDataPanel);
		
		editDrinkPanel.add(addIngredientPanel);
		
		/*
		 * Information Editing Panel that resides on the right side of the screen
		 * Contains the drink's DRINK NAME, INGREDIENTS, and DESCRIPTION 
		 */
		JPanel informationPanel = new JPanel();
		informationPanel.setLayout(new BorderLayout());
		informationPanel.setPreferredSize(new Dimension(400, MAXIMIZED_VERT));
		informationPanel.setBorder(BorderFactory.createTitledBorder("Make a Drink"));
		editDrinkPanel.add(informationPanel);
		
		editPanel.add(editDrinkPanel, BorderLayout.WEST);
		
		JPanel editNamePanel = new JPanel();
		editNamePanel.setLayout(new BorderLayout());
		editNamePanel.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 0));
		JLabel editNameLabel = new JLabel("Drink Name:");
		infoNameTextField = new JTextField(20);
		editNamePanel.add(editNameLabel, BorderLayout.WEST);
		editNamePanel.add(infoNameTextField, BorderLayout.CENTER);
		informationPanel.add(editNamePanel, BorderLayout.NORTH);
		
		
		JPanel editIngredientPanel = new JPanel();
		editIngredientPanel.setLayout(new BoxLayout(editIngredientPanel, BoxLayout.PAGE_AXIS));
		editIngredientPanel.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 10));
		informationPanel.add(editIngredientPanel, BorderLayout.CENTER);
		
		
		/*
		 * Header text for the Drink creation field
		 */
		ingredientPanelArray = new JPanel[13];
		
		JLabel nameHeaderLabel = new JLabel("Ingredient");
		JPanel nameHeaderPanel = new JPanel();
		nameHeaderPanel.add(nameHeaderLabel);
		nameHeaderLabel.setPreferredSize(new Dimension(150,20));
		JLabel sizeHeaderLabel = new JLabel("Size (mL)");
		JPanel sizeHeaderPanel = new JPanel();
		sizeHeaderPanel.add(sizeHeaderLabel);
		
		ingredientPanelArray[12] = new JPanel(new BorderLayout());
		ingredientPanelArray[12].add(nameHeaderPanel, BorderLayout.WEST);
		ingredientPanelArray[12].add(sizeHeaderPanel, BorderLayout.CENTER);
		editIngredientPanel.add(ingredientPanelArray[12]);
		
		
		/*
		 * Handles the Labels, textfields, and remove buttons
		 * in the Drink creation field
		 */
		JPanel ingredientNamePanel[] = new JPanel[12];
		ingredientNameArray = new JTextField[12];
		JPanel textPanelArray[] = new JPanel[12];
		amountTextArray = new JTextField[12];
		JPanel removePanelArray[] = new JPanel[12];
		removeIngredientArray = new JButton[12];
		
		for (int i = 0; i < 12; i++){
			
			ingredientPanelArray[i] = new JPanel(new BorderLayout());
			ingredientNamePanel[i] = new JPanel();
			ingredientNameArray[i] = new JTextField(20);
			ingredientNameArray[i].setText("");
			ingredientNameArray[i].setEditable(false);
			//ingredientNameArray[i].setVisible(false);
			ingredientNamePanel[i].add(ingredientNameArray[i]);
			
			amountTextArray[i] = new JTextField(3);
			amountTextArray[i].setText("");
			//amountTextArray[i].setVisible(false);
			textPanelArray[i] = new JPanel();
			textPanelArray[i].add(amountTextArray[i]);
			
			
			removeIngredientArray[i] = new JButton("Remove");
			removeIngredientArray[i].addActionListener(this);
			//removeIngredientArray[i].setVisible(false);
			removePanelArray[i] = new JPanel();
			removePanelArray[i].add(removeIngredientArray[i]);
			
	
			ingredientPanelArray[i].add(ingredientNamePanel[i],BorderLayout.WEST);
			ingredientPanelArray[i].add(textPanelArray[i],BorderLayout.CENTER);
			ingredientPanelArray[i].add(removePanelArray[i],BorderLayout.EAST);
			//ingredientPanelArray[i].setVisible(false);
			editIngredientPanel.add(ingredientPanelArray[i]);
		}
		
		/*
		 * This is the layout for the Description text box.
		 * It is currently not being used
		 */
		JPanel descriptionPanel = new JPanel(new BorderLayout());
		JLabel descriptionLabel = new JLabel("Drink Description:");
		JTextArea infoDescriptionTextArea = new JTextArea();
		infoDescriptionTextArea.setRows(5);
		infoDescriptionTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		descriptionPanel.add(descriptionLabel, BorderLayout.NORTH);
		descriptionPanel.add(infoDescriptionTextArea, BorderLayout.CENTER);
		informationPanel.add(descriptionPanel, BorderLayout.SOUTH);
		
		
		//*************************************************************************************
		/*
		 * End of Edit Panel code. Beginning of Config Panel code
		 */
		//*************************************************************************************
				
		//ci = new dummyConfig("");
		//try {
		//	loadConfiguration("config.txt");
		//} catch (FileNotFoundException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
		/*
		 * Accept and Cancel Button Panel
		 */
		
		JPanel configAcceptPanel = new JPanel();
		configAcceptPanel.setLayout(new BorderLayout());
		configPanel.add(configAcceptPanel, BorderLayout.SOUTH);
		
		configAcceptButton = new JButton("Accept");
		configAcceptButton.addActionListener(this);
		configAcceptPanel.add(configAcceptButton, BorderLayout.EAST);
		
		configResetButton = new JButton("Reset");
		configResetButton.addActionListener(this);
		configAcceptPanel.add(configResetButton, BorderLayout.WEST);
		
		/*
		 * Information Editing Panel that resides on the right side of the screen
		 * Contains the drink's DRINK NAME, INGREDIENTS, and DESCRIPTION 
		 */
		JPanel arduinoConfigPanel = new JPanel();
		arduinoConfigPanel.setLayout(new BorderLayout());
		arduinoConfigPanel.setPreferredSize(new Dimension(400, MAXIMIZED_VERT));
		arduinoConfigPanel.setBorder(BorderFactory.createTitledBorder("Arduino Configuration"));
		configPanel.add(arduinoConfigPanel, BorderLayout.WEST);
		
		
		JPanel ipConfigPanel = new JPanel();
		ipConfigPanel.setLayout(new BorderLayout());
		ipConfigPanel.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 0));
		JLabel ipConfigLabel = new JLabel("IP Address:");
		ipConfigTextField = new JTextField(20);
		ipConfigPanel.add(ipConfigLabel, BorderLayout.WEST);
		ipConfigPanel.add(ipConfigTextField, BorderLayout.CENTER);
		arduinoConfigPanel.add(ipConfigPanel, BorderLayout.NORTH);
		
		
		JPanel ingredientConfigPanel = new JPanel();
		ingredientConfigPanel.setLayout(new BoxLayout(ingredientConfigPanel, BoxLayout.PAGE_AXIS));
		ingredientConfigPanel.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 10));
		arduinoConfigPanel.add(ingredientConfigPanel, BorderLayout.CENTER);
		
		
		/*
		 * Header text for the Drink creation field
		 */
		configPanelArray = new JPanel[13];
		
		JLabel pumpHeaderLabel = new JLabel("");//Pump #");
		JPanel pumpHeaderPanel = new JPanel();
		pumpHeaderPanel.add(pumpHeaderLabel);
		pumpHeaderLabel.setPreferredSize(new Dimension(150,20));
		JLabel ingredientHeaderLabel = new JLabel("");//Ingredient");
		JPanel ingredientHeaderPanel = new JPanel();
		ingredientHeaderPanel.add(ingredientHeaderLabel);
		
		configPanelArray[12] = new JPanel(new BorderLayout());
		configPanelArray[12].add(pumpHeaderPanel, BorderLayout.WEST);
		configPanelArray[12].add(ingredientHeaderPanel, BorderLayout.CENTER);
		ingredientConfigPanel.add(configPanelArray[12]);
		
		
		/*
		 * Handles the Labels, textfields, and remove buttons
		 * in the Drink creation field
		 */
		JPanel pumpPanelArray[] = new JPanel[12];
		
		JPanel comboPanelArray[] = new JPanel[12];
		ingredientConfigComboBox = new JComboBox[12];
		
		for (int i = 0; i < 12; i++){
			
			configPanelArray[i] = new JPanel(new BorderLayout());
			pumpPanelArray[i] = new JPanel();
			JLabel pumpNumLabel = new JLabel();
			pumpNumLabel.setText("Pump #" + (i+1));
			pumpPanelArray[i].add(pumpNumLabel);
			//ingredientNamePanel[i] = new JPanel();
			//ingredientNameArray[i] = new JTextField(20);
			//ingredientNameArray[i].setText("");
			//ingredientNameArray[i].setEditable(false);
			//ingredientNameArray[i].setVisible(false);
			//ingredientNamePanel[i].add(ingredientNameArray[i]);
			
			ingredientConfigComboBox[i] = new JComboBox<String>();
			ingredientConfigComboBox[i].addItem("N/A");
			
			for(String s:userIngredients){
				ingredientConfigComboBox[i].addItem(s);
			}
			
			
			//amountTextArray[i].setVisible(false);
			comboPanelArray[i] = new JPanel();
			comboPanelArray[i].add(ingredientConfigComboBox[i]);
			
			
	
			configPanelArray[i].add(pumpPanelArray[i],BorderLayout.WEST);
			configPanelArray[i].add(comboPanelArray[i],BorderLayout.CENTER);
			//ingredientPanelArray[i].setVisible(false);
			ingredientConfigPanel.add(configPanelArray[i]);
		}
		
		
		
		/*
		 * Menu Bar
		 */
		JMenuBar menuBar = new JMenuBar();
		barbotPanel.add(menuBar, BorderLayout.NORTH);
		
		JMenu edit = new JMenu("Edit");
		menuBar.add(edit);
		
		selectDrink = new JMenuItem("Select Drink");
		selectDrink.addActionListener(this);
		edit.add(selectDrink);
		
		createDrink = new JMenuItem("Create Drink");
		createDrink.addActionListener(this);
		edit.add(createDrink);
		
		configWindow = new JMenuItem("Config");
		configWindow.addActionListener(this);
		edit.add(configWindow);
		
		/*
		 * Shuts down the java process when the window is closed
		 */
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		//This is temporary. The GUI will probably have
		//a config tab where this can be freely edited.
		//ci=new dummyConfig("192.168.2.3");
		
		//Starting the GUI here
		ClientGui gui = new ClientGui(); 
    	gui.setVisible(true);
    	
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * From here on are the action handlers for buttons and the such
		 */
		if(e.getSource() == createDrink){
			//Switch to Create Drink view
			selectionPanel.setVisible(false);
			editPanel.setVisible(true);
			configPanel.setVisible(false);
			barbotPanel.remove(selectionPanel);
			barbotPanel.add(editPanel);
			barbotPanel.remove(configPanel);
		}
		else if(e.getSource() == selectDrink){
			//Switch to Select Drink view
			selectionPanel.setVisible(true);
			editPanel.setVisible(false);
			configPanel.setVisible(false);
			barbotPanel.add(selectionPanel);
			barbotPanel.remove(editPanel);
			barbotPanel.remove(configPanel);
		}
		else if(e.getSource() == configWindow){
			//Switch to the Configuration Window
			selectionPanel.setVisible(false);
			editPanel.setVisible(false);
			configPanel.setVisible(true);
			barbotPanel.remove(selectionPanel);
			barbotPanel.remove(editPanel);
			barbotPanel.add(configPanel);
			
		}
		else if(e.getSource() == searchTextField){
			//Handles search actions
			//searchResultsTextArea.setText(searchTextField.getText());
			//barbotPanel.update(getGraphics());
		}
		else if(e.getSource() == selectionAcceptButton){
			/*
			 * The Accept button on the Select page.
			 * Takes selected drink, and sends creation
			 * data to the Arduino
			 */
			Map<String,Integer> newIngredients = new HashMap<>();
			int i = 0;
			while(!selectIngredientNameArray[i].getText().equals("") && !selectAmountTextArray[i].getText().equals("")){
				newIngredients.put(selectIngredientNameArray[i].getText(), Integer.parseInt(selectAmountTextArray[i].getText()));
				//System.out.println(ci.getPumpID(selectIngredientNameArray[i].getText()) + " " + selectIngredientNameArray[i].getText());
				i++;
			}
			Drink newDrink = new Drink(selectInfoNameTextField.getText(), newIngredients);
			
			System.out.println("Making Drinks");
			int a;
			try {
				a = ArduinoComunicator.makeDrink(newDrink, ci);
				System.out.println("Made 1 drink"+a);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else if(e.getSource() == editAcceptButton){
			/*
			 * The Accept button on the Create Drink page
			 * Takes all data currently displayed on the creation
			 * screen and creates a JSON drink object with it.
			 * Selection Page buttons are updated from here
			 */
			Map<String,Integer> newIngredients = new HashMap<>();
			int i = 0;
			while(!ingredientNameArray[i].getText().equals("") && !amountTextArray[i].getText().equals("")){
				newIngredients.put(ingredientNameArray[i].getText(), Integer.parseInt(amountTextArray[i].getText()));
				i++;
			}
			Drink newDrink = new Drink(infoNameTextField.getText(), newIngredients);
			drinkList.getDrinkSet().add(newDrink);
			drinkList.writeToFile("DrinkDatabase");
			
			try {
				reInit();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else if(e.getSource() == configAcceptButton){
			//System.out.println("HI");
			ci = new dummyConfig(ipConfigTextField.getText());
			for(int i = 0; i < 12; i++){
				ci.setPumpID(i,ingredientConfigComboBox[i].getSelectedItem().toString());
			}
		//	try {
		//		saveConfiguration("config.txt");
		//	} catch (FileNotFoundException e1) {
		//		// TODO Auto-generated catch block
		//		e1.printStackTrace();
		//	}
		}
		else if(e.getSource() == addIngredientDataButton){
			//Updates Ingredient ComboBox with addIngredientDataField
			userIngredients.add(addIngredientDataField.getText());
			addIngredientDataField.setText("");
			try {
				saveIngredients("ingredients.txt");
				reInit();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == addIngredientDrinkButton){
			//Takes selected item from combo box, and places it in the ingredient list
			int n = 0;
			while(n < 12 && !ingredientNameArray[n].getText().equals("")){
				n++;
			}
			if(n < 12){
				ingredientNameArray[n].setText(addIngredientDrinkBox.getSelectedItem().toString());
				//ingredientNameArray[n].setVisible(true);
				//amountTextArray[n].setVisible(true);
				//removeIngredientArray[n].setVisible(true);
				//ingredientPanelArray[n].setVisible(true);
			}
			
			if(n >= 11){
				addIngredientDrinkButton.setEnabled(false);
			}
			
		}
		
		for(int i = 0; i < 12; i++){
			if(e.getSource() == removeIngredientArray[i]){
				if(!addIngredientDrinkButton.isEnabled()){
					addIngredientDrinkButton.setEnabled(true);
				}
				int m = i;
				while(m < 11 && !ingredientNameArray[m+1].getText().equals("")){
					ingredientNameArray[m].setText(ingredientNameArray[m+1].getText());
					amountTextArray[m].setText(amountTextArray[m+1].getText());
					m++;
				}
				ingredientNameArray[m].setText("");
				amountTextArray[m].setText("");
				//ingredientNameArray[m].setVisible(false);
				//amountTextArray[m].setVisible(false);
				//removeIngredientArray[m].setVisible(false);
				//ingredientPanelArray[m].setVisible(false);
			}
		}
		
		/*
		 * This part is theoretically supposed to fill the information box on the right
		 * side of the selection screen after a drink button is selected.
		 * Currently it only is able to draw data from the keySet (ingredient name);
		 * the size of the drink (the Integer part of the hashmap) is being somehow changed
		 * to a long, and as a result, I cannot access it.
		 */
		for(int i = 0; i < 12; i++){
			if(e.getSource() == buttonArray[i]){
				for(int p = 0; p < 12; p++){
					selectIngredientNameArray[p].setText("");
					selectAmountTextArray[p].setText("");
					selectInfoNameTextField.setText("");
				}
				for(Drink d:drinkArray){
					if(d.getName() == buttonArray[i].getText()){
						selectInfoNameTextField.setText(d.getName());
						currentDrink = d;
						int n = 0;
						Map<String, Integer> ingredients = d.getIngredients();
						for(String s:ingredients.keySet()){
							selectIngredientNameArray[n].setText(s);
							selectAmountTextArray[n].setText("" + ingredients.get(s));
							n++;
						}
					}
				}
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * Loads the ingredients currently stored in the file fileName
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void loadIngredients(String fileName) throws FileNotFoundException{
			userIngredients.clear();
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			userIngredients = (ArrayList<String>) in.readObject();
			in.close();
			fileIn.close();
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Saves the ingredients currently stored in userIngredients
	 * to fileName
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void saveIngredients (String fileName) throws FileNotFoundException{
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out= new ObjectOutputStream(fileOut);
			out.writeObject(userIngredients);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	
	/**
	 * Clears the current ingredient list. Not in use at the moment.
	 * @param fileName
	 */
	public void clearIngredients (String fileName){
		userIngredients.clear();
		
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out= new ObjectOutputStream(fileOut);
			out.writeObject(userIngredients);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*@SuppressWarnings("unchecked")
	*//**
	 * Loads the ingredients currently stored in the file fileName
	 * @param fileName
	 * @throws FileNotFoundException
	 *//*
	public void loadConfiguration(String fileName) throws FileNotFoundException{
		ci.clear();
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			ci =  (ConfigInterface) in.readObject();
			in.close();
			fileIn.close();
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	*//**
	 * Saves the ingredients currently stored in userIngredients
	 * to fileName
	 * @param fileName
	 * @throws FileNotFoundException
	 *//*
	public void saveConfiguration (String fileName) throws FileNotFoundException{
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out= new ObjectOutputStream(fileOut);
			out.writeObject(ci);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	*/
	/**
	 * reInit() is used whenever there is an update to the GUI
	 * More specifically, it updates both the main button grid
	 * and the ingredients listed in the Combo box
	 * @throws FileNotFoundException
	 */
	public void reInit() throws FileNotFoundException{
		int n = 0;
		for(Drink d:drinkArray){
			buttonArray[n].setText(d.getName());
			n++;
		}
		
		addIngredientDrinkBox.removeAllItems();
		addIngredientDrinkBox.addItem(new String("                    "));
		
		for(String s:userIngredients){
			addIngredientDrinkBox.addItem(s);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode()==KeyEvent.VK_DOWN){
			//if(searchResultsList.getSelectedIndex()<0){
				
			//}
			//if(searchResultsList.getComponentCount()>=searchResultsList.getSelectedIndex()){
				searchResultsList.setSelectedIndex(searchResultsList.getSelectedIndex()+1);
				searchResultsList.ensureIndexIsVisible(searchResultsList.getSelectedIndex());
			//}
		}
		else if(arg0.getKeyCode()==KeyEvent.VK_UP){
			if(searchResultsList.getSelectedIndex()<=0){
				searchTextField.requestFocusInWindow();
			}
			else{
				searchResultsList.setSelectedIndex(searchResultsList.getSelectedIndex()-1);
				searchResultsList.ensureIndexIsVisible(searchResultsList.getSelectedIndex());
			}
		}
		else if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
			if(searchResultsList.getSelectedIndex()>=0){
				for(int p = 0; p < 12; p++){
					selectIngredientNameArray[p].setText("");
					selectAmountTextArray[p].setText("");
					selectInfoNameTextField.setText("");
				}
				for(Drink d:drinkArray){
					if(d.getName() == searchResultsList.getSelectedValue()){
						selectInfoNameTextField.setText(d.getName());
						currentDrink = d;
						int n = 0;
						Map<String, Integer> ingredients = d.getIngredients();
						for(String s:ingredients.keySet()){
							selectIngredientNameArray[n].setText(s);
							selectAmountTextArray[n].setText("" + ingredients.get(s));
							n++;
						}
					}
				}
			}
		}
		else{
			if(searchTextField.getText().equals("")){
				searchResultsTextArea.setText("");
				searchListModel.removeAllElements();
				//System.out.println("HELLO");
				return;
			}
			searchListModel.removeAllElements();
			String s = new String();
			for(Drink d:drinkArray){
				if(searchByNameButton.isSelected()){
					if(d.getName().toLowerCase().indexOf(searchTextField.getText().toLowerCase()) != -1){
						s = s + d.getName() + "\n";
						searchListModel.addElement(d.getName());
					}
				}
				else{
					for(String ingredient:d.getIngredients().keySet()){
						if(ingredient.toLowerCase().indexOf(searchTextField.getText().toLowerCase()) != -1){
							searchListModel.addElement(d.getName());
						}
					}
				}
			}
			searchResultsTextArea.setText(s);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
