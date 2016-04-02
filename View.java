import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

/**
 * View class to display the frames
 * @author Ziyu Huang, Zhining Qi, Tri Ninh
 *
 */
public class View extends JFrame
{
	//AdminSearch Tab
	JPanel AdminSearch;
	JPanel upper;
	JPanel display;
	JPanel lower;
	JButton person;
	JButton car;
	JButton violation; 
	JButton unpaid;
	
	JComboBox<String> choices;
	JLabel label;
    
//  UserSearch Tab 
    JPanel UserSearch;
    JTable itemTable;
    JScrollPane scrollPane, scrollPane2;
    JTextField SearchField;
    JButton resetSearchTable;
    JButton plateSearch;
    JButton pcnSearch;
    JButton dlicenseSearch;
    JPanel topPanel;
    JPanel searchButtonPanel;
    JPanel searchPanel;
    
    JTabbedPane tabbedPane;

// AdminModify Tab 
    JPanel AdminModify;
    JPanel modify;
    JButton insertPerson, updatePerson, deletePerson;
    JButton insertCar, updateCar, deleteCar;
    JButton insertViolation, updateViolation, deleteViolation;
    
    
	JFrame frame;
	public View()
	{
		tabbedPane = new JTabbedPane();
		AdminSearch = new JPanel();
        UserSearch = new JPanel();
        AdminModify = new JPanel();
        
        tabbedPane.addTab("AdminModify", new ImageIcon(), AdminModify, null);
        tabbedPane.addTab("AdminSearch", new ImageIcon(), AdminSearch, null);
        tabbedPane.addTab("User", new ImageIcon(), UserSearch, null);

//		AdminModify Tab
        insertPerson = new JButton("Insert Person");
        insertPerson.setBorder(null);
        insertPerson.setForeground(Color.BLACK);
        insertPerson.setFont(new Font("Roboto Medium", Font.BOLD, 25));
        updatePerson = new JButton("Update Person");
        updatePerson.setBorder(null);
        updatePerson.setForeground(Color.BLACK);
        updatePerson.setFont(new Font("Roboto Medium", Font.BOLD, 25));
        deletePerson = new JButton("Delete Person");
        deletePerson.setBorder(null);
        deletePerson.setForeground(Color.BLACK);
        deletePerson.setFont(new Font("Roboto Medium", Font.BOLD, 25));
        insertCar = new JButton("Insert Car");
        insertCar.setBorder(null);
        insertCar.setForeground(Color.BLACK);
        insertCar.setFont(new Font("Roboto Medium", Font.BOLD, 25));
        updateCar = new JButton("Update Car");
        updateCar.setBorder(null);
        updateCar.setForeground(Color.BLACK);
        updateCar.setFont(new Font("Roboto Medium", Font.BOLD, 25));
        deleteCar = new JButton("Delete Car");
        deleteCar.setBorder(null);
        deleteCar.setForeground(Color.BLACK);
        deleteCar.setFont(new Font("Roboto Medium", Font.BOLD, 25));
        insertViolation = new JButton("Insert Violation");
        insertViolation.setBorder(null);
        insertViolation.setForeground(Color.BLACK);
        insertViolation.setFont(new Font("Roboto Medium", Font.BOLD, 25));
        updateViolation = new JButton("Update Violation");
        updateViolation.setBorder(null);
        updateViolation.setForeground(Color.BLACK);
        updateViolation.setFont(new Font("Roboto Medium", Font.BOLD, 25));
        deleteViolation = new JButton("Delete Violation");
        deleteViolation.setBorder(null);
        deleteViolation.setForeground(Color.BLACK);
        deleteViolation.setFont(new Font("Roboto Medium", Font.BOLD, 25));
        
        setButtonFormat(insertPerson);
        setButtonFormat(updatePerson);
        setButtonFormat(deletePerson);
        setButtonFormat(insertCar);
        setButtonFormat(updateCar);
        setButtonFormat(deleteCar);
        setButtonFormat(insertViolation);
        setButtonFormat(updateViolation);
        setButtonFormat(deleteViolation);
        
        modify = new JPanel();
        modify.setLayout(new GridLayout(3,3, 30, 40));
        modify.setBackground(Color.WHITE);
        modify.setOpaque(true);

        modify.add(insertPerson);
        modify.add(updatePerson);
        modify.add(deletePerson);
        modify.add(insertCar);
        modify.add(updateCar);
        modify.add(deleteCar);
        modify.add(insertViolation);
        modify.add(updateViolation);
        modify.add(deleteViolation);
        
        AdminModify.add(modify);
        
        
//      AdminSearch Tab
        person = new JButton("Person Table");
        car = new JButton("Car Table");
        violation = new JButton("Violation Table");
        unpaid = new JButton("Unpaid Table");
        setButtonFormat(person);
        setButtonFormat(car);
        setButtonFormat(violation);
        setButtonFormat(unpaid);
        
        lower = new JPanel();
        lower.setLayout(new FlowLayout());
        lower.add(person);
        lower.add(car);
        lower.add(violation);
        lower.add(unpaid);
        
        String[] options = new String[14];
        options[0] = "";
        options[1] = "[1] Total numbers of current year violation";
        options[2] = "[2] Violation not paid over 1 year";
        options[3] = "[3] frequency of violation contravention";
        options[4] = "[4] Owns more than 2 cars and has no violation";
        options[5] = "[5] Average fine";
        options[6] = "[6] The number of violation committed by different age";
        options[7] = "[7] Make of the car @ central @ 2013,2014";
        options[8] = "[8] The number of violation in recent three years";
        options[9] = "[9] A color more than 15 cars share";
        options[10] = "[10] Years have maximum and minimum number of violation";
        options[11] = "[11] Find the person who violated parking at least twice";
        options[12] = "[12] Top 5 Make of cars more likely to violate parking";
        options[13] = "[13] Find out persons who have 2 or 2 more unpaid tickets";
        choices = new JComboBox(options);
        choices.setSelectedIndex(0);
        
        upper = new JPanel();
        display = new JPanel();
        AdminSearch.setLayout(new BorderLayout());
        upper.add(choices);
        scrollPane2 = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        display.add(scrollPane2);
        AdminSearch.add(upper, BorderLayout.NORTH);
        AdminSearch.add(display, BorderLayout.CENTER);
        AdminSearch.add(lower, BorderLayout.SOUTH);
        
//      UserSearch Tab
        topPanel = new JPanel();
        searchButtonPanel = new JPanel();
        searchPanel = new JPanel();
        SearchField = new JTextField(20);
        resetSearchTable = new JButton("Reset");
        plateSearch = new JButton("LicensePlate UserSearch");
        pcnSearch = new JButton("PCN UserSearch");
        dlicenseSearch = new JButton("DriverLicense UserSearch");
        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        UserSearch.setLayout(new BorderLayout());
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.Y_AXIS));
        searchButtonPanel.setLayout(new BoxLayout(searchButtonPanel,BoxLayout.X_AXIS));

        topPanel.add(SearchField);
        searchButtonPanel.add(plateSearch);
        searchButtonPanel.add(pcnSearch);
        searchButtonPanel.add(dlicenseSearch);

        topPanel.add(searchButtonPanel);
        searchPanel.add(scrollPane);
        UserSearch.add(topPanel, BorderLayout.NORTH);
        UserSearch.add(searchPanel, BorderLayout.CENTER);
		frame = new JFrame("Parking Violation");

		frame.add(tabbedPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 700);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);	
	}
	
	/**
	 * create and display JTable on the panel in the AdminSearch Tab
	 * @param tableModel table model to be used to create JTable
	 */
	public void setAdminSearchTable(DefaultTableModel tableModel)
	{
		display.removeAll();
		itemTable = new JTable(tableModel);
		itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane2 = new JScrollPane(itemTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane2.setPreferredSize(new Dimension(700,450));
		itemTable.invalidate();
		scrollPane2.repaint();
		display.add(scrollPane2);
	}
	
	/**
	 * create and display JTable on the panel in the UserSearch Tab
	 * @param tableModel tableModel table model to be used to create JTable
	 */
	public void setTable(DefaultTableModel tableModel)
	{
		searchPanel.removeAll();
		itemTable = new JTable(tableModel);
		itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(itemTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(700,450));
		
		itemTable.invalidate();
		scrollPane.repaint();
		searchPanel.add(scrollPane);
		
	}
	void addPlateSearchListener(ActionListener lis){plateSearch.addActionListener(lis);}
	void addPcnSearchListener(ActionListener lis){pcnSearch.addActionListener(lis);}
	void addDLicenseSearchListener(ActionListener lis){dlicenseSearch.addActionListener(lis);}
	void insertPersonListener(ActionListener lis){insertPerson.addActionListener(lis);}
	void deletePersonListener(ActionListener lis){deletePerson.addActionListener(lis);}
	void updatePersonListener(ActionListener lis){updatePerson.addActionListener(lis);}
	void insertViolationListener(ActionListener lis){insertViolation.addActionListener(lis);}
	void insertCarListener(ActionListener lis){insertCar.addActionListener(lis);}
	void updateCarListener(ActionListener lis){updateCar.addActionListener(lis);}
	void deleteCarListener(ActionListener lis){deleteCar.addActionListener(lis);}
	void updateViolationListener(ActionListener lis){updateViolation.addActionListener(lis);}
	void deleteViolationListener(ActionListener lis){deleteViolation.addActionListener(lis);}
	
	void showPersonTableListener(ActionListener lis){person.addActionListener(lis);}
	void showCarTableListener(ActionListener lis){car.addActionListener(lis);}
	void showViolationListener(ActionListener lis){violation.addActionListener(lis);}
	void showUnpaidListener(ActionListener lis){unpaid.addActionListener(lis);}
	
	//actionlistener for JComboBox
	void jcomboboxListener(ActionListener lis){choices.addActionListener(lis);}
	
	//button format
	public static void setButtonFormat(JButton jb)
	{
		jb.setForeground(Color.black);
        jb.setBackground(Color.WHITE);
        jb.setPreferredSize(new Dimension(170, 40));
        jb.setFont(new Font("Serif", Font.BOLD, 20));
	}
	
}
