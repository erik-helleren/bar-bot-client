package org;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EditView extends JPanel{
	
	JPanel editPanel;
	
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
	
	JComboBox<String> editDrinkComboBox;
	JComboBox<String> addIngredientDrinkBox;
	

	JMenuItem selectDrink;
	JMenuItem createDrink;
	JMenuItem configWindow;
	
	JList<String> ingredientList;
	

	EditView(ClientModel model){
		//*************************************************************************************
		/*
		 * End of Select Panel code. Beginning of Edit Panel code
		 */
		//*************************************************************************************
		
		//setTitle("Bar-Bot");
		setMinimumSize(new Dimension(1000, 700));
		//setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		//setBackground(Color.LIGHT_GRAY);
		//setResizable(false);
		/*
		JPanel barbotPanel = new JPanel();
		barbotPanel.setLayout( new BorderLayout());
		/*getContentPane().add(barbotPanel);
		
		editPanel = new JPanel();
		editPanel.setLayout( new BorderLayout());
		editPanel.setVisible(true);
		barbotPanel.add(editPanel);
		*/
		setLayout(new BorderLayout());
		
		
		/*
		 * Accept and Cancel Button Panel
		 */
		
		JPanel acceptPanel = new JPanel();
		acceptPanel.setLayout(new BorderLayout());
		/*editPanel.*/add(acceptPanel, BorderLayout.SOUTH);
		
		editAcceptButton = new JButton("Accept");
		acceptPanel.add(editAcceptButton, BorderLayout.EAST);
		
		editResetButton = new JButton("Reset");
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
		addIngredientPanel.setPreferredSize(new Dimension(400, 0));
		
		/*
		 * Contains items for adding ingredients
		 * to the drink creator
		 */
		JPanel addIngredientDrinkPanel = new JPanel();
		addIngredientDrinkPanel.setBorder(BorderFactory.createTitledBorder("Make or Edit a Drink"));
	
		editDrinkComboBox = new JComboBox<String>();
		editDrinkComboBox.addItem(new String("Make a New Drink"));
		int n = 0;
		while(!model.getDrinkName(n).equals("")){
			editDrinkComboBox.addItem(model.getDrinkName(n));
			n++;
		}
		editDrinkComboBox.setMaximumRowCount(10);
		addIngredientDrinkPanel.add(editDrinkComboBox);
		
		addIngredientDrinkBox = new JComboBox<String>();
		addIngredientDrinkBox.addItem(new String("                    "));
		
		for(String s:model.getIngredientsList()){
			addIngredientDrinkBox.addItem(s);
		}
		
		addIngredientDrinkPanel.add(addIngredientDrinkBox);
		addIngredientDrinkButton = new JButton("Accept");
		addIngredientDrinkPanel.add(addIngredientDrinkButton);
		addIngredientPanel.add(addIngredientDrinkPanel);
		
		/*
		 * Contains items for adding ingredients
		 * to the database
		 */
		JPanel addIngredientDataPanel = new JPanel();
		addIngredientDataPanel.setLayout(new BorderLayout(0,10));
		addIngredientDataPanel.setBorder(BorderFactory.createTitledBorder("Add Ingredient to Database"));
		JPanel addIngredientEntryPanel = new JPanel();
		addIngredientEntryPanel.setLayout(new FlowLayout());
		
		addIngredientDataField = new JTextField(20);
		addIngredientDataField.setBackground(ClientMain.tbgc);
		addIngredientDataField.setForeground(ClientMain.tfgc);
		addIngredientEntryPanel.add(addIngredientDataField);
		addIngredientDataButton = new JButton("Accept");
		addIngredientEntryPanel.add(addIngredientDataButton);
		addIngredientDataPanel.add(addIngredientEntryPanel, BorderLayout.NORTH);
		
		ingredientList = new JList<String>();
		ingredientList.setBackground(ClientMain.tgbgc);
		ingredientList.setForeground(ClientMain.tfgc);
		
		addIngredientDataPanel.add(ingredientList, BorderLayout.CENTER);
		
		
		addIngredientPanel.add(addIngredientDataPanel);
		
		editDrinkPanel.add(addIngredientPanel);
		
		/*
		 * Information Editing Panel that resides on the right side of the screen
		 * Contains the drink's DRINK NAME, INGREDIENTS, and DESCRIPTION 
		 */
		JPanel informationPanel = new JPanel();
		informationPanel.setLayout(new BorderLayout());
		informationPanel.setPreferredSize(new Dimension(400, JFrame.MAXIMIZED_VERT));
		informationPanel.setBorder(BorderFactory.createTitledBorder("Make a Drink"));
		editDrinkPanel.add(informationPanel);
		
		/*editPanel.*/add(editDrinkPanel, BorderLayout.WEST);
		
		JPanel editNamePanel = new JPanel();
		editNamePanel.setLayout(new BorderLayout());
		editNamePanel.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 0));
		JLabel editNameLabel = new JLabel("Drink Name:");
		infoNameTextField = new JTextField(20);
		infoNameTextField.setBackground(ClientMain.tbgc);
		infoNameTextField.setForeground(ClientMain.tfgc);
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
			ingredientNameArray[i].setBackground(ClientMain.tgbgc);
			ingredientNameArray[i].setForeground(ClientMain.tfgc);
			ingredientNameArray[i].setText("");
			ingredientNameArray[i].setEditable(false);
			//ingredientNameArray[i].setVisible(false);
			ingredientNamePanel[i].add(ingredientNameArray[i]);
			
			amountTextArray[i] = new JTextField(3);
			amountTextArray[i].setBackground(ClientMain.tbgc);
			amountTextArray[i].setForeground(ClientMain.tfgc);
			amountTextArray[i].setText("");
			//amountTextArray[i].setVisible(false);
			textPanelArray[i] = new JPanel();
			textPanelArray[i].add(amountTextArray[i]);
			
			
			removeIngredientArray[i] = new JButton("Remove");
			//removeIngredientArray[i].addActionListener(this);
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
		infoDescriptionTextArea.setBackground(ClientMain.tbgc);
		infoDescriptionTextArea.setForeground(ClientMain.tfgc);
		infoDescriptionTextArea.setRows(5);
		infoDescriptionTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		descriptionPanel.add(descriptionLabel, BorderLayout.NORTH);
		descriptionPanel.add(infoDescriptionTextArea, BorderLayout.CENTER);
		informationPanel.add(descriptionPanel, BorderLayout.SOUTH);
		
		
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
	
	
	//Methods for combo box that contains
	Object getDrinkComboBox(){
		return editDrinkComboBox.getSelectedItem();
	}
	
	void addDrinkComboBox(String s){
		editDrinkComboBox.addItem(s);
	}
	
	void clearDrinkComboBox(){
		editDrinkComboBox.removeAllItems();
	}
	
	//Methods for the combo box that contains
	//the ingredients
	String getIngredientComboBox(){
		return addIngredientDrinkBox.getSelectedItem().toString();
	}
	
	void addIngredientComboBox(String s){
		addIngredientDrinkBox.addItem(s);
	}
	
	void clearIngredientComboBox(){
		addIngredientDrinkBox.removeAllItems();
	}
	
	void enableAddIngredientButton(Boolean b){
		addIngredientDrinkButton.setEnabled(b);
	}
	
	boolean getEnabledAddIngredientButton(){
		return addIngredientDrinkButton.isEnabled();
	}
	
	String getCreateIngredientText(){
		return addIngredientDataField.getText();
	}
	
	void setCreateIngredientText(String s){
		addIngredientDataField.setText(s);
	}
	
	void setDrinkName(String s){
		infoNameTextField.setText(s);
	}
	
	String getDrinkName(){
		return infoNameTextField.getText();
	}
	
	void setIngredientName(int i, String s){
		ingredientNameArray[i].setText(s);
	}
	
	String getIngredientName(int i){
		return ingredientNameArray[i].getText();
	}
	
	void setAmount(int i, String amount){
		amountTextArray[i].setText("" + amount);
	}
	
	String getAmount(int i){
		return amountTextArray[i].getText();
	}
	
	JButton getRemoveButton(int i){
		return removeIngredientArray[i];
	}
	
	
	void addAcceptButtonListener(ActionListener accept){
		editAcceptButton.addActionListener(accept);
	}
	
	void addCancelButtonListener(ActionListener cancel){
		editResetButton.addActionListener(cancel);
	}
	
	void addDrinkComboBoxListener(ActionListener drinkBox){
		editDrinkComboBox.addActionListener(drinkBox);
	}
	
	void addCreateIngredientListener(ActionListener create){
		addIngredientDataButton.addActionListener(create);
	}
	
	void addDrinkIngredientListener(ActionListener e){
		addIngredientDrinkButton.addActionListener(e);
	}
	
	void addRemoveButtonListener(ActionListener remove){
		for (int i = 0 ; i < 12; i++){
			removeIngredientArray[i].addActionListener(remove);
		}
	}
	
	void addWindowSwitchListener(ActionListener e){
		selectDrink.addActionListener(e);
		createDrink.addActionListener(e);
		configWindow.addActionListener(e);
	}
	
	
	
	
}
