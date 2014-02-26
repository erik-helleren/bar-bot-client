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
	JMenuItem addDrink;
	JMenuItem selectDrink;
	
	/*
	 * These are the panels for Drink Selection and 
	 * for Editing Drinks
	 */
	JPanel selectionPanel;
	JPanel editPanel;
	
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
		 */
		
		JPanel acceptPanel = new JPanel();
		acceptPanel.setLayout(new BorderLayout());
		editPanel.add(acceptPanel, BorderLayout.SOUTH);
		
		editAcceptButton = new JButton("Accept");
		acceptPanel.add(editAcceptButton, BorderLayout.EAST);
		
		editResetButton = new JButton("Reset");
		acceptPanel.add(editResetButton, BorderLayout.WEST);
		
		/*
		 * Panel for Drink Grid
		 */
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(4,3));
		editPanel.add(gridPanel, BorderLayout.CENTER);
		
		JButton buttonArray[] = new JButton[12];
		for (int i = 0; i < 12; i++){
			buttonArray[i] = new JButton("" + i);
			gridPanel.add(buttonArray[i]);
		}
		
		
		/*
		 * Information Panel that resides on the right side of the screen
		 * Contains the currently selected DRINK NAME, INGREDIENTS, and DESCRIPTION 
		 */
		JPanel informationPanel = new JPanel();
		informationPanel.setLayout(new BorderLayout());
		informationPanel.setBackground(Color.WHITE);
		editPanel.add(informationPanel, BorderLayout.EAST);
		
		JTextField infoNameTextField = new JTextField("NAME GOES HERE");
		informationPanel.add(infoNameTextField, BorderLayout.NORTH);
		
		JLabel infoIngredientLabel = new JLabel("INGREDIENT LIST");
		informationPanel.add(infoIngredientLabel, BorderLayout.CENTER);
		
		JTextArea infoDescriptionTextArea = new JTextArea("DESCRIPTION");
		infoDescriptionTextArea.setRows(5);
		informationPanel.add(infoDescriptionTextArea, BorderLayout.SOUTH);
		
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
		
		addDrink = new JMenuItem("Add Drink");
		addDrink.addActionListener(this);
		edit.add(addDrink);
		
		
		
		
		
		
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
		if(e.getSource() == addDrink){
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