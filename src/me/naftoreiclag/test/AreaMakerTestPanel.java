package me.naftoreiclag.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AreaMakerTestPanel extends JPanel
{
JFileChooser fileChooser = new JFileChooser();
	
	public AreaMakerTestPanel() throws Exception
	{
		this.setSize(500, 500);
		
		this.setFocusable(true);
		this.requestFocusInWindow();
	}

	public void onFileNew(ActionEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void onFileCFIPressed(ActionEvent e)
	{
		int returnVal = fileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			System.out.println(file.getName());
		}
		else
		{
			System.out.println("closed");
		}
	}

	public void onFileOpen(ActionEvent e)
	{
		
	}

	public void onFileSave(ActionEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
