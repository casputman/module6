package ai.ui.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import ai.FileProcessor;
import ai.classifier.Classifier;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Window supporting the initialization phase. i.e. picking training sets and defining classes.
 */
public class InitMainWindow {

	private JFrame frame;
	private JTable table;
	private JButton btnRemoveClass;
	private JButton btnStart;
	private JLabel lblError;
	private JPanel panel_1;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JTextField textFieldKValue;
	private JTextField textFieldVocabularySize;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InitMainWindow window = new InitMainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public InitMainWindow() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 567, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		
		DefaultTableModel tm = new DefaultTableModel();
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Add class");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new InitAddClassWindow(tm);
			}
		});
		panel.add(btnNewButton);
		
		btnRemoveClass = new JButton("Remove class");
		panel.add(btnRemoveClass);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Map<String, String> classfolders = new HashMap<String, String>();
				for (int i = 0; i < tm.getRowCount(); i++) {
					classfolders.put((String) tm.getValueAt(i, 0), (String) tm.getValueAt(i, 1));
				}
				
				String currentImportingClass = "";
				try {
					Map<String, Collection<String>> texts = new HashMap<String, Collection<String>>();
					for (Entry<String, String> entry : classfolders.entrySet()) {
						currentImportingClass = entry.getKey();
						texts.put(entry.getKey(), FileProcessor.importFiles(entry.getValue()));
					}
					
					Classifier classifier = new Classifier(texts, 
								Integer.valueOf(textFieldVocabularySize.getText()), 
								Double.valueOf(textFieldKValue.getText()));
					
					new ClassifierMainWindow(classifier).setVisible(true);
					frame.dispose();
				} catch (IOException e) {
					lblError.setText("Error while reading the files for class " + currentImportingClass + " (IOException)");
				}
			}
		});
		panel.add(btnStart);
		
		table = new JTable(new Object[][] {}, new String[] {"Class", "Folder"});
        JScrollPane scrollPane = new JScrollPane(table);
        
		tm.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				lblError.setText(""); // Remove error on table change
			}
		});
		tm.addColumn("Class");
		tm.addColumn("Folder");
		table.setModel(tm);
		
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		frame.getContentPane().add(lblError, BorderLayout.NORTH);
		
		panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.EAST);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		lblNewLabel = new JLabel("K value");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.LINE_START;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_1.add(lblNewLabel, gbc_lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Vocabulary size");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.LINE_START;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		panel_1.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textFieldKValue = new JTextField();
		textFieldKValue.setHorizontalAlignment(SwingConstants.TRAILING);
		GridBagConstraints gbc_textFieldKValue = new GridBagConstraints();
		gbc_textFieldKValue.anchor = GridBagConstraints.LINE_END;
		gbc_textFieldKValue.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldKValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldKValue.gridx = 1;
		gbc_textFieldKValue.gridy = 0;
		panel_1.add(textFieldKValue, gbc_textFieldKValue);
		textFieldKValue.setText(Double.toString(Classifier.DEFAULT_K));
		textFieldKValue.setColumns(10);
		
		textFieldVocabularySize = new JTextField();
		textFieldVocabularySize.setHorizontalAlignment(SwingConstants.TRAILING);
		GridBagConstraints gbc_textFieldVocabularySize = new GridBagConstraints();
		gbc_textFieldVocabularySize.anchor = GridBagConstraints.LINE_END;
		gbc_textFieldVocabularySize.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldVocabularySize.gridx = 1;
		gbc_textFieldVocabularySize.gridy = 1;
		panel_1.add(textFieldVocabularySize, gbc_textFieldVocabularySize);
		textFieldVocabularySize.setText(Double.toString(Classifier.DEFAULT_VOCABULARY_SIZE));
		textFieldVocabularySize.setColumns(10);
	}

}
