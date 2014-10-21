package csse2002.security;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;

/**
 * The view for the Spy Simulator.
 */
@SuppressWarnings("serial")
public class SpyView extends JFrame {

	// the model of the Spy Simulator
	private SpyModel model;

	/** THESE FIELDS USED FOR TESTING: DO NOT CHANGE DECLARATION! */
	// text area for displaying the current knowledge state of the spy
	public JTextArea txtSpy;
	// scroll panel for containing txtSpy
	private JScrollPane scpSpy;
	// text area for displaying informant interactions and errors
	public JTextArea txtInfo;
	// scroll panel for containing txtInfo
	private JScrollPane scpInfo;
	// informants field: for entering the name of informants to be read
	public JTextField txtInformantsFile;
	// read button: for reading the informants in txtInformantsFile
	public JButton cmdRead;
	// meet informants button: for updating knowledge state of spy after
	// encounter with next informant
	public JButton cmdMeet;
	// guess button: for making the spy guess the secret
	public JButton cmdGuess;

	/** END DECLARATION OF TESTING FIELDS */
	private final String LINE_SEPARATOR = System.getProperty("line.separator");

	

	/**
	 * Creates a new Spy Simulator window.
	 */
	public SpyView(SpyModel model) {
		super("Spy Simulator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//create a gridbag as layout style.
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);
		//set the size of the window.
		setBounds(50, 50, 800, 500);
		//set the background color.
		setBackground(Color.WHITE);
		//make it unresizable.
		setResizable(false);
		// c = new GridBagConstaints(gridx, gridy, gridwidth, gridheight,
		// weightx, weighty, anchor, fill, inset, ipadx, ipady);
		// set the Spy text
		txtSpy = new JTextArea("Spy thinks ..." + LINE_SEPARATOR
				+ "Secret is true with probability "
				+ SpyModel.SECRET_BIAS.toString());
		//make it is not editable.
		txtSpy.setEditable(false);
		//Set a pane for txtSpy.
		scpSpy = new JScrollPane(txtSpy);
		gridbag.setConstraints(scpSpy, new GridBagConstraints(0, 0, 10, 8, 2.4,
				1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		//Set text area for displaying informant interactions and errors
		txtInfo = new JTextArea();
		scpInfo = new JScrollPane(txtInfo);
		gridbag.setConstraints(scpInfo, new GridBagConstraints(10, 0, 10, 8,
				0.14, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		//Set a field for entering the name of informants to be read
		txtInformantsFile = new JTextField();
		gridbag.setConstraints(txtInformantsFile, new GridBagConstraints(0, 9,
				15, 1, 0.6, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		//Set the Read button.
		cmdRead = new JButton("Read Informants");
		gridbag.setConstraints(cmdRead, new GridBagConstraints(15, 9, 5, 1,
				0.4, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		//Set the Meet button.
		cmdMeet = new JButton("Meet Informant");
		gridbag.setConstraints(cmdMeet, new GridBagConstraints(10, 8, 5, 1,
				0.25, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		//Set the Guess button.
		cmdGuess = new JButton("Guess Secret");
		gridbag.setConstraints(cmdGuess, new GridBagConstraints(15, 8, 5, 1,
				0.25, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	
		// add all bellowing components to the model.
		this.model = model;
		add(scpInfo);
		add(scpSpy);
		add(txtInformantsFile);
		add(cmdRead);
		add(cmdMeet);
		add(cmdGuess);
	}

}
