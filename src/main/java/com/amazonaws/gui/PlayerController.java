package com.amazonaws.gui;

import com.amazonaws.backend.User;
import com.amazonaws.backend.Weapon;
import com.amazonaws.backend.Character.StatTag;

import java.util.ArrayList;
import java.util.Map;

import com.amazonaws.backend.Character;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PlayerController extends WindowController{
	
	@FXML
	public void initialize() {
		User.getInstance().setController(this);
		
		lblGameName.setText(User.getInstance().getGame().getName());
		lblGamePassword.setText(User.getInstance().getGame().getPassword());
		
		tcName.setCellValueFactory(new PropertyValueFactory<CharacterDisplayer, String>("name"));
		tcClass.setCellValueFactory(new PropertyValueFactory<CharacterDisplayer, String>("pcClass"));
		tcLevel.setCellValueFactory(new PropertyValueFactory<CharacterDisplayer, String>("level"));
		tcHP.setCellValueFactory(new PropertyValueFactory<CharacterDisplayer, String>("hp"));
		
		update();
	}
	
	/*
	 * Updates information about everyone in the party
	 */
	public void updatePartyTable() {
		ObservableList<CharacterDisplayer> partyData = FXCollections.observableArrayList();
		for (Character c : User.getInstance().getParty()) {
			partyData.add(new CharacterDisplayer(c));
		}
		tblParty.setItems(partyData);
	}
	
	/*
	 * Updates informationa but the active character of the user
	 * NOTE all set functions are direct ports form old dungeon crawler
	 * may need to updated and rewrote eventually when UI overhaul comes
	 */
	public void updateCharacter() {
		setInventoryTab();
		setACTab();
		setWeaponsTab();
		setSkillsTab();
		setSpellSlots();
		setSpellsKnown();
		setFeatTab();
		setLootTab();
		setStatTab();
		setCharTab();
	}
	
	private void setCharTab() {
		Character c = User.getInstance().getActiveCharacter();
		raceLab.setText("   " + c.getStat(StatTag.RACE));
		clasLab.setText("   " + c.getStat(StatTag.CLASS));
		nameLab.setText("   " + c.getStat(StatTag.NAME));
		hitpLab.setText("   " + c.getStat(StatTag.CURRENT_HP) + "/" + c.getStat(StatTag.TOTAL_HP));
		leveLab.setText("   " + c.getStat(StatTag.LEVEL));
		profBonLab.setText("   " + c.getStat(StatTag.PROF_BON));
	}
	
	private void setStatTab() {
		Character c = User.getInstance().getActiveCharacter();
		streLab.setText("     " + c.getStat(StatTag.STR) + "(+"+c.getStat(StatTag.STR_BON)+")");
		dextLab.setText("     " + c.getStat(StatTag.DEX) + "(+"+c.getStat(StatTag.DEX_BON)+")");
		inteLab.setText("     " + c.getStat(StatTag.INT) + "(+"+c.getStat(StatTag.INT_BON)+")");
		consLab.setText("     " + c.getStat(StatTag.CON) + "(+"+c.getStat(StatTag.CON_BON)+")");
		charLab.setText("     " + c.getStat(StatTag.CHA) + "(+"+c.getStat(StatTag.CHA_BON)+")");
		wisdLab.setText("     " + c.getStat(StatTag.WIS) + "(+"+c.getStat(StatTag.WIS_BON)+")");	
	}
	
	private void setLootTab() {
		//TODO add support for gold and exp in character
	}
	
	private void setFeatTab() {
		StringBuilder sb = new StringBuilder();
		for (String f : User.getInstance().getActiveCharacter().getFeats()) {
			sb.append(f);
			sb.append('\n');
		}
		featAre.setText(sb.toString());
	}
	
	private void setSpellsKnown() {
		Map<Integer, ArrayList<String>> spellMap = User.getInstance().getActiveCharacter().getSpellList();
		
		int index = 0;
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < spellMap.get(index).size(); i++) {
			String spell = spellMap.get(index).get(i);
			sb.append(spell);
			if (i != spellMap.get(index).size()-1) {
				sb.append(", ");
			}
		}
		leve0SpelFie.setText(sb.toString());
		index++;
		
		sb = new StringBuilder();
		for (int i = 0; i < spellMap.get(index).size(); i++) {
			String spell = spellMap.get(index).get(i);
			sb.append(spell);
			if (i != spellMap.get(index).size()-1) {
				sb.append(", ");
			}
		}
		leve1SpelFie.setText(sb.toString());
		index++;
		
		sb = new StringBuilder();
		for (int i = 0; i < spellMap.get(index).size(); i++) {
			String spell = spellMap.get(index).get(i);
			sb.append(spell);
			if (i != spellMap.get(index).size()-1) {
				sb.append(", ");
			}
		}
		leve2SpelFie.setText(sb.toString());
		index++;
		
		sb = new StringBuilder();
		for (int i = 0; i < spellMap.get(index).size(); i++) {
			String spell = spellMap.get(index).get(i);
			sb.append(spell);
			if (i != spellMap.get(index).size()-1) {
				sb.append(", ");
			}
		}
		leve3SpelFie.setText(sb.toString());
		index++;
		
		sb = new StringBuilder();
		for (int i = 0; i < spellMap.get(index).size(); i++) {
			String spell = spellMap.get(index).get(i);
			sb.append(spell);
			if (i != spellMap.get(index).size()-1) {
				sb.append(", ");
			}
		}
		leve4SpelFie.setText(sb.toString());
		index++;
		
		sb = new StringBuilder();
		for (int i = 0; i < spellMap.get(index).size(); i++) {
			String spell = spellMap.get(index).get(i);
			sb.append(spell);
			if (i != spellMap.get(index).size()-1) {
				sb.append(", ");
			}
		}
		leve5SpelFie.setText(sb.toString());
		index++;
		
		sb = new StringBuilder();
		for (int i = 0; i < spellMap.get(index).size(); i++) {
			String spell = spellMap.get(index).get(i);
			sb.append(spell);
			if (i != spellMap.get(index).size()-1) {
				sb.append(", ");
			}
		}
		leve6SpelFie.setText(sb.toString());
		index++;
		
		sb = new StringBuilder();
		for (int i = 0; i < spellMap.get(index).size(); i++) {
			String spell = spellMap.get(index).get(i);
			sb.append(spell);
			if (i != spellMap.get(index).size()-1) {
				sb.append(", ");
			}
		}
		leve7SpelFie.setText(sb.toString());
		index++;
		
		sb = new StringBuilder();
		for (int i = 0; i < spellMap.get(index).size(); i++) {
			String spell = spellMap.get(index).get(i);
			sb.append(spell);
			if (i != spellMap.get(index).size()-1) {
				sb.append(", ");
			}
		}
		leve8SpelFie.setText(sb.toString());
		index++;
		
		sb = new StringBuilder();
		for (int i = 0; i < spellMap.get(index).size(); i++) {
			String spell = spellMap.get(index).get(i);
			sb.append(spell);
			if (i != spellMap.get(index).size()-1) {
				sb.append(", ");
			}
		}
		leve9SpelFie.setText(sb.toString());
	}
	
	private void setSpellSlots() {
		//TODO SET SPELL SLOTS
	}
	
	private void setSkillsTab() {
		athlBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.ATHLETICS));
		acroBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.ACROBATICS));
		sleiHandBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.SLEIGHT_OF_HAND));
		steaBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.STEALTH));
		arcaBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.ARCANA));
		histBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.HISTORY));
		inveBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.INVESTIGATION));
		natuBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.NATURE));
		reliBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.RELIGION));
		animHandBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.ANIMAL_HANDLING));
		insiBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.INSIGHT));
		mediBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.MEDICINE));
		percBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.PERCEPTION));
		survBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.SURVIVAL));
		deceBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.DECEPTION));
		intiBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.INTIMIDATION));
		perfBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.PERFORMANCE));
		persBonLab.setText("+" + User.getInstance().getActiveCharacter().getStat(StatTag.PERSUASION));
	}
	
	private void setWeaponsTab() {
		ArrayList<Weapon> w = User.getInstance().getActiveCharacter().getWeapons();
		if (w.size() > 0) {
			Weapon wep = w.get(0);
			weap1NameLab.setText("   " +wep.name);
			weap1HitLab.setText(User.getInstance().getActiveCharacter().getHitBonus(wep));
			weap1DamaLab.setText(User.getInstance().getActiveCharacter().getDamage(wep));
		}
		if (w.size() > 1) {
			Weapon wep = w.get(1);
			weap2NameLab.setText("   " +wep.name);
			weap2HitLab.setText(User.getInstance().getActiveCharacter().getHitBonus(wep));
			weap2DamaLab.setText(User.getInstance().getActiveCharacter().getDamage(wep));
		}
		if (w.size() > 2) {
			Weapon wep = w.get(2);
			weap3NameLab.setText("   " +wep.name);
			weap3HitLab.setText(User.getInstance().getActiveCharacter().getHitBonus(wep));
			weap3DamaLab.setText(User.getInstance().getActiveCharacter().getDamage(wep));
		}
	}
	
	private void setInventoryTab() {
		StringBuilder sb = new StringBuilder();
		for (String i : User.getInstance().getActiveCharacter().getItems()) {
			sb.append(i);
			sb.append('\n');
		}
		inveFie.setText(sb.toString());
	}
	
	private void setACTab(){
		acLab.setText(User.getInstance().getActiveCharacter().getStat(StatTag.AC));
	}
	
	
	/*
	 * Updates the displayed information in the controller
	 */
	@Override
	public void update() {
		updatePartyTable();
		updateCharacter();
	}
	
	@FXML
	private Label lblGameName;
	@FXML
	private Label lblGamePassword;
	
	@FXML
	private TableView<CharacterDisplayer> tblParty;
	@FXML
	private TableColumn<CharacterDisplayer, String> tcName;
	@FXML
	private TableColumn<CharacterDisplayer, String> tcClass;
	@FXML
	private TableColumn<CharacterDisplayer, String> tcLevel;
	@FXML
	private TableColumn<CharacterDisplayer, String> tcHP;
	
	@FXML
    private TextArea inveFie;
    @FXML
    private TextField enteFie;
    @FXML
    private TextArea commAre;
    @FXML
    private TextArea encoAre;
    @FXML
    private TextArea enviAre;
    @FXML
    private TextArea objeAre;
    @FXML
    private TextArea suggActiAre;
    @FXML
    private TextArea featAre;
    @FXML
    private Label leve1SlotLab;
    @FXML
    private Label leve2SlotLab;
    @FXML
    private Label leve3SlotLab;
    @FXML
    private Label leve4SlotLab;
    @FXML
    private Label leve5SlotLab;
    @FXML
    private Label leve6SlotLab;
    @FXML
    private Label leve7SlotLab;
    @FXML
    private Label leve8SlotLab;
    @FXML
    private Label leve9SlotLab;
    @FXML
    private TextField leve0SpelFie;
    @FXML
    private TextField leve1SpelFie;
    @FXML
    private TextField leve2SpelFie;
    @FXML
    private TextField leve3SpelFie;
    @FXML
    private TextField leve4SpelFie;
    @FXML
    private TextField leve5SpelFie;
    @FXML
    private TextField leve6SpelFie;
    @FXML
    private TextField leve7SpelFie;
    @FXML
    private TextField leve8SpelFie;
    @FXML
    private TextField leve9SpelFie;

    @FXML
    private Label weap1NameLab;
    @FXML
    private Label weap1HitLab;
    @FXML
    private Label weap1DamaLab;
    @FXML
    private Label weap2NameLab;
    @FXML
    private Label weap2HitLab;
    @FXML
    private Label weap2DamaLab;
    @FXML
    private Label weap3NameLab;
    @FXML
    private Label weap3HitLab;
    @FXML
    private Label weap3DamaLab;
    
    @FXML
    private Label animHandBonLab;
    @FXML
    private Label arcaBonLab;
    @FXML
    private Label athlBonLab;
    @FXML
    private Label acroBonLab;
    @FXML
    private Label deceBonLab;
    @FXML
    private Label histBonLab;
    @FXML
    private Label insiBonLab;
    @FXML
    private Label intiBonLab;
    @FXML
    private Label inveBonLab;
    @FXML
    private Label mediBonLab;
    @FXML
    private Label natuBonLab;
    @FXML
    private Label percBonLab;
    @FXML
    private Label perfBonLab;
    @FXML
    private Label persBonLab;
    @FXML
    private Label reliBonLab;
    @FXML
    private Label sleiHandBonLab;
    @FXML
    private Label steaBonLab;
    @FXML
    private Label survBonLab;

    @FXML
    private Label raceLab;
    @FXML
    private Label clasLab;
    @FXML
    private Label nameLab;
    @FXML
    private Label hitpLab;
    @FXML
    private Label leveLab;
    @FXML
    private Label profBonLab;
    @FXML
    private Label streLab;
    @FXML
    private Label dextLab;
    @FXML
    private Label inteLab;
    @FXML
    private Label consLab;
    @FXML
    private Label charLab;
    @FXML
    private Label wisdLab;
    @FXML
    private Label goldLab;
    @FXML
    private Label expLab;
    
    @FXML
    private Label acLab;
}
