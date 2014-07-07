package me.naftoreiclag.test;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class AreaMakerTest extends JFrame
{
	private AreaMakerTest() throws Exception
	{
		super("Pig Collision Demo");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);

		//
		
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("File");
		menuBar.add(menu);

		JMenu menu2 = new JMenu("About");
		menuBar.add(menu2);

		this.setJMenuBar(menuBar);

		//
		
		AreaMakerTestPanel panel = new AreaMakerTestPanel();
		JScrollPane scrollContainer = new JScrollPane(panel);

		this.add(scrollContainer);
	}

	public static void main(String[] args) throws Exception
	{
		AreaMakerTest m = new AreaMakerTest();
		m.setVisible(true);
	}
}
