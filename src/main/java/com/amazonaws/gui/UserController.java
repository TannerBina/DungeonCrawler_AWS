package com.amazonaws.gui;

import com.amazonaws.backend.User;
import com.amazonaws.lambdafunction.callers.DeleteCharacterInput;
import com.amazonaws.lambdafunction.callers.Output;
import com.amazonaws.util.Constants;
import com.amazonaws.util.LambdaServices;
import com.amazonaws.util.Messages;
import com.amazonaws.backend.Character;
import com.amazonaws.backend.Character.StatTag;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/*
 * A controller for the page UserWin.fxml
 * Handles creating games, joining games and picking character
 */
public class UserController {
	
	private Timer updateGameTable;

	/*
	 * Initaliziation function
	 */
	@FXML
	public void initialize() {
		// set cell values for char table and game table
		tcName.setCellValueFactory(new PropertyValueFactory<CharacterDisplayer, String>("name"));
		tcClass.setCellValueFactory(new PropertyValueFactory<CharacterDisplayer, String>("pcClass"));
		tcRace.setCellValueFactory(new PropertyValueFactory<CharacterDisplayer, String>("race"));
		tcLevel.setCellValueFactory(new PropertyValueFactory<CharacterDisplayer, String>("level"));

		tcGameName.setCellValueFactory(new PropertyValueFactory<GameDisplayer, String>("gameName"));
		tcUsername.setCellValueFactory(new PropertyValueFactory<GameDisplayer, String>("username"));

		updateCharacterTable();

		updateGameTable = new Timer();
		updateGameTable.scheduleAtFixedRate(
				new TimerTask() {
					@Override
					public void run() {
						Platform.runLater( new Runnable() {
							public void run() {
								updateGameTable();
							}
						});
					}
				}, 0, 100);
		
		updateGameTable();
		
		lblUser.setText("User : " + User.getInstance().getUsername());
	}

	/*
	 * Updates the game table to display current games.
	 * Called on a timer
	 */
	private void updateGameTable() {
		ObservableList<String> gameOptions = FXCollections.observableArrayList();
		gameOptions.add("Select Game");
		ObservableList<GameDisplayer> gameData = FXCollections.observableArrayList();
		
		cbGameChooser.setItems(gameOptions);
		cbGameChooser.getSelectionModel().selectFirst();
		tblGameList.setItems(gameData);
	}

	/*
	 * Updates the character table to display
	 * the characters that the user has.
	 * Updates on call
	 */
	private void updateCharacterTable() {
		// settup character data and options
		ObservableList<CharacterDisplayer> characterData = FXCollections.observableArrayList();
		ObservableList<String> characterOptions = FXCollections.observableArrayList();
		characterOptions.add("Select Character");

		for (Character c : User.getInstance().getCharacters()) {
			characterData.add(new CharacterDisplayer(c.getStat(StatTag.NAME), c.getStat(StatTag.RACE),
					c.getStat(StatTag.CLASS), c.getStat(StatTag.LEVEL)));
			characterOptions.add(c.getStat(StatTag.NAME));
		}

		// set the chooser and table values
		cbCharacterChooser.setItems(characterOptions);
		cbCharacterChooser.getSelectionModel().selectFirst();
		tblCharacter.setItems(characterData);
	}

	/*
	 * Handles the log out button. Brings you back to the login page
	 */
	@FXML
	public void handleBtnLogOut() {
		User.getInstance().reset();
		naviToNew(btnLogOut, Constants.WIN_LOGIN);
	}

	/*
	 * Handles the create new character button Brings you to the new character page.
	 */
	@FXML
	public void handleBtnCreateNewCharacter() {
		CharacterCreateController.state = 0;
		naviToNew(btnCreateNewCharacter, Constants.WINS_CREATE_CHAR[0]);
	}

	/*
	 * Handles the host game button. Brings you to the create game page.
	 */
	@FXML
	public void handleBtnHostGame() {
		naviToNew(btnHostGame, Constants.WIN_CREATE_GAME);
	}

	/*
	 * Handles the join game button. Brings you to the game page after entering the
	 * correct password
	 */
	@FXML
	public void handleBtnJoinGame() {

	}

	/*
	 * Handles the delete selected character button. Removes character data and
	 * updates dynamoDb tables
	 */
	@FXML
	public void handleBtnDeleteSelectedCharacter() {
		//get the index from chooser
		int index = cbCharacterChooser.getSelectionModel().getSelectedIndex();
		
		//if the index is a char reference
		if (index != 0) {
			//settup input to lambda function
			DeleteCharacterInput in = new DeleteCharacterInput();
			in.setId(User.getInstance().getCharacters().get(index-1).getStat(StatTag.ID));
			in.setUsername(User.getInstance().getUsername());
			
			//send to lambda function
			Output out = LambdaServices.deleteCharacter(in);
			Scanner s = new Scanner(out.getMessage());
			s.useDelimiter(Constants.LAMBDA_DELIMINATOR);
			
			//check error tag or not
			switch(s.next()) {
			case Messages.ERROR_TAG:
				try {
					lblError.setText(Messages.getErrorString(Messages.getErrorMessage(out.getMessage())));
				} catch(Exception e ) {
					System.err.println("Error Message not recognized in UserController handleBtnDeleteCharacter.");
				}
				break;
			
			case Messages.SUCCESS_TAG:
				User.getInstance().getCharacters().remove(index-1);
				updateCharacterTable();
				break;
			}
			
			s.close();
		}
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

	/*
	 * All FXML elements
	 */
	@FXML
	private Label lblUser;
	@FXML
	private Label lblError;
	@FXML
	private TextField tfPassword;
	@FXML
	private Button btnLogOut;
	@FXML
	private Button btnCreateNewCharacter;
	@FXML
	private Button btnHostGame;

	@FXML
	private TableView<GameDisplayer> tblGameList;
	@FXML
	private TableView<CharacterDisplayer> tblCharacter;

	@FXML
	private ChoiceBox<String> cbCharacterChooser;
	@FXML
	private ChoiceBox<String> cbGameChooser;

	@FXML
	private TableColumn<GameDisplayer, String> tcGameName;
	@FXML
	private TableColumn<GameDisplayer, String> tcUsername;

	@FXML
	private TableColumn<CharacterDisplayer, String> tcName;
	@FXML
	private TableColumn<CharacterDisplayer, String> tcRace;
	@FXML
	private TableColumn<CharacterDisplayer, String> tcClass;
	@FXML
	private TableColumn<CharacterDisplayer, String> tcLevel;
}
