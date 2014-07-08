package me.naftoreiclag.test.copy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class LandmarkMakerTest extends JFrame
{
	LandmarkMakerTestPanel panel;
	
	private LandmarkMakerTest() throws Exception
	{
		super("Pig Collision Demo");
		
		System.out.println("landmark mkaer test");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		
		panel = new LandmarkMakerTestPanel();

		//
		
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem fileMenuNew = new JMenuItem("New");
		fileMenuNew.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				panel.onFileNew(e);
			}
		});
		fileMenu.add(fileMenuNew);

		JMenuItem fileMenuCFI = new JMenuItem("Create from image");
		fileMenuCFI.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				panel.onFileCFIPressed(e);
			}
		});
		fileMenu.add(fileMenuCFI);

		JMenuItem fileMenuOpen = new JMenuItem("Open");
		fileMenuOpen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				panel.onFileOpen(e);
			}
		});
		fileMenu.add(fileMenuOpen);

		JMenuItem fileMenuSave = new JMenuItem("Save");
		fileMenuSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				panel.onFileSave(e);
			}
		});
		fileMenu.add(fileMenuSave);

		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);

		JMenu aboutMenu = new JMenu("About");
		menuBar.add(aboutMenu);

		this.setJMenuBar(menuBar);

		//
		
		JScrollPane scrollContainer = new JScrollPane(panel);
		panel.giveScrolPaneAccess(scrollContainer);

		this.add(scrollContainer);
	}

	public static void main(String[] args) throws Exception
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
		}
		
		LandmarkMakerTest m = new LandmarkMakerTest();
		m.setVisible(true);
	}
}
