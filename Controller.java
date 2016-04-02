import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Controller to build connection using views and the model. 
 * Implement actionlisteners for the views and make changes to the database
 * @author Ziyu Huang, Zhining Qi, Tri Ninh
 *
 */
public class Controller 
{
	PersonView pview;
	Model model;
	View view;
	DeletePersonView dpview;
	UpdatePerson upview;
	UpdateCarView ucview;
	ViolationView vview;
	CarView cview;
	DeleteCarView dcview;
	UpdateViolationView uvview;
	DeleteViolationView dvview;

	public Controller(Model model, View view, PersonView pview, 
			DeletePersonView dpview, UpdatePerson upview,
			ViolationView vview, CarView cview, UpdateCarView ucview,
			DeleteCarView dcview, UpdateViolationView uvview,
			DeleteViolationView dvview)
	{
		this.model = model;
		this.view = view;
		this.pview = pview;
		this.dpview = dpview;
		this.upview = upview;
		this.vview = vview;
		this.cview = cview;
		this.ucview = ucview;
		this.dcview = dcview;
		this.uvview = uvview;
		this.dvview = dvview;
		
		//actionListeners for user search
		view.addPlateSearchListener(new PlateSearchEL());
		view.addPcnSearchListener(new PcnSearchEL());
		view.addDLicenseSearchListener(new DLicenseSearchEL());
		
		//actionListeners for insertion
		view.insertPersonListener(new AddPersonEL());
		view.insertViolationListener(new AddViolationEL());
		view.insertCarListener(new AddCarEL());
		
		//actionListeners for deletion
		view.deletePersonListener(new PersonDelete());
		view.deleteCarListener(new DeleteCarEL());
		view.deleteViolationListener(new ViolationDelete());
		
		//actionListeners for update
		view.updatePersonListener(new UpdatePersonEL());
		view.updateCarListener(new UpdateCarEL());
		view.updateViolationListener(new UpdateViolationEL());
		
		//actionListeners for choosing queries
		view.jcomboboxListener(new ChoiceEL());
		
		//show table actionListeners
		view.showPersonTableListener(new ShowPersonEL());
		view.showCarTableListener(new ShowCarEL());
		view.showViolationListener(new ShowViolationEL());
		view.showUnpaidListener(new ShowUnpaidEL());
		
		//pop up frames' button actionListeners
		pview.addSubmitListener(new SubmitEL());
		dpview.addDeletePersonListener(new DeletePersonEL());
		dvview.addDeleteViolationListener(new DeleteViolationEL());
		upview.addUpdatePersonListener(new addUpdatePersonEL());
		vview.addSubmitIVListener(new SubmitIVEL());
		cview.addSubmitListener(new SubmitC());
		ucview.addUpdateCarListener(new addUpdateCarEL());
		dcview.addDeleteCarListener(new SubmitDeleteCarEL());
		uvview.addSearchViolationAmountListener(new addSearchViolationEL());
		uvview.addUpdateViolationAmountListener(new addUpdateViolationEL());
	}
	
