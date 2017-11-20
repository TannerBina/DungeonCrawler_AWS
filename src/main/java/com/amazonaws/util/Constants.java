package com.amazonaws.util;

import java.util.Scanner;

public class Constants {
	public static final boolean DEBUG = true;
	
	public static final String LAMBDA_DELIMINATOR = "__";
	public static final String CHAR_ID_TAG = "CHAR_ID";
	public static final String NULL = "NULL";
	
	public static final String WIN_USER = "userWin.fxml";
	public static final String WIN_LOGIN = "loginWin.fxml";
	public static final String WIN_CREATE_GAME = "createGameWin.fxml";
	public static final String WIN_PLAYER = "playerWin.fxml";
	public static final String WIN_DM = "dmWin.fxml";
	
	public static final String[] WINS_CREATE_CHAR = {
		"nameWin.fxml", "raceWin.fxml", "classWin.fxml",
		"backgroundWin.fxml", "statWin.fxml", "alignmentWin.fxml",
		"profWin.fxml", "featWin.fxml", "weaponWin.fxml", "armorWin.fxml",
		"itemWin.fxml", "spellWin.fxml"
	};
}
