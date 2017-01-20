package ai.ui.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ai.FileProcessor;
import ai.classifier.Classifier;

public class ClassifierMainWindow {
	
	public static void main(String[] args) throws IOException {
		Map<String, String> classfolders = new HashMap<String, String>();
		
		classfolders.put("Female", "C:\\Users\\Florian\\Documents\\blogs\\F");
		classfolders.put("Male", "C:\\Users\\Florian\\Documents\\blogs\\M");
		
		Map<String, Collection<String>> texts = new HashMap<String, Collection<String>>();
		for (Entry<String, String> entry : classfolders.entrySet()) {
			texts.put(entry.getKey(), FileProcessor.importFiles(entry.getValue()));
		}
		
		Classifier classifier = new Classifier(texts);
		
		new ClassifierMainWindow(classifier).setVisible(true);
	}
	
	private Classifier classifier;

	private JFrame frame;
	
	JLabel lblExpectation;
	JLabel lblCurrentDocPath;
	JTextArea textPaneCurrentDoc;
	private JPanel textCountsContainer;

	private Container classAssignmentContainer;

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public ClassifierMainWindow(Classifier classifier) {
		this.classifier = classifier;
		
		initialize();
	}
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0};
		gridBagLayout.rowHeights = new int[] {0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JPanel documentContainer = new JPanel();
		GridBagConstraints gbc_documentContainer = new GridBagConstraints();
		gbc_documentContainer.fill = GridBagConstraints.BOTH;
		gbc_documentContainer.weighty = 1.0;
		gbc_documentContainer.weightx = 1.0;
		gbc_documentContainer.insets = new Insets(0, 10, 0, 5);
		gbc_documentContainer.gridx = 0;
		gbc_documentContainer.gridy = 0;
		frame.getContentPane().add(documentContainer, gbc_documentContainer);
		documentContainer.setLayout(new BoxLayout(documentContainer, BoxLayout.Y_AXIS));
		
		JPanel expactationContainer = new JPanel();
		documentContainer.add(expactationContainer);
		
		JLabel lblExpectationText = new JLabel("Expectation:");
		expactationContainer.add(lblExpectationText);
		
		lblExpectation = new JLabel("");
		expactationContainer.add(lblExpectation);
		
		JPanel currentDocumentContainer = new JPanel();
		documentContainer.add(currentDocumentContainer);
		GridBagLayout gbl_currentDocumentContainer = new GridBagLayout();
		gbl_currentDocumentContainer.columnWidths = new int[] {0, 0};
		gbl_currentDocumentContainer.rowHeights = new int[] {0, 0};
		gbl_currentDocumentContainer.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_currentDocumentContainer.rowWeights = new double[]{0.0, 1.0};
		currentDocumentContainer.setLayout(gbl_currentDocumentContainer);
		
		lblCurrentDocPath = new JLabel("");
		GridBagConstraints gbc_lblCurrentDocPath = new GridBagConstraints();
		gbc_lblCurrentDocPath.anchor = GridBagConstraints.NORTH;
		gbc_lblCurrentDocPath.insets = new Insets(0, 0, 5, 0);
		gbc_lblCurrentDocPath.gridx = 0;
		gbc_lblCurrentDocPath.gridy = 0;
		currentDocumentContainer.add(lblCurrentDocPath, gbc_lblCurrentDocPath);
		
		textPaneCurrentDoc = new JTextArea();
		textPaneCurrentDoc.setEditable(false);
		textPaneCurrentDoc.setLineWrap(true);
		
		JScrollPane scroll = new JScrollPane(textPaneCurrentDoc);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.insets = new Insets(0, 0, 5, 0);
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 1;
		currentDocumentContainer.add(scroll, gbc_textPane);
		
		classAssignmentContainer = new JPanel();
		documentContainer.add(classAssignmentContainer);
		classAssignmentContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel sidebarContainer = new JPanel();
		GridBagConstraints gbc_sidebarContainer = new GridBagConstraints();
		gbc_sidebarContainer.insets = new Insets(0, 0, 0, 10);
		gbc_sidebarContainer.fill = GridBagConstraints.BOTH;
		gbc_sidebarContainer.gridx = 1;
		gbc_sidebarContainer.gridy = 0;
		frame.getContentPane().add(sidebarContainer, gbc_sidebarContainer);
		GridBagLayout gbl_sidebarContainer = new GridBagLayout();
		gbl_sidebarContainer.columnWidths = new int[] {0};
		gbl_sidebarContainer.rowHeights = new int[] {0, 0, 0};
		gbl_sidebarContainer.columnWeights = new double[]{0.0};
		gbl_sidebarContainer.rowWeights = new double[]{0.0, 1.0, 0.0};
		sidebarContainer.setLayout(gbl_sidebarContainer);
		
		JPanel settingsContainer = new JPanel();
		GridBagConstraints gbc_settingsContainer = new GridBagConstraints();
		gbc_settingsContainer.fill = GridBagConstraints.HORIZONTAL;
		gbc_settingsContainer.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc_settingsContainer.insets = new Insets(0, 0, 5, 0);
		gbc_settingsContainer.gridx = 0;
		gbc_settingsContainer.gridy = 0;
		sidebarContainer.add(settingsContainer, gbc_settingsContainer);
		GridBagLayout gbl_settingsContainer = new GridBagLayout();
		gbl_settingsContainer.columnWidths = new int[]{0, 0, 0};
		gbl_settingsContainer.rowHeights = new int[] {0, 0};
		gbl_settingsContainer.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_settingsContainer.rowWeights = new double[]{0.0, 0.0};
		settingsContainer.setLayout(gbl_settingsContainer);
		
		JLabel lblSettingsError = new JLabel("");
		GridBagConstraints gbc_lblSettingsError = new GridBagConstraints();
		gbc_lblSettingsError.gridwidth = 2;
		gbc_lblSettingsError.insets = new Insets(0, 0, 5, 5);
		gbc_lblSettingsError.gridx = 0;
		gbc_lblSettingsError.gridy = 0;
		settingsContainer.add(lblSettingsError, gbc_lblSettingsError);
		
		JButton btnLoadTestSet = new JButton("Load test set");
		btnLoadTestSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblSettingsError.setText("");
				
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				//In response to a button click:
				int returnVal = fc.showOpenDialog(frame);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            try {
						docsQueue.putAll(FileProcessor.importFilesWithPath(file.getAbsolutePath()));
					} catch (IOException e1) {
						lblSettingsError.setText("Error while importing");
					}
		        }
				
				update();
			}
		});
		

		GridBagConstraints gbc_btnLoadTestSet = new GridBagConstraints();
		gbc_btnLoadTestSet.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLoadTestSet.gridwidth = 2;
		gbc_btnLoadTestSet.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadTestSet.gridx = 0;
		gbc_btnLoadTestSet.gridy = 1;
		settingsContainer.add(btnLoadTestSet, gbc_btnLoadTestSet);
		
		JPanel statsContainer = new JPanel();
		GridBagConstraints gbc_statsContainer = new GridBagConstraints();
		gbc_statsContainer.insets = new Insets(0, 0, 5, 0);
		gbc_statsContainer.fill = GridBagConstraints.HORIZONTAL;
		gbc_statsContainer.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc_statsContainer.gridx = 0;
		gbc_statsContainer.gridy = 1;
		sidebarContainer.add(statsContainer, gbc_statsContainer);
		GridBagLayout gbl_statsContainer = new GridBagLayout();
		gbl_statsContainer.columnWidths = new int[] {0};
		gbl_statsContainer.rowHeights = new int[] {0, 0};
		gbl_statsContainer.columnWeights = new double[]{1.0};
		gbl_statsContainer.rowWeights = new double[]{0.0, 0.0};
		statsContainer.setLayout(gbl_statsContainer);
		
		textCountsContainer = new JPanel();
		GridBagConstraints gbc_textCountsContainer = new GridBagConstraints();
		gbc_textCountsContainer.fill = GridBagConstraints.HORIZONTAL;
		gbc_textCountsContainer.insets = new Insets(0, 0, 5, 0);
		gbc_textCountsContainer.anchor = GridBagConstraints.NORTH;
		gbc_textCountsContainer.gridx = 0;
		gbc_textCountsContainer.gridy = 0;
		statsContainer.add(textCountsContainer, gbc_textCountsContainer);
		GridBagLayout gbl_textCountsContainer = new GridBagLayout();
		gbl_textCountsContainer.columnWidths = new int[] {0, 0};
		gbl_textCountsContainer.rowHeights = new int[] {0};
		gbl_textCountsContainer.columnWeights = new double[]{0.0, 1.0};
		gbl_textCountsContainer.rowWeights = new double[]{0.0};
		textCountsContainer.setLayout(gbl_textCountsContainer);
		
		addStatistic("K value");
		setStatistic("K value", Double.toString(classifier.getK()));
		
		addStatistic("Vocabulary size");
		setStatistic("Vocabulary size", Integer.toString(classifier.getVocabularySize()));
		
		for (String className : classifier.getClasses()) {
			addStatistic(className + TEXT_COUNT_SUFFIX);
			addClassifierButton(className);
		}
		
		addStatistic("Accuracy");
		update();
	}
	


	public void update() {
		for (Entry<String, Integer> entry : classifier.getTextCounts().entrySet()) {
			setStatistic(entry.getKey() + TEXT_COUNT_SUFFIX, entry.getValue().toString());
		}
		
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		double acc = (double) hit / (hit + miss) * 100;
		
		setStatistic("Accuracy", (miss + hit > 0) ? df.format(acc) + "%" : "N/A");
		
		if (nextDocAvailable() && !currentDocExists()) {
			loadNextDoc();
		}
	}
	
	private void addClassifierButton(String className) {
		JButton btnMale = new JButton(className);
		btnMale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (currentDocExists()) {
					classifier.train(className, getCurrentDoc());
					
					if (className.equals(lblExpectation.getText())) {
						addHit();
					} else {
						addMiss();
					}
					
					update();
					if (nextDocAvailable()) {
						loadNextDoc();
					} else {
						lblCurrentDocPath.setText("Test documents exhausted!");
						clearCurrentDoc();
					}
				}
			}
		});
		
		classAssignmentContainer.add(btnMale);
	}
	
	int miss = 0;
	int hit = 0;
	
	private void addMiss() {
		miss += 1;
	}
	
	private void addHit() {
		hit += 1;
	}
	
	Map<String, String> docsQueue = new HashMap<String, String>();
	
	private boolean nextDocAvailable() {
		return docsQueue.size() > 0;
	}
	
	private void loadNextDoc() {
		Entry<String, String> doc = docsQueue.entrySet().iterator().next();
		lblCurrentDocPath.setText(doc.getKey());
		setCurrentDoc(doc.getValue());
		
		docsQueue.remove(doc.getKey());
	}
	
	boolean currentDocExists = false;
	
	private void setCurrentDoc(String text) {
		textPaneCurrentDoc.setText(text);
		lblExpectation.setText(classifier.apply(text));
		currentDocExists = true;
	}
	
	private void clearCurrentDoc() {
		textPaneCurrentDoc.setText("");
		currentDocExists = false;
	}
	
	private boolean currentDocExists() {
		return currentDocExists;
	}

	private String getCurrentDoc() {
		return textPaneCurrentDoc.getText();
	}
	
	public static final String TEXT_COUNT_SUFFIX = " texts";
	HashMap<String, JLabel> statLabels = new HashMap<String, JLabel>();
	
	private void addStatistic(String name) {
		int row = statLabels.size();
		
		JLabel lblStatName = new JLabel(name);
		GridBagConstraints gbc_lblStatName = new GridBagConstraints();
		gbc_lblStatName.anchor = GridBagConstraints.LINE_START;
		gbc_lblStatName.insets = new Insets(0, 0, 0, 5);
		gbc_lblStatName.gridx = 0;
		gbc_lblStatName.gridy = row;
		textCountsContainer.add(lblStatName, gbc_lblStatName);
		
		JLabel lblStatValue = new JLabel();
		GridBagConstraints gbc_lblStatValue = new GridBagConstraints();
		gbc_lblStatValue.anchor = GridBagConstraints.LINE_END;
		gbc_lblStatValue.gridx = 1;
		gbc_lblStatValue.gridy = row;
		textCountsContainer.add(lblStatValue, gbc_lblStatValue);
		
		statLabels.put(name, lblStatValue);
	}
	
	private void setStatistic(String name, String value) {
		if (statLabels.containsKey(name)) {
			statLabels.get(name).setText(value);
		} else {
			System.err.println("No stat found by name " + name);
		}
	}

}
