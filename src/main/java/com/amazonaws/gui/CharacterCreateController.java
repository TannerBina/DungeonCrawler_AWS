package com.amazonaws.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.backend.User;
import com.amazonaws.util.Constants;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/*
 * A controller for all pages related to creating a character.
 * Handles create a new character, upon creation, calls the create
 * character function.
 */
public class CharacterCreateController {
	
	public static int state;
	private static Map<Integer, String> dataMap;
	
	/*
	 * Initalization function
	 */
	@FXML
	public void initialize() {
		//init based on states
		switch(state) {
		case 0:
			initName();
			break;
		case 1:
			initRace();
			break;
		case 2:
			initClass();
			break;
		case 3:
			initBackground();
			break;
		case 4:
			initStats();
			break;
		case 5:
			initAlignment();
			break;
		case 6:
			initProfs();
			break;
		case 7:
			initFeats();
			break;
		case 8:
			initWeapons();
			break;
		case 9:
			initArmor();
			break;
		case 10:
			initSpells();
			break;
		case 11:
			initItems();
			break;
		}
	}

	private void initProfs() {
		// TODO Auto-generated method stub
		
	}

	private void initSpells() {
		// TODO Auto-generated method stub
		
	}

	private void initItems() {
		// TODO Auto-generated method stub
		
	}

	private void initArmor() {
		// TODO Auto-generated method stub
		
	}

	private void initWeapons() {
		// TODO Auto-generated method stub
		
	}

	private void initFeats() {
		// TODO Auto-generated method stub
		
	}

	private void initAlignment() {
		// TODO Auto-generated method stub
		
	}

	private void initStats() {
		// TODO Auto-generated method stub
		
	}

	private void initBackground() {
		// TODO Auto-generated method stub
		
	}

	private void initClass() {
		// TODO Auto-generated method stub
		
	}

	private void initRace() {
		// TODO Auto-generated method stub
		
	}

	private void initName() {
		dataMap = new HashMap<>();
	}
	
	/*
	 * Handle the back btn being pressed.
	 * Deletes last entered data field,
	 * Navis to page beforehand.
	 */
	@FXML
	private void handleBtnBack() {
		state--;
		
		if (state < 0) {
			naviToNew(btnBack, Constants.WIN_USER);
			return;
		}
		
		dataMap.remove(state);
		naviToNew(btnBack, Constants.WINS_CREATE_CHAR[state]);
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
	
	@FXML
	private Button btnBack;
	@FXML
	private Button btnNext;
}
