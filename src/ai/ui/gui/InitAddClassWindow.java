package ai.ui.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class InitAddClassWindow {

	private JFrame frame;
	DefaultTableModel tm;
	private JTextField textField;

	/**
	 * Create the application.
	 */
	public InitAddClassWindow(DefaultTableModel tm) {
		this.tm = tm;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 434, 151);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblClassName = new JLabel("Class name");
		lblClassName.setBounds(10, 11, 102, 14);
		frame.getContentPane().add(lblClassName);
		
		textField = new JTextField();
		textField.setBounds(122, 8, 198, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblFolder = new JLabel("Folder");
		lblFolder.setBounds(10, 35, 102, 14);
		frame.getContentPane().add(lblFolder);
		
		JLabel lblPlaceholder = new JLabel("");
		lblPlaceholder.setBounds(122, 61, 286, 14);
		frame.getContentPane().add(lblPlaceholder);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(122, 31, 89, 23);
		btnBrowse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				//In response to a button click:
				int returnVal = fc.showOpenDialog(frame);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            lblPlaceholder.setText(file.getAbsolutePath());
		        } else {
		            lblPlaceholder.setText("Something went wrong, try again!");
		        }
			}
		});
		frame.getContentPane().add(btnBrowse);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tm.addRow(new Object[] {textField.getText(), lblPlaceholder.getText()});
				frame.dispose();
			}
		});
		btnAdd.setBounds(164, 86, 89, 23);
		frame.getContentPane().add(btnAdd);
		
		frame.setVisible(true);
	}
}
