import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class DeleteViolationView extends JFrame{

	JPanel panel;
	JLabel label;
	JTextField textfield;
	JButton btn;

	public DeleteViolationView()
	{
		panel = new JPanel();
		textfield = new JTextField(20);
		btn = new JButton("Submit");
		label = new JLabel("Enter PCN to Delete: ");

		panel.add(label);
		panel.add(textfield);
		panel.add(btn);
		this.setSize(200,400);
		this.add(panel);
		this.setTitle("Delete Violation");
	}
	void addDeleteViolationListener(ActionListener lis){btn.addActionListener(lis);}
}