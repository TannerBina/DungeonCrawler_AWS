package com.amazonaws.gui;

import com.amazonaws.backend.User;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class WindowController {
	/*
	 * Handles sending whatever is in
	 * the text box to the server
	 */
	@FXML
	public void onEnterSend() {
		String message = enteFie.getText().toString().trim();
		enteFie.clear();
		if (message.equals("$clear") || message.equals("@clear")) {
			commAre.clear();
		} else {
			User.getInstance().send(message);
		}
	}
	
	/*
	 * Updates the displayed information in the controller
	 */
	public void update() {
		
	}
	
	/*
	 * Displays a given string in the text area
	 */
	public void display(String s) {
		commAre.appendText(s);
	}
	
	@FXML
	private TextField enteFie;
	@FXML
	private TextArea commAre;
}