	class ShowUnpaidEL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String query = "select * from unpaid";
			model.TableModel = model.SearchByQuery(query);
			setMessageTable(model.TableModel);
		}
	}
	
	class ShowPersonEL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String query = "select * from person";
			model.TableModel = model.SearchByQuery(query);
			setMessageTable(model.TableModel);
		}
	}
	
	class ShowCarEL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String query = "select * from car";
			model.TableModel = model.SearchByQuery(query);
			setMessageTable(model.TableModel);
		}
	}
	
	class ShowViolationEL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String query = "select * from violation";
			model.TableModel = model.SearchByQuery(query);
			setMessageTable(model.TableModel);
		}
	}
	
	
	class ChoiceEL implements ActionListener
	{
		String query = "";
		@Override
		public void actionPerformed(ActionEvent e) {
			String choice = (String) view.choices.getSelectedItem();
			switch(choice)
			{
			case "":
				break;
			case "[1] Total numbers of current year violation":
				query = "select * from violation  where year(issued) =  year(current_timestamp)";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
			
			case "[2] Violation not paid over 1 year":
				query = "select * from violation where datediff(current_timestamp, issued)>365 and paid = 0";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
			
			case "[3] frequency of violation contravention":
				query = "select contravention as Contravention, count(*) as Numbers_Violation from violation group by Contravention order by Numbers_Violation desc";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
				
			case "[4] Owns more than 2 cars and has no violation":
				query = "select name as NAME, dl as DriverLicense from"
						+ " person where dl in (select dlicense from car "
						+ "c1 where exists (select * from car c2 where c2.dlicense "
						+ "= c1.dlicense and c1.lp<>c2.lp)) and"
						+ " dl not in (select udl from unpaid)";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
				
			case "[5] Average fine":
				query = "select avg(fine) as Average_Fine from violation";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
				
			case "[6] The number of violation committed by different age":
				query = "select  timestampdiff(YEAR,dob, current_timestamp) as Age,count(*) "
						+ "as CountViolations from person where dl "
						+ "in (select dlicense from car where lp in "
						+ "(select lplate from violation) ) group by age";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
				
			case "[7] Make of the car @ central @ 2013,2014":
				query = "select year(issued) as year, make,  "
						+ "location from car, violation where car.lp "
						+ "= violation.lplate and violation.location "
						+ "like '%Central' group by year";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
				
			case "[8] The number of violation in recent three years":
				query = "select year(issued) as Year, count(*) as Numbers_Violation "
						+ "from violation group by Year order  by Year desc  limit 3";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
				
			case "[9] A color more than 15 cars share":
				query = "select color, count(*) as Numbers_Cars from car group by color having count(color) > 15";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
				
			case "[10] Years have maximum and minimum number of violation":
				query = "select able.c, able.b  from (select year(issued) as c,"
						+ " count(*) as b from violation group by c) able where "
						+ "able.b >= all(select m.b from (select year(issued) as c, "
						+ "count(*) as b from violation group by c) m ) or"
						+ " able.b <= all(select m.b from (select year(issued) as c, "
						+ "count(*) as b from violation group by c) m )";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
			
			case "[11] Find the person who violated parking at least twice":
				query = "select name, dl, count(pcn) from person, violation,"
						+ " car where dl = dlicense and"
						+ " lp = lplate group by dl having count(pcn) >1";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
		
			case "[12] Top 5 Make of cars more likely to violate parking":
				query = "select make, count(*) as occurence from car,"
						+ "violation where lp=lplate group by make"
						+ " order by count(*) desc limit 5";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
			
			case "[13] Find out persons who have 2 or 2 more unpaid tickets":
				query = "select name, udl, count(udl) from unpaid, "
						+ "person where udl=dl group by udl having count(udl)>1;";
				model.TableModel = model.SearchByQuery(query);
				setMessageTable(model.TableModel);
				break;
				
			default:
				break;
			}
		}
	}
	
	class DeleteViolationEL implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String pcn = dvview.textfield.getText();
			pcn.toUpperCase();
			
			String message;
			String searchTarget = "select pcn from violation where pcn = '"+pcn+"'";
			model.TableModel = model.SearchByQuery(searchTarget);
			if(model.TableModel.getRowCount() == 0)
			{
				message = pcn + " is not in the database";
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String query = "delete from violation where pcn = '"+pcn+"'";
			model.executeQuery(query);;
			model.TableModel.fireTableDataChanged();
			message = pcn + " is deleted from the database";
			JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
			dvview.setVisible(false);
			dvview.dispose();
		}
	}
	
	class ViolationDelete implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			dvview.setLocationRelativeTo(null);
			dvview.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dvview.pack();
			dvview.setVisible(true);
		}
	}
	
	class addUpdateViolationEL implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = "";
			String pcn = uvview.pcn.getText().trim();
			String amountToPay = uvview.amount.getText();

			if(pcn == "")
			{
				message = "Pcn Field can not be empty, Please Check it again.";
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String balance = uvview.textarea.getText();			
			
			if(balance.length() == 0)
			{
				message = "Wrong PCN number, Please Check it again.";
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else
			{
				if(amountToPay == "")
				{
					message = "Amount Field can not be empty, Please Check it again.";
					JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.ERROR_MESSAGE);
					return;
				}
				float amountPaid = Float.parseFloat(amountToPay);
				balance = balance.substring(balance.lastIndexOf("$") + 1, balance.length());
				float amount = Float.parseFloat(balance);
				float remain = amount - amountPaid;
				if(remain < 0)
				{
					message = "Please check the amount.";
					JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String query = "update violation set balance = '"+remain+"' where pcn = '"+pcn+"'";
				model.executeQuery(query);
				if(remain == 0)
				{
					query = "update violation set paid = 1 where pcn = '"+pcn+"'";
					model.executeQuery(query);
				}
				model.TableModel.fireTableDataChanged();
				message = "Balance for " + pcn + " has been updated";
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
				uvview.setVisible(false);
				ucview.dispose();
			}
			
			
		}
	}
	
	class addSearchViolationEL implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = "";
			String searchTarget = "";
			String pcn = uvview.pcn.getText().toUpperCase().trim();
			
			searchTarget = "select balance from violation where pcn = '"+pcn+"'";
			model.TableModel = model.SearchByQuery(searchTarget);
			if(model.TableModel.getRowCount() == 0){
				message = pcn + " is not in the database";
				uvview.textarea.setText("");
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			float s =  (Float) model.TableModel.getValueAt(0, 0);
			uvview.textarea.setText("Amount Due: $" + s);	
		}
	}
	
	class UpdateViolationEL implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			uvview.setLocationRelativeTo(null);
			uvview.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			uvview.setSize(500, 300);
			uvview.setVisible(true);
		}
	}
	
	class SubmitDeleteCarEL implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			String lp = dcview.textfield.getText().toUpperCase();
			
			String message;
			String searchTarget = "select lp from car where lp = '"+lp+"'";
			model.TableModel = model.SearchByQuery(searchTarget);
			if(model.TableModel.getRowCount() == 0)
			{
				message = lp + " is not in the database";
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String query = "delete from car where lp = '"+lp+"'";
			model.executeQuery(query);
			model.TableModel.fireTableDataChanged();
			message = lp + " has been deleted from the database";
			JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
			dcview.setVisible(false);
			dcview.dispose();
		}	
	}
	
	class DeleteCarEL implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			dcview.setLocationRelativeTo(null);
			dcview.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dcview.setSize(500, 300);
			dcview.setVisible(true);
		}
	}
	
	class addUpdateCarEL implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String message = "";
			String searchTarget = "";
			String olp = ucview.oldPlate.getText().toUpperCase();
			String nlp = ucview.newPlate.getText().toUpperCase();
			
			searchTarget = "select lp from car where lp = '"+olp+"'";
			model.TableModel = model.SearchByQuery(searchTarget);
			if(model.TableModel.getRowCount() == 0)
			{
				message = olp + " is not in the database";
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			else
			{
				searchTarget = "select lp from car where lp = '"+nlp+"'";
				model.TableModel = model.SearchByQuery(searchTarget);
				if(model.TableModel.getRowCount() > 0)
				{
					message = nlp + " is already in the system, please choose another license plate number";
					JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}
			String query = "update car set lp = '"+nlp+"' where lp = '"+olp+"'";
			model.executeQuery(query);
			model.TableModel.fireTableDataChanged();
			message = olp + " has been updated to " + nlp;
			JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
			ucview.setVisible(false);
			ucview.dispose();
		}
	}
	
	class UpdateCarEL implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			ucview.setLocationRelativeTo(null);
			ucview.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			ucview.setSize(500, 300);
			ucview.setVisible(true);
		}
	}
	
	
	class SubmitC implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String lp = cview.licensePlate.getText();
			String dl = cview.driverLicense.getText();
			int year = Integer.parseInt(cview.year.getText());
			String make = cview.make.getText();
			String cmodel = cview.model.getText();
			String color = cview.color.getText();
			
			String message = "";
			String searchTarget = "select dl from person where dl = '"+dl+"'";
			model.TableModel = model.SearchByQuery(searchTarget);
			if(model.TableModel.getRowCount() == 0)
			{
				message = dl + " is not in the database";
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else
			{
				model.addCar(lp, dl, year, make, cmodel, color);
				message = "Data has been inserted into the database sucessfully!";
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
			}
			cview.setVisible(false);
			cview.dispose();
		}
	}
	
	class AddCarEL implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			cview.setLocationRelativeTo(null);
			cview.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			cview.setSize(500, 300);
			cview.setVisible(true);
		}
	}
	
	class UpdatePersonEL implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			upview.setLocationRelativeTo(null);
			upview.setSize(300,300);
			upview.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			upview.setVisible(true);
		}
	}
	
	class addUpdatePersonEL implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			String message;
			String oldName = upview.oldNameFd.getText().toUpperCase();
			String newName = upview.newNameFd.getText().toUpperCase();

			String searchTarget = "select name from person where name = '"+oldName+"'";
			model.TableModel = model.SearchByQuery(searchTarget);
			if(model.TableModel.getRowCount() == 0)
			{
				message = oldName + " is not in the database";
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String query = "update person set name = '"+newName+"' where name = '"+oldName+"'";
			model.executeQuery(query);
			model.TableModel.fireTableDataChanged();
			message = oldName + " has been update to " + newName;
			JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
			upview.setVisible(false);
			upview.dispose();
		}
		
	}
	
	//Zhining
		class SubmitIVEL implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				//1.pcn
				String pcn = vview.pcn.getText();
				//2.issuedate 
				String issuedate = vview.issuedate.getText();
				DateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
				try{
					Date date = (Date)formatter.parse(issuedate);
				}catch (ParseException e1) {
					e1.printStackTrace();
				}
				//3.location
				String location = vview.location.getText();
				//4.cont
				String contravention = vview.contravention.getText();
				//5.charge
				String chargelevel = new String();
				if(vview.high.isSelected())	chargelevel="H";
				if(vview.low.isSelected())	chargelevel="L";
				//6.fine
				String strFine = vview.fine.getText();
				float fine=Float.parseFloat(strFine);
				//7.paid?
				int paid = -1;
				if(vview.no.isSelected()) paid=0;
				if(vview.yes.isSelected()) paid=1;
				//8.lplate
				String lplate = vview.lplate.getText();
				//9.balance
				String strBalance = vview.fine.getText();
				float balance=Float.parseFloat(strBalance);
				
				String message;
				String searchTarget = "select lp from car where lp = '"+lplate+"'";
				model.TableModel = model.SearchByQuery(searchTarget);
				if(model.TableModel.getRowCount() == 0)
				{
					message = lplate + " of the car is not registered";
					JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				model.addViolation(pcn, issuedate,location, contravention,chargelevel,fine,paid,lplate,balance);
				model.TableModel.fireTableDataChanged();
				view.scrollPane.repaint();
				message = "Insert Violation Successfully";
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
				vview.setVisible(false);
				vview.dispose();
			}
		}

		//Zhining
		class AddViolationEL implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e){
				
				vview.setLocationRelativeTo(null);
				vview.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				vview.setSize(500, 300);
				vview.setVisible(true);
			}
			
		}
		
	class DeletePersonEL implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String dl = dpview.textfield.getText().toUpperCase();
			
			String message;
			String searchTarget = "select dl from person where dl = '"+dl+"'";
			model.TableModel = model.SearchByQuery(searchTarget);
			if(model.TableModel.getRowCount() == 0)
			{
				message = dl + " is not in the database";
				JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String query = "delete from person where dl = '"+dl+"'";
			model.executeQuery(query);
			model.TableModel.fireTableDataChanged();
			message = dl + " has been deleted from the database";
			JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.INFORMATION_MESSAGE);
			dpview.setVisible(false);
			dpview.dispose();
		}
		
	}
	class SubmitEL implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			String dl = pview.driverLicense.getText().toUpperCase();
			String name = pview.name.getText();
			String dob = pview.dob.getText();
			DateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
			try {
				Date date = (Date)formatter.parse(dob);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			String gender = new String();
			if(pview.male.isSelected()) gender = "M";
			if(pview.female.isSelected()) gender = "F";
			model.addUser(dl, name, gender, dob);
			//model.TableModel.fireTableDataChanged();
			view.scrollPane.repaint();
			JOptionPane.showMessageDialog(null, "Insert Successfully", "Result", JOptionPane.INFORMATION_MESSAGE);
			pview.setVisible(false);
			pview.dispose();
		}
	}
	class PersonDelete implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			dpview.setLocationRelativeTo(null);
			dpview.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dpview.pack();
			dpview.setVisible(true);
		}
	}
	
	class AddPersonEL implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			pview.setLocationRelativeTo(null);
			pview.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			pview.setSize(500, 300);
			pview.setVisible(true);	
		}
	}
	
	class PlateSearchEL implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			String searchParam = view.SearchField.getText();
			searchParam.toUpperCase();
			searchParam.trim();
			
			String query = "select pcn, name, dl, lp, balance from violation inner join car on"
			+ " lplate = lp inner join person on"
			+ " dlicense = dl where paid = 0  and lp = '"+searchParam+"'" ;
			model.TableModel = model.SearchByQuery(query);
			if(searchParam.equals(""))
				JOptionPane.showMessageDialog(null, "LicensePlate field CAN'T be empty!", "Error", JOptionPane.ERROR_MESSAGE);
			else if(model.TableModel.getRowCount() == 0)
				JOptionPane.showMessageDialog(null, "No Record", "Result", JOptionPane.INFORMATION_MESSAGE);
			else
			{
				int rowCount = model.TableModel.getRowCount();
				String message = rowCount + " Record found";
				JOptionPane.showMessageDialog(null,message , "Result", JOptionPane.INFORMATION_MESSAGE);
			}
			view.setTable(model.TableModel);
			model.TableModel.fireTableDataChanged();
			view.scrollPane.repaint();
		}
	}
	
	class PcnSearchEL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String pcn = view.SearchField.getText();
			pcn.toUpperCase();
			
			String query = "select pcn, name, dl, balance from violation inner join car on"
					+ " lplate = lp inner join person on"
					+ " dlicense = dl where paid = 0 and pcn = '"+pcn+"'" ;
			model.TableModel = model.SearchByQuery(query);
			if(pcn.length() != 10){
				JOptionPane.showMessageDialog(null, "Wrong Pcn number", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else if(model.TableModel.getRowCount() == 0)
			{
				JOptionPane.showMessageDialog(null, "No Record", "Result", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				int rowCount = model.TableModel.getRowCount();
				String message = rowCount + " Record found";
				JOptionPane.showMessageDialog(null,message , "Result", JOptionPane.INFORMATION_MESSAGE);
			}
			view.setTable(model.TableModel);
			model.TableModel.fireTableDataChanged();
			view.scrollPane.repaint();
		}
	}
	
	class DLicenseSearchEL implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			String DLicense = view.SearchField.getText();
			DLicense.toUpperCase();
			
			String query = "select dl, name, lp, location, issued, balance from person, car, violation where dl=dlicense and "
					+ " lp=lplate and paid = 0 and dl = '"+DLicense+"' ";
			model.TableModel = model.SearchByQuery(query);
			if(DLicense.equals(""))
				JOptionPane.showMessageDialog(null, "Invalid Driver License", "Error", JOptionPane.ERROR_MESSAGE);
			else if(model.TableModel.getRowCount() == 0)
			{
				JOptionPane.showMessageDialog(null, "No Record", "Result", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				int rowCount = model.TableModel.getRowCount();
				String message = rowCount + " Record found";
				JOptionPane.showMessageDialog(null,message , "Result", JOptionPane.INFORMATION_MESSAGE);
			}
				
			view.setTable(model.TableModel);
			model.TableModel.fireTableDataChanged();
			view.scrollPane.repaint();
		}
	}
	
	/**
	 * check if the table model is empty or not
	 * Set corresponding messages dialog
	 * @param tableModel
	 */
	public void setMessageTable(DefaultTableModel tableModel)
	{
		if(tableModel.getRowCount() == 0)
		{
			JOptionPane.showMessageDialog(null, "No Record", "Result", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			int rowCount = tableModel.getRowCount();
			String message = rowCount + " Record found";
			JOptionPane.showMessageDialog(null,message , "Result", JOptionPane.INFORMATION_MESSAGE);
		}
		view.setAdminSearchTable(tableModel);
		tableModel.fireTableDataChanged();
		view.scrollPane.repaint();
	}
}
