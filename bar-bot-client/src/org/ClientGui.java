package org;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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



public class ClientGui extends JFrame implements ActionListener{
	
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
	
	/*
	 * These are the panels for Drink Selection and 
	 * for Editing Drinks
	 */
	JPanel selectionPanel;
	JPanel editPanel;
	
	/*
	 * Selection Panels, Buttons, etc.
	 */
	JButton selectionAcceptButton;
	JButton selectionResetButton;
	JButton buttonArray[];
	JTextField selectIngredientNameArray[];
	JTextField selectAmountTextArray[];
	
	/*
	 * Edit Panels, Buttons, etc.
	 */
	JButton editAcceptButton;
	JButton editResetButton;
	
	JTextField infoNameTextField;
	
	JTextField ingredientNameArray[];
	JTextField amountTextArray[];
	
	JTextField addIngredientDataField;
	JButton addIngredientDataButton;
	JButton addIngredientDrinkButton;
	
	JComboBox<String> addIngredientDrinkBox;
	
	
	
	
	ClientGui(){
		userIngredients = new ArrayList<String>();
		setTitle("Bar-Bot");
		setSize(1000, 700);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		setBackground(Color.gray);
		//setResizable(false);
		
		drinkList = new DrinkList();
		drinkList.loadFromFile("DrinkDatabase");
    	drinkArray = drinkList.getDrinkSet();
		

		barbotPanel = new JPanel();
		barbotPanel.setLayout(new BorderLayout());
		getContentPane().add(barbotPanel);
		
		editPanel = new JPanel();
		editPanel.setLayout( new BorderLayout());
		editPanel.setVisible(false);
		barbotPanel.add(editPanel);
		
		selectionPanel = new JPanel();
		selectionPanel.setLayout( new BorderLayout()); 
		barbotPanel.add(selectionPanel);
		
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
		 * Panel for Drink Grid
		 */
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(4,3,3,3));
		selectionPanel.add(gridPanel, BorderLayout.CENTER);
		
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
		JTextField selectInfoNameTextField = new JTextField(20);
		selectInfoNameTextField.setEditable(false);
		selectNamePanel.add(selectNameLabel, BorderLayout.WEST);
		selectNamePanel.add(selectInfoNameTextField, BorderLayout.CENTER);
		selectionInformationPanel.add(selectNamePanel, BorderLayout.NORTH);
		
		
		JPanel selectIngredientPanel = new JPanel();
		selectIngredientPanel.setLayout(new BoxLayout(selectIngredientPanel, BoxLayout.PAGE_AXIS));
		selectIngredientPanel.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 10));
		selectionInformationPanel.add(selectIngredientPanel, BorderLayout.CENTER);
		
		
		/*
		 * Header text for the Drink creation field
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
		 * Handles the Labels, textfields, and remove buttons
		 * in the Drink creation field
		 */
		JPanel selectIngredientNamePanel[] = new JPanel[12];
		selectIngredientNameArray = new JTextField[12];
		JPanel selectTextPanelArray[] = new JPanel[12];
		selectAmountTextArray = new JTextField[12];
		//JPanel selectRemovePanelArray[] = new JPanel[12];
		//JButton selectRemoveIngredientArray[] = new JButton[12];
		
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
			
			
			//selectRemoveIngredientArray[i] = new JButton("Remove");
			//selectRemovePanelArray[i] = new JPanel();
			//selectRemovePanelArray[i].add(selectRemoveIngredientArray[i]);
			
	
			selectIngredientPanelArray[i].add(selectIngredientNamePanel[i],BorderLayout.WEST);
			selectIngredientPanelArray[i].add(selectTextPanelArray[i],BorderLayout.CENTER);
			//selectIngredientPanelArray[i].add(selectRemovePanelArray[i],BorderLayout.EAST);
			selectIngredientPanel.add(selectIngredientPanelArray[i]);
		}
		
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		addIngredientDrinkBox = new JComboBox<String>();
		addIngredientDrinkBox.addItem(new String("        "));
		
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
		JPanel ingredientPanelArray[] = new JPanel[13];
		
		JLabel nameHeaderLabel = new JLabel("Ingredient");
		nameHeaderLabel.setPreferredSize(new Dimension(150,20));
		JLabel sizeHeaderLabel = new JLabel("Size (mL)");
		JPanel sizeHeaderPanel = new JPanel();
		sizeHeaderPanel.add(sizeHeaderLabel);
		
		ingredientPanelArray[12] = new JPanel(new BorderLayout());
		ingredientPanelArray[12].add(nameHeaderLabel, BorderLayout.WEST);
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
		JButton removeIngredientArray[] = new JButton[12];
		
		for (int i = 0; i < 12; i++){
			
			ingredientPanelArray[i] = new JPanel(new BorderLayout());
			ingredientNamePanel[i] = new JPanel();
			ingredientNameArray[i] = new JTextField(20);
			ingredientNameArray[i].setText("");
			ingredientNameArray[i].setEditable(false);
			ingredientNamePanel[i].add(ingredientNameArray[i]);
			
			amountTextArray[i] = new JTextField(3);
			amountTextArray[i].setText("");
			textPanelArray[i] = new JPanel();
			textPanelArray[i].add(amountTextArray[i]);
			
			
			removeIngredientArray[i] = new JButton("Remove");
			removePanelArray[i] = new JPanel();
			removePanelArray[i].add(removeIngredientArray[i]);
			
	
			ingredientPanelArray[i].add(ingredientNamePanel[i],BorderLayout.WEST);
			ingredientPanelArray[i].add(textPanelArray[i],BorderLayout.CENTER);
			ingredientPanelArray[i].add(removePanelArray[i],BorderLayout.EAST);
			editIngredientPanel.add(ingredientPanelArray[i]);
		}
		
		
		JPanel descriptionPanel = new JPanel(new BorderLayout());
		JLabel descriptionLabel = new JLabel("Drink Description:");
		JTextArea infoDescriptionTextArea = new JTextArea();
		infoDescriptionTextArea.setRows(5);
		infoDescriptionTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		descriptionPanel.add(descriptionLabel, BorderLayout.NORTH);
		descriptionPanel.add(infoDescriptionTextArea, BorderLayout.CENTER);
		informationPanel.add(descriptionPanel, BorderLayout.SOUTH);
		
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
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		ci=new dummyConfig("192.168.2.3");
		
		ClientGui gui = new ClientGui(); 
    	gui.setVisible(true);
    	
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == createDrink){
			selectionPanel.setVisible(false);
			editPanel.setVisible(true);
			barbotPanel.remove(selectionPanel);
			barbotPanel.add(editPanel);
		}
		else if(e.getSource() == selectDrink){
			selectionPanel.setVisible(true);
			editPanel.setVisible(false);
			barbotPanel.add(selectionPanel);
			barbotPanel.remove(editPanel);
		}
		else if(e.getSource() == selectionAcceptButton){
			System.out.println("Making Drinks");
			int a;
			try {
				a = ArduinoComunicator.makeDrink(currentDrink, ci);
				System.out.println("Made 1 drink"+a);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else if(e.getSource() == editAcceptButton){
			Map<String,Integer> newIngredients = new HashMap<>();
			int i = 0;
			while(!ingredientNameArray[i].getText().equals("") && !amountTextArray[i].getText().equals("")){
				newIngredients.put(ingredientNameArray[i].getText(), Integer.parseInt(amountTextArray[i].getText()));
				i++;
			}
			Drink newDrink = new Drink(infoNameTextField.getText(), newIngredients);
			drinkList.getDrinkSet().add(newDrink);
			
			try {
				reInit();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else if(e.getSource() == addIngredientDataButton){
			userIngredients.add(addIngredientDataField.getText());
			try {
				saveIngredients("ingredients.txt");
				reInit();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == addIngredientDrinkButton){
			int n = 0;
			while(!ingredientNameArray[n].getText().equals("")){
				n++;
			}
			ingredientNameArray[n].setText(addIngredientDrinkBox.getSelectedItem().toString());
		}
		
		for(int i = 0; i < 12; i++){
			if(e.getSource() == buttonArray[i]){
				for(Drink d:drinkArray){
					if(d.getName() == buttonArray[i].getText()){
						currentDrink = d;
						int n = 0;
						Map<String, Integer> ingredients = d.getIngredients();
						//Set<Entry<String,Integer>> mapSet = ingredients.entrySet();
						//Iterator<Entry<String,Integer>> mapIterator = mapSet.iterator();
						//while(mapIterator.hasNext()){
							//Entry<String, Integer> mapEntry = mapIterator.next();
							//String keyValue = mapEntry.getKey();
							//Integer value = Integer.parseInt(mapEntry.getValue());
							//System.out.println(keyValue + " " + value);
						//}
						for(String s:ingredients.keySet()){
							selectIngredientNameArray[n].setText(s);
							//long value = ((long)d.getIngredients().get(s));
							//System.out.println(d.getIngredients().get(s));
							//selectAmountTextArray[n].setText("" + value);
							n++;
						}
						//for(Integer k:d.getIngredients().keySet().){
						//	selectAmountTextArray[n].setText(k.toString());
						//}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
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
	
	public void reInit() throws FileNotFoundException{
		int n = 0;
		for(Drink d:drinkArray){
			buttonArray[n].setText(d.getName());
			n++;
		}
		
		addIngredientDrinkBox.removeAllItems();
		
		for(String s:userIngredients){
			addIngredientDrinkBox.addItem(s);
		}
	}
	
}