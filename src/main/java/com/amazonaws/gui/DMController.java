package com.amazonaws.gui;

import com.amazonaws.backend.User;

import javafx.fxml.FXML;

public class DMController extends WindowController{

	@FXML
	public void initialize() {
		User.getInstance().setController(this);
	}
	
	/*
	 * Handles pressing the load dungeon button
	 * and loading a dungeon into the game
	 */
	@FXML
	public void handLoadDungBut() {
		
	}
	
	/*
	 * Updates the displayed information in the controller
	 */
	@Override
	public void update() {
		
	}
}
