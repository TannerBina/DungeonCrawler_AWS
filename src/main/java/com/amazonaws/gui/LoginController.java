package com.amazonaws.gui;

import java.io.IOException;
import java.util.Scanner;

import com.amazonaws.backend.User;
import com.amazonaws.lambdafunction.callers.CreateUserInput;
import com.amazonaws.lambdafunction.callers.LoginInput;
import com.amazonaws.lambdafunction.callers.Output;
import com.amazonaws.util.Constants;
import com.amazonaws.util.LambdaServices;
import com.amazonaws.util.Messages;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/*
 * Controls loginWin
 * Handles the login fetch and create new user fetch
 */
public class LoginController {
	/*
	 * Initialization function
	 * Empty for the login controller
	 */
	@FXML
	public void initialize() {
		
	}
	
	/*
	 * Handle the press of the button btnLogin,
	 * attempts to login the user, if successful
	 * sets the user class up
	 */
	@FXML
	private void handleBtnLogin() {
		//settup the input with the username and password
		LoginInput in = new LoginInput();
		in.setUsername(fldUsername.getText().toString());
		in.setPassword(fldPassword1.getText().toString());
		
		//get the output
		Output out = LambdaServices.login(in);
		
		//scan to determine error or success
		Scanner s = new Scanner(out.getMessage());
		s.useDelimiter(Constants.LAMBDA_DELIMINATOR);
		
		switch(s.next()) {
		
		//if its an error set the label error text.
		case Messages.ERROR_TAG:
			try {
				lblError.setText(Messages.getErrorString(Messages.getErrorMessage(out.getMessage())));
			} catch (Exception e) {
				System.err.println("Error Message not recognized in LoginController handleBtnLogin.");
			}
			break;
			
		//if its a success tag set user creds and characters
		case Messages.SUCCESS_TAG:
			User.getInstance().setCredentials(in.getUsername(), in.getPassword());
			User.getInstance().setCharacters(out.getData());
			naviToNew(btnLogin, Constants.WIN_USER);
			break;
		}
		
		s.close();
	}
	
	/*
	 * Handles the create user button
	 * if succesful, creates user and 
	 * logins in as the user, sets user class up
	 */
	@FXML
	private void handleBtnNewUser() {
		CreateUserInput in = new CreateUserInput();
		in.setUsername(fldUsername.getText().toString());
		in.setPassword_1(fldPassword1.getText().toString());
		in.setPassword_2(fldPassword2.getText().toString());
		
		Output out = LambdaServices.createUser(in);
		
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
			handleBtnLogin();
			break;
		}
		
		s.close();
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
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            	@Override
            	public void handle(WindowEvent t) {
            		User.getInstance().close();
            		System.exit(0);
            	}
            });
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            System.err.println();
            System.err.println(fileName + " Error in navigating to");
        }
	}
	
	/*
	 * All fxml elements that need to be declared
	 */
	//text field elements
	@FXML
	private TextField fldUsername;
	@FXML
	private TextField fldPassword1;
	@FXML
	private TextField fldPassword2;
	
	//label elements
	@FXML
	private Label lblError;
	
	//button elements
	@FXML
	private Button btnLogin;
	
	
}
