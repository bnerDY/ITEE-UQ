package csse2002.security;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * The controller for the Spy Simulator.
 */
public class SpyController {
	// the model of the simulator
	private SpyModel model;
	// the view of the simulator
	private SpyView view;
	// ActionListener for the read.
	private class readListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			List<TwoCoinChannel> tmp = null;
			// Check if the file is readable or not.
			try {
				tmp = InformantsReader.readInformants(view.txtInformantsFile
						.getText());
			} catch (FileFormatException e1) {
				view.txtInfo.setText("Error reading from file.");
			} catch (IOException e1) {
				view.txtInfo.setText("Error reading from file.");
			} finally {
				if (tmp != null) {
					model.addInformants(tmp);
					view.txtInfo.setText("Informants added from file.");
				}
			}
		}
	}
	//ActionListener for the meet.
	private class meetListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			view.txtInfo.setText(model.meet());
			view.txtSpy.setText(model.getWhatSpyThinks());
		}
	}
	//ActionListener for the guess
	private class guessListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			view.txtInfo.setText(model.guess());
			view.txtSpy.setText(model.getWhatSpyThinks());
		}
	}

	/**
	 * Initialises the SpyController for the Spy Simulator.
	 */
	public SpyController(SpyModel model, SpyView view) {
		this.model = model;
		this.view = view;
		//add ActionListener to the read button.
		view.cmdRead.addActionListener(new readListener());
		//add ActionListener to the meet button.
		view.cmdMeet.addActionListener(new meetListener());
		//add ActionListener to the guess button.
		view.cmdGuess.addActionListener(new guessListener());
	}

}
