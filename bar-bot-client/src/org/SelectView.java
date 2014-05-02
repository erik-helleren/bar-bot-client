package org;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class SelectView extends JPanel{


	JPanel selectionPanel;
	
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
	
	JMenuItem selectDrink;
	JMenuItem createDrink;
	JMenuItem configWindow;
	
	public static String imageDirectory = System.getProperty("user.dir") + System.getProperty("file.separator") + "img";
	
	private ClientModel clientModel;
	
	SelectView(ClientModel model){
		
		
		clientModel = model;
	

		//setTitle("Bar-Bot");
		setMinimumSize(new Dimension(1000, 700));
		//setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		//setBackground(Color.LIGHT_GRAY);
		//setResizable(false);
		
		
		//*************************************************************************************
		/*
		 * Beginning of Selection Panel code
		 */
		//*************************************************************************************
		/*
		JPanel barbotPanel = new JPanel();
		barbotPanel.setLayout( new BorderLayout());
		/*getContentPane().add(barbotPanel);

		selectionPanel = new JPanel();
		selectionPanel.setLayout( new BorderLayout()); 
		barbotPanel.add(selectionPanel);*/
		
		setLayout(new BorderLayout());
		
		
		/*
		 * Accept and Cancel Button Panel
		 * for Drink Selection
		 */
		JPanel selectionAcceptPanel = new JPanel();
		selectionAcceptPanel.setBackground(ClientMain.bgc);
		selectionAcceptPanel.setLayout(new BorderLayout());
		/*selectionPanel.*/add(selectionAcceptPanel, BorderLayout.SOUTH);
		
		selectionAcceptButton = new JButton("Accept");
		//selectionAcceptButton.addActionListener(this);
		selectionAcceptPanel.add(selectionAcceptButton, BorderLayout.EAST);
		
		selectionResetButton = new JButton("Reset");
		//selectionAcceptButton.addActionListener(this);
		selectionAcceptPanel.add(selectionResetButton, BorderLayout.WEST);
		
		/*
		 * Panel for holding the Drink Grid
		 * and the Search box
		 */
		JPanel drinkSelectionPanel = new JPanel();
		drinkSelectionPanel.setBackground(ClientMain.bgc);
		drinkSelectionPanel.setLayout(new BorderLayout());
		/*selectionPanel.*/add(drinkSelectionPanel, BorderLayout.CENTER);
		
		
		/*
		 * Panel for Drink Grid
		 */
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(4,3,3,3));
		drinkSelectionPanel.add(gridPanel, BorderLayout.CENTER);
		
		buttonArray = new JButton[12];
		for (int i = 0; i < 12; i++){
			buttonArray[i] = new JButton("" + i);
			try {
				buttonArray[i].setIcon(new ImageIcon(ImageIO.read(new File(imageDirectory + System.getProperty("file.separator") + "default.png"))));
			} catch (IOException e) {
				BufferedImage defaultImage = new BufferedImage(100,100, BufferedImage.TYPE_INT_ARGB);
				Graphics2D page = (Graphics2D) defaultImage.getGraphics();
				page.setColor(Color.PINK);
				page.fillRect(0,0,100,100);
				page.setColor(Color.MAGENTA.darker());
				page.drawLine(25,25,75,75);
				page.drawLine(25,75,75,25);
				try {
					ImageIO.write(defaultImage, "png", new File(imageDirectory + System.getProperty("file.separator") + "default.png"));
				} catch (IOException c) {
					c.printStackTrace();
				}
				buttonArray[i].setIcon(new ImageIcon(defaultImage));
				e.printStackTrace();
			}
			//buttonArray[i].setBorder(BorderFactory.createEmptyBorder());
			//buttonArray[i].setContentAreaFilled(false);
			if(!model.getDrinkName(i).equals("")){
				buttonArray[i].setText(model.getDrinkName(i));
			}
		//	buttonArray[i].addActionListener(this);
			gridPanel.add(buttonArray[i]);
		}
	//	int n = 0;
		//for(Drink d:drinkArray){
		//	buttonArray[n].setText(d.getName());
		//	n++;
		//}
		
		/*
		 * Panel for Search box
		 */
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		searchPanel.setBorder(BorderFactory.createTitledBorder("Search Drink Database"));
		drinkSelectionPanel.add(searchPanel, BorderLayout.NORTH);
		
		searchTextField = new JTextField(24);
		//searchTextField.addKeyListener(this);
		JPanel searchTextPanel = new JPanel(new BorderLayout());
		searchTextPanel.add(searchTextField, BorderLayout.WEST);
		searchPanel.add(searchTextPanel, BorderLayout.NORTH);
		
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
		//searchResultsList.addKeyListener(this);
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
		selectionInformationPanel.setPreferredSize(new Dimension(400, JFrame.MAXIMIZED_VERT));
		selectionInformationPanel.setBorder(BorderFactory.createTitledBorder("Drink Information"));
		/*selectionPanel.*/add(selectionInformationPanel, BorderLayout.EAST);
		
		
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
		
		/*
		 * Menu Bar
		 *
		JMenuBar menuBar = new JMenuBar();
		barbotPanel.add(menuBar, BorderLayout.NORTH);
		
		JMenu edit = new JMenu("Edit");
		menuBar.add(edit);
		
		selectDrink = new JMenuItem("Select Drink");
		//selectDrink.addActionListener(this);
		edit.add(selectDrink);
		
		createDrink = new JMenuItem("Create Drink");
		//createDrink.addActionListener(this);
		edit.add(createDrink);
		
		configWindow = new JMenuItem("Config");
		//configWindow.addActionListener(this);
		edit.add(configWindow);
		*/
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}
	
	/**
	 * Sets the text for the buttons that appear on the 
	 * main screen
	 * @param i
	 * @param s
	 */
	void setButtonArrayText(int i, String s){
		buttonArray[i].setText(s);
	}
	
	String getButtonArrayText(int i){
		return buttonArray[i].getText();
	}
	
	String getSearchText(){
		return searchTextField.getText();
	}
	
	void addSearchResult(String s){
		searchListModel.addElement(s);
	}
	
	void clearSearchResults(){
		searchListModel.removeAllElements();
	}
	
	void selectSearchResult(int i){
		searchResultsList.setSelectedIndex(i);
		searchResultsList.ensureIndexIsVisible(i);
	}
	
	int getSelectedIndex(){
		return searchResultsList.getSelectedIndex();
	}
	
	String getSelectedName(){
		return searchResultsList.getSelectedValue().toString();
	}
	
	/**
	 * Returns 1 for search by name
	 * Returns 0 for search by ingredient
	 * @return
	 */
	Boolean getSearchType(){
		return searchByNameButton.isSelected();
	}
	
	void setDrinkName(String s){
		selectInfoNameTextField.setText(s);
	}
	
	String getDrinkName(){
		return selectInfoNameTextField.getText();
	}
	
	void setIngredientName(int i, String s){
		selectIngredientNameArray[i].setText(s);
	}
	
	String getIngredientName(int i){
		return selectIngredientNameArray[i].getText();
	}
	
	void setAmount(int i, String amount){
		selectAmountTextArray[i].setText("" + amount);
	}
	
	String getAmount(int i){
		return selectAmountTextArray[i].getText();
	}
	
	
	
	void addAcceptButtonListener(ActionListener accept){
		selectionAcceptButton.addActionListener(accept);
	}
	
	void addCancelButtonListener(ActionListener cancel){
		selectionResetButton.addActionListener(cancel);
	}
	
	void addDrinkButtonListener(ActionListener drink){
		for (int i = 0; i < 12; i++){
			buttonArray[i].addActionListener(drink);
		}
	}
	
	void addSearchKeyListener(KeyListener search){
		searchTextField.addKeyListener(search);
	}
	
	void addWindowSwitchListener(ActionListener e){
		//selectDrink.addActionListener(e);
		//createDrink.addActionListener(e);
		//configWindow.addActionListener(e);
	}
	
	
}
