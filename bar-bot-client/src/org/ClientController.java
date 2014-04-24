package org;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class ClientController {

	private SelectView s_view;
	private EditView e_view;
	private ConfigView c_view;
	
	private ClientModel model;
	
	ClientController(ClientModel model, SelectView s_view, EditView e_view, ConfigView c_view){
		this.s_view = s_view;
		this.e_view = e_view;
		this.c_view = c_view;
		this.model = model;
		
		
		
		s_view.addAcceptButtonListener(new AcceptButtonListener());
		s_view.addDrinkButtonListener(new DrinkButtonListener());
		s_view.addSearchKeyListener(new SearchKeyListener());
		s_view.addWindowSwitchListener(new WindowSwitchListener());
		
		e_view.addAcceptButtonListener(new EditAcceptButtonListener());
		e_view.addDrinkComboBoxListener(new DrinkComboBoxListener());
		e_view.addCreateIngredientListener(new CreateIngredientListener());
		e_view.addDrinkIngredientListener(new DrinkIngredientListener());
		e_view.addRemoveButtonListener(new RemoveButtonListener());
		e_view.addWindowSwitchListener(new WindowSwitchListener());
		
		c_view.addAcceptButtonListener(new ConfigAcceptButtonListener());
		c_view.addWindowSwitchListener(new WindowSwitchListener());
	}
	
	
	public void reInit() throws FileNotFoundException{
		e_view.clearDrinkComboBox();//editDrinkComboBox.removeAllItems();
		e_view.addDrinkComboBox(new String("Make a New Drink"));//editDrinkComboBox.addItem(new String("Make a New Drink"));
		
		int n = 0;
		while(!model.getDrinkName(n).equals("")){
			e_view.addDrinkComboBox(model.getDrinkName(n));
			s_view.setButtonArrayText(n, model.getDrinkName(n));
			n++;
		}
		
		e_view.clearIngredientComboBox();//addIngredientDrinkBox.removeAllItems();
		e_view.addIngredientComboBox(new String("                    "));//addIngredientDrinkBox.addItem(new String("                    "));
		
		for(String s:model.getIngredientsList()){
			e_view.addIngredientComboBox(s);//addIngredientDrinkBox.addItem(s);
		}
		for(int i = 0; i < 12; i++){	
		//	ingredientConfigComboBox[i].removeAllItems();
		//	ingredientConfigComboBox[i].addItem(new String("N/A"));
		//	for(String s:userIngredients){
		//		ingredientConfigComboBox[i].addItem(s);
		//	}
		}
	}
	
	
	class AcceptButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			/*
			 * The Accept button on the Select page.
			 * Takes selected drink, and sends creation
			 * data to the Arduino
			 */
			Map<String,Integer> newIngredients = new HashMap<>();
			int i = 0;
			while(!s_view.getIngredientName(i).equals("") && !s_view.getAmount(i).equals("")){
				newIngredients.put(s_view.getIngredientName(i), Integer.parseInt(s_view.getAmount(i)));
				i++;
			}
			Drink newDrink = new Drink(s_view.getDrinkName(), newIngredients);
			
			System.out.println("Making Drinks");
			int a;
			try {
				a = ArduinoComunicator.makeDrink(newDrink, model.getConfigInterface());
				System.out.println("Made 1 drink"+a);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	class CancelButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//No function for this yet
		}
	}
	
	class WindowSwitchListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			if(((JMenuItem) e.getSource()).getText().equals("Select Drink")){
				s_view.setVisible(true);
				e_view.setVisible(false);
				c_view.setVisible(false);
			}
			else if(((JMenuItem) e.getSource()).getText().equals("Create Drink")){
				s_view.setVisible(false);
				e_view.setVisible(true);
				c_view.setVisible(false);
			}
			else if(((JMenuItem) e.getSource()).getText().equals("Config")){
				s_view.setVisible(false);
				e_view.setVisible(false);
				c_view.setVisible(true);
			}
		}
		
	}
	
	class DrinkButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < 12; i++){
				if(((JButton) e.getSource()).getText().equals(s_view.getButtonArrayText(i))){
					for(int p = 0; p < 12; p++){
						s_view.setIngredientName(p, "");
						s_view.setAmount(p, "");
						s_view.setDrinkName("");
					}
					Drink d = model.getDrink(s_view.getButtonArrayText(i));
					if(d == null){
						return;
					}
					s_view.setDrinkName(d.getName());
					
					int n = 0;
					Map<String, Integer> ingredients = d.getIngredients();
					for(String s:ingredients.keySet()){
						s_view.setIngredientName(n, s);
						s_view.setAmount(n, "" + ingredients.get(s));
						n++;
					}
				}
			}
		}
	}
	
	class SearchKeyListener implements KeyListener{

		public void keyPressed(KeyEvent arg0) {}

		public void keyReleased(KeyEvent arg0) {
			if(arg0.getKeyCode()==KeyEvent.VK_DOWN){
					s_view.selectSearchResult(s_view.getSelectedIndex()+1);
			}
			else if(arg0.getKeyCode()==KeyEvent.VK_UP){
				s_view.selectSearchResult(s_view.getSelectedIndex()-1);
			}
			else if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
				if(s_view.getSelectedIndex()>=0){
					for(int p = 0; p < 12; p++){
						s_view.setIngredientName(p, "");
						s_view.setAmount(p, "");
						s_view.setDrinkName("");
					}
					Drink d = model.getDrink(s_view.getSelectedName());
					if(d == null){
						return;
					}
					s_view.setDrinkName(d.getName());
					
					int n = 0;
					Map<String, Integer> ingredients = d.getIngredients();
					for(String s:ingredients.keySet()){
						s_view.setIngredientName(n, s);
						s_view.setAmount(n, "" + ingredients.get(s));
						n++;
					}
				}
			}
			else{
				if(s_view.getSearchText().equals("")){
					s_view.clearSearchResults();
					return;
				}
				s_view.clearSearchResults();
				if(s_view.getSearchType()){
					List<String> d = model.getDrinkSubstring(s_view.getSearchText());
					for(String s:d){
						s_view.addSearchResult(s);
					}
				}
				else{
					List<String> d = model.getIngredientSubstring(s_view.getSearchText());
					for(String s:d){
						s_view.addSearchResult(s);
					}
				}
			}
			
		}

		public void keyTyped(KeyEvent arg0) {}
		
	}
	
	class EditAcceptButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			/*
			 * The Accept button on the Create Drink page
			 * Takes all data currently displayed on the creation
			 * screen and creates a JSON drink object with it.
			 * Selection Page buttons are updated from here
			 */
			
			if(e_view.getDrinkComboBox().toString().equals("Make a New Drink")){
				Map<String,Integer> newIngredients = new HashMap<>();
				int i = 0;
				while(!e_view.getIngredientName(i).equals("") && !e_view.getAmount(i).equals("")){
					newIngredients.put(e_view.getIngredientName(i), Integer.parseInt(e_view.getAmount(i)));
					i++;
				}
				Drink newDrink = new Drink(e_view.getDrinkName(), newIngredients);
				model.addDrink(newDrink);//drinkList.getDrinkSet().add(newDrink);
				model.writeToFile();
				
			}
			else{
				Set<Drink> temp = new HashSet<>();
				Drink d = model.getDrink(e_view.getDrinkComboBox().toString());
				if(d != null){
					temp.add(d);
					//d.getIngredients().clear();
					Map<String,Integer> newIngredients = new HashMap<>();
					int i = 0;
					while(!e_view.getIngredientName(i).equals("") && !e_view.getAmount(i).equals("")){
						newIngredients.put(e_view.getIngredientName(i), Integer.parseInt(e_view.getAmount(i)));
						i++;
					}
					Drink newDrink = new Drink(e_view.getDrinkName(), newIngredients);
					model.removeDrink(d);//drinkList.getDrinkSet().remove(d);
					model.addDrink(newDrink);//drinkList.getDrinkSet().add(newDrink);
					model.writeToFile();//drinkList.writeToFile("DrinkDatabase");
					//break;
					
				}
				else if(d.getName().equals(e_view.getDrinkName())){//infoNameTextField.getText())){
					JOptionPane.showMessageDialog(e_view, "Eggs are not supposed to be green.");
					return;
				}
				
				//drinkArray.remove(temp);
			}
			
			try {
				reInit();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	
	class DrinkComboBoxListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			if(e_view.getDrinkComboBox() != null){
				if(e_view.getDrinkComboBox().toString().equals("Make a New Drink")){
					for(int i = 0; i < 12; i++){
						e_view.setIngredientName(i, "");
						e_view.setAmount(i, "");
						e_view.setDrinkName("");
					}
					return;
				}
				Drink d = model.getDrink(e_view.getDrinkComboBox().toString());
				if(d == null){
					return;
				}
				e_view.setDrinkName(d.getName());
				
				int n = 0;
				Map<String, Integer> ingredients = d.getIngredients();
				for(String s:ingredients.keySet()){
					e_view.setIngredientName(n, s);
					e_view.setAmount(n, "" + ingredients.get(s));
					n++;
				}
			}
		}
		
	}
	
	class CreateIngredientListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			//Updates Ingredient ComboBox with addIngredientDataField
			model.getIngredientsList().add(e_view.getCreateIngredientText());//userIngredients.add(addIngredientDataField.getText());
			e_view.setCreateIngredientText("");//addIngredientDataField.setText("");
			try {
				model.saveIngredients("ingredients.txt");
				reInit();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	class DrinkIngredientListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			//Takes selected item from combo box, and places it in the ingredient list
			int n = 0;
			while(n < 12 && !e_view.getIngredientName(n).equals("")){//ingredientNameArray[n].getText().equals("")){
				n++;
			}
			if(n < 12){
				e_view.setIngredientName(n, e_view.getIngredientComboBox().toString());//ingredientNameArray[n].setText(addIngredientDrinkBox.getSelectedItem().toString());
				//ingredientNameArray[n].setVisible(true);
				//amountTextArray[n].setVisible(true);
				//removeIngredientArray[n].setVisible(true);
				//ingredientPanelArray[n].setVisible(true);
			}
			
			if(n >= 11){
				e_view.enableAddIngredientButton(false);//addIngredientDrinkButton.setEnabled(false);
			}
		}
		
	}
	
	class RemoveButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			int i;
			for(i = 0; i < 12; i++){
				if(e.getSource() == e_view.getRemoveButton(i)){
					if(!e_view.getEnabledAddIngredientButton()){
						e_view.enableAddIngredientButton(true);
					}
					int m = i;
					while(m < 11 && !e_view.getIngredientName(m+1).equals("")){//ingredientNameArray[m+1].getText().equals("")){
						e_view.setIngredientName(m, e_view.getIngredientName(m+1));//ingredientNameArray[m].setText(ingredientNameArray[m+1].getText());
						e_view.setAmount(m, e_view.getAmount(m+1));//amountTextArray[m].setText(amountTextArray[m+1].getText());
						m++;
					}
					e_view.setIngredientName(m, "");//ingredientNameArray[m].setText("");
					e_view.setAmount(m, "");//amountTextArray[m].setText("");
					//ingredientNameArray[m].setVisible(false);
					//amountTextArray[m].setVisible(false);
					//removeIngredientArray[m].setVisible(false);
					//ingredientPanelArray[m].setVisible(false);
				}
			}
			
		}
		
	}
	
	class ConfigAcceptButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			//System.out.println("HI");
			model.setIP(c_view.getIP());//ipConfigTextField.getText());
			for(int i = 0; i < 12; i++){
				model.setPumpID(i,c_view.getIngredientConfig(i));//ingredientConfigComboBox[i].getSelectedItem().toString());
			}
		//	try {
		//		saveConfiguration("config.txt");
		//	} catch (FileNotFoundException e1) {
		//		// TODO Auto-generated catch block
		//		e1.printStackTrace();
		//	}
		}
		
	}
	
}
