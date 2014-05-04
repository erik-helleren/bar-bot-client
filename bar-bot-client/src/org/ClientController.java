package org;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ClientController {

	private SelectView s_view;
	private EditView e_view;
	private ConfigView c_view;
	
	private ClientModel model;
	private ScheduledExecutorService statusUpdater;
	Map<Short, String> orders;
	
	ClientController(ClientModel model, SelectView s_view, EditView e_view, ConfigView c_view){
		this.s_view = s_view;
		this.e_view = e_view;
		this.c_view = c_view;
		this.model = model;
		
		orders = new HashMap<Short, String>();
		
		s_view.addAcceptButtonListener(new AcceptButtonListener());
		s_view.addDrinkButtonListener(new DrinkButtonListener());
		s_view.addSearchKeyListener(new SearchKeyListener());
		//s_view.addWindowSwitchListener(new WindowSwitchListener());
		s_view.addSearchListListener(new SearchListListener());
		
		e_view.addAcceptButtonListener(new EditAcceptButtonListener());
		e_view.addDrinkListListener(new DrinkListListener());
		e_view.addCreateIngredientListener(new CreateIngredientListener());
		e_view.addDrinkIngredientListener(new DrinkIngredientListener());
		e_view.addRemoveButtonListener(new RemoveButtonListener());
		e_view.addRemoveIngredientMouseListener(new RemoveIngredientMouseListener());
		//e_view.addWindowSwitchListener(new WindowSwitchListener());
		//e_view.addCreateIngredientKeyListener(new CreateIngredientKeyListener());
		
		c_view.addAcceptButtonListener(new ConfigAcceptButtonListener());
		//c_view.addWindowSwitchListener(new WindowSwitchListener());
		
		statusUpdater = Executors.newSingleThreadScheduledExecutor();
		statusUpdater.scheduleAtFixedRate(new Runnable() {
			public void run() {
				JTextArea statusField = getStatusField();
				String text = "";
				try {
					byte[] status = checkStatus();
					int totalPumps = status.length;
					String[] pumpIngredients = new String[totalPumps];
					for(String ingredient: getIngredientsList()) {
						int pumpid = getPumpID(ingredient);
						if(pumpid != -1) {
							try{
								pumpIngredients[pumpid] = ingredient;
							} catch (ArrayIndexOutOfBoundsException e) {
								//e.printStackTrace();
							}
						}
					}
					if(totalPumps <= 0) {
						text += "No Pumps detected.";
					} else {
						for(int i = 0; i < totalPumps; i++) {
							String level = "";
							if(status[i] == 0) level = "Empty";
							else if (status[i] == 1) level = "Critical";
							else if (status[i] == 2) level = "Low";
							else if (status[i] == 3) level = "Medium";
							else if (status[i] == 4) level = "High";
							else if (status[i] == 5) level = "FAIL";
							else level = ""+status[i];
							if(pumpIngredients[i] != null)
								text += String.format("Pump %02d - %10s: %5s  ", i, pumpIngredients[i], level);
							else
								
								text += String.format("Pump %02d - %10s: %5s  ", i, "N/A", level);
							if(i % 3 == 2) text += '\n';
						}
					}
					if(!text.endsWith("\n")) text += '\n';
					Map<Short, String> currOrders = getOrders();
					for(short id: currOrders.keySet()) {
						status = checkDrinkStatus(id);
						String code;
						if(status[0] == 0x00) code = "Queued";
						else if (status[0] == 0x01) code = "Being Made";
						else if (status[0] == 0x02) {
							code = "Success";
							currOrders.remove(id);
						}
						else if (status[0] == 0x03) code = "Failed";
						else if (status[0] == 0x04) {
							code = "No Record";
							currOrders.remove(id);
						}
						else code = "UNKNOWN";
						text += String.format("Drink %4d (%s): %s\n", id, currOrders.get(id), code);
						
					}
					statusField.setText(text);
					statusField.repaint();
				} catch (Exception e) {
					//e.printStackTrace();
					text = "Problem retrieving Server information.\n";
					text += String.format("current Time is %d\n", System.nanoTime());
				} finally {
					statusField.setText(text);
					statusField.repaint();
				}
			}
		}, 0, 5, TimeUnit.SECONDS);
	}
	
	public byte[] checkStatus() throws Exception{
		return ArduinoComunicator.checkStatus(model.getConfigInterface());
	}
	
	public byte[] checkDrinkStatus(short id) throws Exception {
		return ArduinoComunicator.checkDrinkStatus(id, model.getConfigInterface());
	}
	
	public JTextArea getStatusField() {
		return s_view.statusTextArea;
	}
	
	public List<String> getIngredientsList() {
		return model.getIngredientsList();
	}
	
	public int getPumpID(String s) {
		return model.getPumpID(s);
	}
	
	public Map<Short, String> getOrders() {
		return orders;
	}
	
	public void reInit() throws FileNotFoundException{
		e_view.clearDrinkList();//editDrinkComboBox.removeAllItems();
		e_view.addDrinkList(new String("Make a New Drink"));//editDrinkComboBox.addItem(new String("Make a New Drink"));
		
		int n = 0;
		while(!model.getDrinkName(n).equals("")){
			e_view.addDrinkList(model.getDrinkName(n));
			s_view.setButtonArrayText(n, model.getDrinkName(n));
			n++;
		}
		e_view.clearDrinkSelection();
		e_view.clearIngredientSelection();
		
		e_view.clearIngredientComboBox();//addIngredientDrinkBox.removeAllItems();
		e_view.addIngredientComboBox(new String("                    "));//addIngredientDrinkBox.addItem(new String("                    "));
		e_view.clearIngredientList();
		
		for(String s:model.getIngredientsList()){
			e_view.addIngredientComboBox(s);//addIngredientDrinkBox.addItem(s);
			e_view.addIngredientList(s);
		}
		for(int i = 0; i < 12; i++){	
		//	ingredientConfigComboBox[i].removeAllItems();
		//	ingredientConfigComboBox[i].addItem(new String("N/A"));
		//	for(String s:userIngredients){
		//		ingredientConfigComboBox[i].addItem(s);
		//	}
		}
		c_view.reInitIngredientConfig();
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
			byte[] a;
			//poorly formed syntax
			try {
				a = ArduinoComunicator.makeDrink(newDrink, model.getConfigInterface());
				if(a[0] == 0x00) {
					short retid = (short)((a[1]<<8) + a[2]);
					orders.put(retid, newDrink.getName());
				}
				//System.out.println("Made 1 drink"+a);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Some Ingredients do not have Pumps registered with them!");
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

		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode()==KeyEvent.VK_DOWN){
				s_view.selectSearchResult(s_view.getSelectedIndex()+1);
			}
			else if(arg0.getKeyCode()==KeyEvent.VK_UP){
				s_view.selectSearchResult(s_view.getSelectedIndex()-1);
			}
		}

		public void keyReleased(KeyEvent arg0) {
			if(arg0.getKeyCode()==KeyEvent.VK_DOWN){
				//s_view.selectSearchResult(s_view.getSelectedIndex()+1);
			}
			else if(arg0.getKeyCode()==KeyEvent.VK_UP){
				//s_view.selectSearchResult(s_view.getSelectedIndex()-1);
			}
			else if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
				if(s_view.getSelectedIndex()>=0){
					for(int p = 0; p < 12; p++){
						s_view.setIngredientName(p, "");
						s_view.setAmount(p, "");
						s_view.setDrinkName("");
					}
					Drink d = model.getDrink(s_view.getSelectedName());
					if(s_view.getSelectedName().equals("") || d == null){
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
					//s_view.selectSearchResult(0);
					s_view.clearSearchResults();
					int n = 0;
					while(!model.getDrinkName(n).equals("")){
						s_view.addSearchResult(model.getDrinkName(n));
						n++;
					}
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
	
	class SearchListListener implements ListSelectionListener{

		public void valueChanged(ListSelectionEvent e) {
			for(int p = 0; p < 12; p++){
				s_view.setIngredientName(p, "");
				s_view.setAmount(p, "");
				s_view.setDrinkName("");
			}
			Drink d = model.getDrink(s_view.getSelectedName());
			if(s_view.getSelectedName().equals("") || d == null){
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
	
	class EditAcceptButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			/*
			 * The Accept button on the Create Drink page
			 * Takes all data currently displayed on the creation
			 * screen and creates a JSON drink object with it.
			 * Selection Page buttons are updated from here
			 */
			System.err.printf("\n\n\'" + e_view.getSelectedDrinkName() + "\'\n\n");
			if(e_view.getSelectedDrinkName().equals("Make a New Drink")){
				Map<String,Integer> newIngredients = new HashMap<>();
				int i = 0;
				while(!e_view.getIngredientName(i).equals("") && !e_view.getAmount(i).equals("")){
					newIngredients.put(e_view.getIngredientName(i), Integer.parseInt(e_view.getAmount(i)));
					i++;
				}
				Drink newDrink = new Drink(e_view.getDrinkName(), newIngredients);
				model.addDrink(newDrink);//drinkList.getDrinkSet().add(newDrink);
				e_view.addDrinkList(new String(e_view.getDrinkName()));
				//e_view.drinkList.repaint();
				int n = 0;
				s_view.clearSearchResults();
				while(!model.getDrinkName(n).equals("")){
					s_view.setButtonArrayText(n, model.getDrinkName(n));
					s_view.addSearchResult(model.getDrinkName(n));
					n++;
				}
				model.writeToFile();
				
			}
			else{
				Set<Drink> temp = new HashSet<>();
				Drink d = model.getDrink(e_view.getSelectedDrinkName());
				System.err.printf("\""+ e_view.getSelectedDrinkName() + "\"\n\n");
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
			
			/*try {
				reInit();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}
		
	}
	
	
	class DrinkListListener implements ListSelectionListener{

		public void actionPerformed(ActionEvent arg0) {
			if(e_view.getDrinkComboBox() != null){
				for(int i = 0; i < 12; i++){
					e_view.setIngredientName(i, "");
					e_view.setAmount(i, "");
					e_view.setDrinkName("");
				}
				if(e_view.getDrinkComboBox().toString().equals("Make a New Drink")){
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

		public void valueChanged(ListSelectionEvent arg0) {
			for(int i = 0; i < 12; i++){
				e_view.setIngredientName(i, "");
				e_view.setAmount(i, "");
				e_view.setDrinkName("");
			}
			System.err.printf("\n\n\'" + e_view.getSelectedDrinkName() + "\'\n\n");
			if(e_view.getSelectedDrinkName().equals("Make a New Drink")){
				return;
			}
			Drink d = model.getDrink(e_view.getSelectedDrinkName());
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
	
	class CreateIngredientListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			//Updates Ingredient ComboBox with addIngredientDataField
			model.getIngredientsList().add(e_view.getCreateIngredientText());//userIngredients.add(addIngredientDataField.getText());
			e_view.setCreateIngredientText("");//addIngredientDataField.setText("");
			try {
				model.saveIngredients("ingredients.txt");
				e_view.clearIngredientSelection();
				e_view.clearIngredientComboBox();
				e_view.addIngredientComboBox(new String("                    "));//addIngredientDrinkBox.addItem(new String("                    "));
				e_view.clearIngredientList();
				
				for(String s:model.getIngredientsList()){
					System.out.printf(s + "\n");
					e_view.addIngredientComboBox(s);//addIngredientDrinkBox.addItem(s);
					e_view.addIngredientList(s);
				}
				//reInit();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	/*class CreateIngredientKeyListener implements KeyListener{

		public void keyPressed(KeyEvent e) {}

		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
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
		public void keyTyped(KeyEvent arg0) {}
		
	}*/
	
	class DrinkIngredientListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			//Takes selected item from combo box, and places it in the ingredient list
			if(!e_view.getIngredientComboBox().equals("                    ")){
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
		
	}
	
	class RemoveIngredientMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(arg0.getClickCount() >= 2){
				model.getIngredientsList().remove(e_view.getSelectedIngredientName());//userIngredients.add(addIngredientDataField.getText());
				//e_view.setCreateIngredientText("");//addIngredientDataField.setText("");
				e_view.selectIngredientList(0);
				try {
					model.saveIngredients("ingredients.txt");
					e_view.clearIngredientSelection();
					e_view.clearIngredientComboBox();
					e_view.addIngredientComboBox(new String("                    "));//addIngredientDrinkBox.addItem(new String("                    "));
					e_view.clearIngredientList();
					
					for(String s:model.getIngredientsList()){
						System.out.printf(s + "\n");
						e_view.addIngredientComboBox(s);//addIngredientDrinkBox.addItem(s);
						e_view.addIngredientList(s);
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
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
			
			model.setIP(c_view.getIP());//ipConfigTextField.getText());
			int passwordInt = Integer.parseInt(c_view.getPassword());
			byte[] password = new byte[4];
			password[0] = (byte) (passwordInt >> 24);
			password[1] = (byte) (passwordInt >> 16);
			password[2] = (byte) (passwordInt >> 8);
			password[3] = (byte) (passwordInt);

			
			try {
				PrintWriter out = new PrintWriter("config");
				out.println(c_view.getIP().toString());
				System.out.println(c_view.getIP());
				out.println(c_view.getPassword());
				
				for(int i = 0; i < 12; i++){
					model.setPumpID(i,c_view.getIngredientConfig(i));//ingredientConfigComboBox[i].getSelectedItem().toString());
					out.println(c_view.getIngredientConfig(i));
				
				}
			

				out.flush();
				out.close();
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
		/*	try {
				ArduinoComunicator.submitPassword(password, model.getConfigInterface());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		*/	
			/*try {
				//model.saveConfiguration("config");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}
		
	}
	
}
