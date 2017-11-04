package com.amazonaws.gui;

import java.io.IOException;
import java.util.Scanner;

import com.amazonaws.backend.User;
import com.amazonaws.lambdafunction.callers.CreateGameInput;
import com.amazonaws.lambdafunction.callers.Output;
import com.amazonaws.util.Constants;
import com.amazonaws.util.LambdaServices;
import com.amazonaws.util.Messages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/*
 * A controller for the page createGameWin.fxml
 * Handles the input of information for creating a game
 */
public class GameCreateController {

	@FXML
	public void initialize() {
		
	}
	
	/*
	 * Handles the host game button 
	 * creates a game and joins the user
	 * into the game as the dm
	 */
	@FXML
	public void handleBtnHostGame() {
		//settup the input with the username, name, password
		CreateGameInput in = new CreateGameInput();
		in.setName(tfGameName.getText().toString());
		in.setPassword(tfGamePassword.getText().toString());
		in.setUsername(User.getInstance().getUsername());
		
		//attempt to create game
		Output out = LambdaServices.createGame(in);
		
		Scanner s = new Scanner(out.getMessage());
		s.useDelimiter(Constants.LAMBDA_DELIMINATOR);
		
		switch(s.next()) {
		case Messages.ERROR_TAG:
			try {
				lblError.setText(Messages.getErrorString(Messages.getErrorMessage(out.getMessage())));
			} catch (Exception e) {
				System.err.println("Error Message not recognized in LoginController handleBtnLogin.");
			}
			break;
			
		case Messages.SUCCESS_TAG:
			User.getInstance().createGame(tfGameName.getText());
			break;
		}
		
		
		s.close();
	}
	
	/*
	 * Handles the back button and returns the user
	 * to the user window
	 */
	@FXML
	public void handleBtnBack() {
		naviToNew(btnBack, Constants.WIN_USER);
	}
	
	/*
	 * Navigates to a new javafx page.
	 */
	private void naviToNew(Button but, String fileName) {
		try {
            //load stage
            Stage stage = (Stage) but.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fileName));
            Scene scene = new Scene(root);

            //hide current screen
            stage.hide();
            stage.setScene(scene);

            //set the attributes for the screen
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnCloseRequest(event -> System.exit(0));
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            System.err.println();
            System.err.println(fileName + " Error in navigating to");
        }
	}
	
	@FXML
	private Label lblError;
	@FXML
	private TextField tfGamePassword;
	@FXML
	private TextField tfGameName;
	@FXML
	private Button btnHostGame;
	@FXML
	private Button btnBack;
}
