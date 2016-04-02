import javax.swing.*;

/**
 * Tester class to initialize objects in main method
 * @author Ziyu Huang, Zhining Qi, Tri Ninh
 *
 */
public class Tester extends JFrame
{
	public static void main(String[] args)
	{
		ViolationView vview = new ViolationView();
		PersonView pview = new PersonView();
		UpdatePerson upview = new UpdatePerson();
		UpdateCarView ucview = new UpdateCarView();
		UpdateViolationView uvview = new UpdateViolationView();
		CarView cview = new CarView();
		DeletePersonView dpview = new DeletePersonView();
		DeleteCarView dcview = new DeleteCarView();
		DeleteViolationView dvview = new DeleteViolationView();
		View v = new View();
		Model m = new Model(v);
		Controller c = new Controller(m, v, pview, dpview, upview, vview, cview, ucview, dcview, uvview, dvview);
	}
}
