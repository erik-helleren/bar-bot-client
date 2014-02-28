import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;



public class ClientGui extends JFrame implements ActionListener{
	
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
	
	/*
	 * Edit Panels, Buttons, etc.
	 */
	JButton editAcceptButton;
	JButton editResetButton;
	
	
	
	ClientGui(){
		setTitle("Bar-Bot");
		setSize(1000, 700);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		setBackground(Color.gray);
		//setResizable(false);

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
		selectionAcceptPanel.add(selectionAcceptButton, BorderLayout.EAST);
		
		selectionResetButton = new JButton("Reset");
		selectionAcceptPanel.add(selectionResetButton, BorderLayout.WEST);
		
		/*
		 * Panel for Drink Grid
		 */
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(4,3,3,3));
		selectionPanel.add(gridPanel, BorderLayout.CENTER);
		
		JButton buttonArray[] = new JButton[12];
		for (int i = 0; i < 12; i++){
			buttonArray[i] = new JButton("" + i);
			gridPanel.add(buttonArray[i]);
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
		selectNameHeaderLabel.setPreferredSize(new Dimension(225,20));
		JLabel selectSizeHeaderLabel = new JLabel("Size (mL)");
		JPanel selectSizeHeaderPanel = new JPanel();
		selectSizeHeaderPanel.add(selectSizeHeaderLabel);
		
		selectIngredientPanelArray[12] = new JPanel(new BorderLayout());
		selectIngredientPanelArray[12].add(selectNameHeaderLabel, BorderLayout.WEST);
		selectIngredientPanelArray[12].add(selectSizeHeaderPanel, BorderLayout.CENTER);
		selectIngredientPanel.add(selectIngredientPanelArray[12]);
		
		
		/*
		 * Handles the Labels, textfields, and remove buttons
		 * in the Drink creation field
		 */
		JPanel selectIngredientNamePanel[] = new JPanel[12];
		JTextField selectIngredientNameArray[] = new JTextField[12];
		JPanel selectTextPanelArray[] = new JPanel[12];
		JTextField selectAmountTextArray[] = new JTextField[12];
		JPanel selectRemovePanelArray[] = new JPanel[12];
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
		addIngredientPanel.setPreferredSize(new Dimension(400, MAXIMIZED_VERT));
		
		/*
		 * Contains items for adding ingredients
		 * to the drink creator
		 */
		JPanel addIngredientDrinkPanel = new JPanel();
		addIngredientDrinkPanel.setBorder(BorderFactory.createTitledBorder("Add Ingredient to Drink"));
		
		JComboBox addIngredientDrinkBox = new JComboBox();
		addIngredientDrinkBox.addItem(new String("Hello World"));
		addIngredientDrinkPanel.add(addIngredientDrinkBox);
		JButton addIngredientDrinkButton = new JButton("Accept");
		addIngredientDrinkPanel.add(addIngredientDrinkButton);
		addIngredientPanel.add(addIngredientDrinkPanel);
		
		/*
		 * Contains items for adding ingredients
		 * to the database
		 */
		JPanel addIngredientDataPanel = new JPanel();
		addIngredientDataPanel.setBorder(BorderFactory.createTitledBorder("Add Ingredient to Database"));
		
		JTextField addIngredientDataField = new JTextField(20);
		addIngredientDataPanel.add(addIngredientDataField);
		JButton addIngredientDataButton = new JButton("Accept");
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
		JTextField infoNameTextField = new JTextField(20);
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
		JTextField ingredientNameArray[] = new JTextField[12];
		JPanel textPanelArray[] = new JPanel[12];
		JTextField amountTextArray[] = new JTextField[12];
		JPanel removePanelArray[] = new JPanel[12];
		JButton removeIngredientArray[] = new JButton[12];
		
		for (int i = 0; i < 12; i++){
			
			ingredientPanelArray[i] = new JPanel(new BorderLayout());
			ingredientNamePanel[i] = new JPanel();
			ingredientNameArray[i] = new JTextField(20);
			ingredientNameArray[i].setEditable(false);
			ingredientNamePanel[i].add(ingredientNameArray[i]);
			
			amountTextArray[i] = new JTextField(3);
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
		
		
		
		
		
		
		/*if(fileimage == null){
			label = new JLabel();
		}
		else{
			ImageIcon image = new ImageIcon(fileimage);
			label = new JLabel(image);
		}*/

		//dot = new ImageIcon("yellowsquare.jpg");
		//dot2 = new ImageIcon("selectedsquare.jpg");
		
		//scrollPane = new JScrollPane();
		//scrollPane.getViewport().add(label);
		//panel.add(scrollPane, BorderLayout.CENTER);
		
		//scrollPane.getVerticalScrollBar().setUnitIncrement(10);
	    //scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
		
		/*file = new JMenu("File");
		open = new JMenuItem("Open");
		open.addActionListener(this);
		newFile = new JMenuItem("New File");
		newFile.addActionListener(this);
		
		file.add(newFile);
		file.add(open);
		
		viewer = new JMenu("Map Viewer");
		find = new JMenuItem("Find");
		find.addActionListener(this); 
		directions = new JMenuItem("Directions");
		directions.addActionListener(this);
		
		viewer.add(find);
		viewer.add(directions);
		
		menuBar = new JMenuBar();
		menuBar.add(file);
		menuBar.add(viewer);
		setJMenuBar(menuBar);
		*/
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//JFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
    	
		ClientGui gui = new ClientGui(); 
    	gui.setVisible(true);
    	//idCounter = 0;
    	//pathCounter = 0;
    	//locationArray = new ArrayList<LocationNode>();
    	//pathArray = new ArrayList<PathNode>();
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
	} 
	
}