package com.theluxury.gae.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.constants.Placement;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LandCounter implements EntryPoint {
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel deckSizePanel = new HorizontalPanel();
	private Label deckSizeLabel = new Label("Size of your deck");
	private TextBox deckSizeBox = new TextBox();
	
	private HorizontalPanel castingCostPanel = new HorizontalPanel();
	private Label castingCostLabel = new Label("Casting cost of spell");
	private TextBox castingCostBox = new TextBox();
	private Tooltip castingCostTooltip = new Tooltip(castingCostBox);
	
	private HorizontalPanel numberOfCardsPanel = new HorizontalPanel();
	private Label numberOfCardsLabel = new Label("The number of cards of this cmc");
	private TextBox numberOfCardsBox = new TextBox();
	private Tooltip numberOfCardsTooltip = new Tooltip(numberOfCardsBox);
	
	private HorizontalPanel turnPanel = new HorizontalPanel();
	private Label turnLabel = new Label("Turn you want to cast spell by");
	private TextBox turnBox = new TextBox();
	
	private HorizontalPanel playOrDrawPanel = new HorizontalPanel();
	private Label playOrDrawLabel = new Label("You are on the");
	private Button playOrDrawButton = new Button("Play");
	private ButtonGroup playOrDrawGroup = new ButtonGroup();
	private DropDownMenu playOrDrawMenu = new DropDownMenu();
	
	private FlexTable landsFlexTable = new FlexTable();
	private Label numberOfLandsLabel = new Label("Number of this land");
	private Label manaOfLandLabel = new Label("Mana this land can produce");
	private Tooltip manaOfLandTooltip = new Tooltip();
	private Button addLandButton = new Button("Add Land");
	private Button removeLandButton = new Button("Remove Land");
	
	private HorizontalPanel calculatePanel = new HorizontalPanel();
	private Button calculateButton = new Button("Calculate Odds");
	private Label percentagLabel = new Label();
	
	
	public void onModuleLoad() {
		console("loading landcounter");
		
		landsFlexTable.setWidget(0,0,numberOfLandsLabel);
		landsFlexTable.setWidget(0,1,manaOfLandLabel);
		numberOfLandsLabel.getElement().setClassName("landCounter-label");
		manaOfLandLabel.getElement().setClassName("landCounter-label");
		landsFlexTable.setWidget(0,2, addLandButton);
		landsFlexTable.setWidget(0, 3, removeLandButton);
		removeLandButton.getElement().setId("marginLeft");
		
		mainPanel.add(deckSizePanel);
		mainPanel.add(castingCostPanel);
		mainPanel.add(numberOfCardsPanel);
		mainPanel.add(turnPanel);
		mainPanel.add(playOrDrawPanel);
		mainPanel.add(landsFlexTable);
		mainPanel.add(calculatePanel);
		mainPanel.getElement().setId("marginMegaTop");
		
		
		deckSizePanel.add(deckSizeLabel);
		deckSizePanel.add(deckSizeBox);
		deckSizeLabel.getElement().setClassName("landCounter-label");
		deckSizeBox.getElement().setClassName("landCounter-form");
		
		castingCostPanel.add(castingCostLabel);
		castingCostPanel.add(castingCostBox);
		castingCostLabel.getElement().setClassName("landCounter-label");
		castingCostTooltip.setText("Use c for coloress, "
			+ "b for black, g for green, r for red, u for blue, and w for white. "
			+ "For example, a 2 colorless 1 green 1 blue spell would be ccgu");
		castingCostTooltip.setPlacement(Placement.RIGHT);
		// castingCostTooltip.reconfigure();
		castingCostBox.getElement().setClassName("landCounter-form");
		castingCostBox.setWidth("100px");

		
		numberOfCardsPanel.add(numberOfCardsLabel);
		numberOfCardsPanel.add(numberOfCardsBox);
		numberOfCardsLabel.getElement().setClassName("landCounter-label");
		numberOfCardsBox.getElement().setClassName("landCounter-form");
		numberOfCardsTooltip.setText("Put the number of equivalent cards. "
				+ "For example, if you have 8 creatures of this cmc and you "
				+ "don't care which one you casts, put in 8.");
		numberOfCardsTooltip.setPlacement(Placement.RIGHT);
		
		turnPanel.add(turnLabel);
		turnPanel.add(turnBox);
		turnLabel.getElement().setClassName("landCounter-label");
		turnBox.getElement().setClassName("landCounter-form");
		
		playOrDrawPanel.add(playOrDrawLabel);
		playOrDrawPanel.add(playOrDrawGroup);
		playOrDrawLabel.getElement().setClassName("landCounter-label");
		playOrDrawGroup.add(playOrDrawButton);
		playOrDrawButton.getElement().setClassName("btn btn-default dropdown-toggle");
		playOrDrawButton.setDataToggle(Toggle.DROPDOWN);
		
		playOrDrawGroup.add(playOrDrawMenu);
		
		AnchorListItem play = new AnchorListItem();
		play.setText("Play");
		AnchorListItem draw = new AnchorListItem();
		draw.setText("Draw");
		playOrDrawMenu.add(play);
		playOrDrawMenu.add(draw);
		
		play.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				playOrDrawButton.setText("Play");
			}
		});
		
		draw.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				playOrDrawButton.setText("Draw");
			}
		});
		
		calculatePanel.add(calculateButton);
		calculatePanel.add(percentagLabel);
		
		calculateButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				float percentage = getOdds();
				percentage *= (float) 100.0;
				console(percentage);
				percentagLabel.setText(String.valueOf(percentage) + "%");
				if (percentage < 33.3)
					percentagLabel.getElement().setClassName("label label-danger custom-label");
				else if (percentage < 66.6)
					percentagLabel.getElement().setClassName("label label-warning custom-label");
				else
					percentagLabel.getElement().setClassName("label label-success custom-label");
			}
		});
		

		
		
		
		addLandButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addLand();
			}
		});
		
		removeLandButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeLand();
			}
		});
		
		RootPanel.get("landCounter").add(mainPanel);
		RootPanel.get("verification").add(mainPanel);
		
		
		
		// Everything below is for the test class. Bleh, there has to be a better way...
		
	}
	
	private void addLand() {
		int row = landsFlexTable.getRowCount();
		TextBox numberOfLandsTextBox = new TextBox();
		numberOfLandsTextBox.getElement().setClassName("landCounter-form");
		
		TextBox manaOfThisLandTextBox = new TextBox();
		manaOfThisLandTextBox.getElement().setClassName("landCounter-form");
		
		CheckBox comesIntoPlayTapped = new CheckBox();
		Label comesIntoPlayTappedLabel = new Label("Check if comes into play tapped.");
		comesIntoPlayTappedLabel.getElement().setId("marginLeft");
		comesIntoPlayTappedLabel.getElement().setClassName("label label-info");
		
		// Order here matters. need to set widget of tooltip before putting in table. 
		if (row == 1)  {
			manaOfLandTooltip.setText("For example, for Volcanic Island, put ru");
			manaOfLandTooltip.setPlacement(Placement.RIGHT);
			manaOfLandTooltip.setWidget(manaOfThisLandTextBox);
		}
		
		landsFlexTable.setWidget(row, 0, numberOfLandsTextBox);
		landsFlexTable.setWidget(row, 1, manaOfThisLandTextBox);
		landsFlexTable.setWidget(row, 2, comesIntoPlayTapped);
		if (row == 1)
			landsFlexTable.setWidget(row, 3, comesIntoPlayTappedLabel);
		
	}

	private void removeLand() {
		int row = landsFlexTable.getRowCount();
		if (row != 1)
			landsFlexTable.removeRow(row - 1);
	}
	
	private float getOdds() {
		int playOrDraw;
		if (playOrDrawButton.getText().equalsIgnoreCase("Play"))
			playOrDraw = 0;
		else
			playOrDraw = 1;
				
		int turnWantToCast = Integer.valueOf(turnBox.getValue());
		int sizeOfDeck = Integer.valueOf(deckSizeBox.getValue());
				
		// Make a hashmap linking the number of the land (for the random number 
		// generator) and the mana it can produce. 
		// Also, should indicate if the land is a tapped land. 
		HashMap<Integer, Land> landMap = new HashMap<Integer, Land>();
		int landMapInt = 0;
				
		// row count starts since the first row is the labels. 
		for (int i = 1; i < landsFlexTable.getRowCount(); i++) {
			for (int j = 0; j < Integer.valueOf(((TextBox) landsFlexTable.getWidget(i, 0)).getValue()); j++) {
				Land land = new Land();
				land.setMana(((TextBox) landsFlexTable.getWidget(i, 1)).getValue().toLowerCase());
				land.setComesIntoPlayTapped(((CheckBox)landsFlexTable.getWidget(i, 2)).getValue());
				landMap.put(landMapInt, land);
				landMapInt++;
			}
		}
				
		HashSet<Integer> spellsMap = new HashSet<Integer>();
		// Alright, finally dealing with whether or not you draw the card..
		int numberOfSpells = Integer.valueOf(numberOfCardsBox.getValue());
				
		// Check to see if they put in a real number of spells so it doesn't
		// infinite loop. 
		if (numberOfSpells <= 0)
			return 0;
				
		// check to see if deck size is less than number of lands and spells
		// This should be right? landMapInt should be number of lands. 
		if (sizeOfDeck < landMapInt + numberOfSpells) {
			return 0;
		}
				
		for (int i = landMapInt; i < landMapInt + numberOfSpells; i++) {
			spellsMap.add(i);
		}
		
		float numberOfSuccess = 0;
		// just calling rand once here might be more accurate. 
		Random rand = new Random() ;
		for (int i = 0; i < 1000; i++) {
			if (runSimulation(rand, sizeOfDeck,landMap, playOrDraw, 
				turnWantToCast, spellsMap))
				numberOfSuccess++;
		}
		
		return numberOfSuccess / (float) 1000;
	}
	
	private boolean runSimulation(Random rand, int sizeOfDeck, HashMap<Integer, Land> landMap,
			int playOrDraw, int turnWantToCast, HashSet<Integer> spellsMap ) {
		
		HashSet<Integer> pickedSet;
		boolean contains = false;
		// Have to check if you drew the specified card. 
		int lastPickedCard = -1;
		do {
			int randomCard;
			// Is 8 here because you do a -- at the beginning
			int initialNumberOfCardsInHand = 8;
			int initialNumberOfLands = 0;
			// This checks for mulligan
			do {
				// Need set of numbers that have already been picked. 
				pickedSet = new HashSet<Integer>();
				initialNumberOfCardsInHand--;
				for (int i = 0; i < initialNumberOfCardsInHand; i++) {
					do {
						randomCard = rand.nextInt(sizeOfDeck);
					} while (pickedSet.contains(randomCard));
					pickedSet.add(randomCard);
				}
				// Counts number of lands
				for (int card : pickedSet) {
					if (landMap.containsKey(card)) {
						initialNumberOfLands++;
					}
				}
			} while (checkForMulligan(initialNumberOfLands, initialNumberOfCardsInHand));
			// TODO: Put the old thing back in, this is just to test comes into play tapped.
				// (checkForMulligan(initialNumberOfLands, initialNumberOfCardsInHand));
			
			// Then, get the rest of the cards. 
			// minus 1 here since if turn 1, you don't actually have a card. 
			for (int i = 0; i < playOrDraw + turnWantToCast - 1; i++) {
				do {
					randomCard = rand.nextInt(sizeOfDeck);
				} while (pickedSet.contains(randomCard));
				pickedSet.add(randomCard);
				// get the last land. 
				if (i == playOrDraw + turnWantToCast - 2)
					lastPickedCard = randomCard;
			}
			
			// This checks if you draw the card. If not, then, um... redo it. 
			for (int spellCard : spellsMap) {
				if (pickedSet.contains(spellCard)) {
					contains = true;
					break;
				}
			}
		} while (!contains);
		
		// Mark the cards that have been picked to the land. 

		List<Land> pickedLands = new ArrayList<Land>();
		for (int card : pickedSet) {
			if (landMap.containsKey(card)) {
				// So shouldn't add the last land if it comes into play tapped.
				if (card == lastPickedCard && landMap.get(card).isComesIntoPlayTapped())
					continue;
				pickedLands.add(landMap.get(card));
			}
		}
		
		// console("lastpickedcard is " + lastPickedCard + " and pickedSet is " + pickedSet + " and pickedLands is " + pickedLands);
		
		// Okay, now let's see if picked set can cast the spell. 
		String cmcOfSpell = sortString(castingCostBox.getValue().toLowerCase());
		// Sort the string.
		String sortedCmcOfSpell = sortString(cmcOfSpell);
		
		
		// Make it so if there is less lands than total cmc, return false. 
		if (pickedLands.size() < cmcOfSpell.length()) {
			return false;
		}
		
		
		// Also, if number of picked lands is exact same as cmc which is same as turn
		// then also no go if all the lands are tapped. This seems fringe, but still
		// faster than simulation I bet.
		if (pickedLands.size() == cmcOfSpell.length() 
				&& cmcOfSpell.length() >= turnWantToCast)
		{
			boolean allTapped = true;
			for (Land land : pickedLands) {
				if (!land.isComesIntoPlayTapped())
					allTapped = false;
			}
			if (allTapped)
				return false;
		}
		
		
		// new Land Strings is the "temp" permutation of all the mana it can cast. 
		
		HashSet<ManaString> oldManaStrings = new HashSet<ManaString>();
		ManaString blankString = new ManaString();
		oldManaStrings.add(blankString);
		HashSet<ManaString> newManaStrings = new HashSet<ManaString>();
		
		for (Land land : pickedLands) {
		    int manaLength = land.getMana().length();
		    // so there are going to be old length * manaLength new permutations of
		    // can cast. Mana length is the mumber of kids of mana this land can have.

		    // So for each mana kind, run through all the old strings, and add 
		    // that mana kind. 
		    for (int i = 0; i < manaLength; i++){
		        char c = land.getMana().charAt(i); 
		        
		        for (ManaString manaString : oldManaStrings) {
		        	
		        	ManaString newManaString = new ManaString(manaString, land, c);
		        	newManaStrings.add(newManaString);
		        }
		        // For loop looks like this to run through each c j times, and the 
		        // j % manaLength makes sure each set of old permutations get c
		        // added to it once. 
//		        for (String oldString : oldLandsStrings) {
//		        	String sortedString = sortString(oldString + c);
//		        	newLandsStrings.add(sortedString);
//		        }
		    }
		    
		    
		    oldManaStrings  = newManaStrings;
		    newManaStrings = new HashSet<ManaString>();
		}
		
		
		// Array of the non colorless casting cost of spell. 
		ArrayList<String> regexString = splitStringSinceRegexIsTooHard(sortedCmcOfSpell);
		// See if any of the old lands Strings contains the regexString
		for (ManaString manaString : oldManaStrings) {
			console("manaString is: " + manaString.getMana() + " and untapped is:" + 
		manaString.getUntappedMana() + " and tapped is:" + manaString.getTappedMana());
			boolean stringContains = true;
			for (String castingCostString : regexString) 
				if (!manaString.getMana().contains(castingCostString)) {
					stringContains = false;
				}
			if (stringContains) {
				// So doesn't automatically return true now. Have to check if
				// at least one of the soruces come from untapped 
				// if cmc == turn want to cast.
				if (cmcOfSpell.length() >= turnWantToCast && !cmcOfSpell.contains("c")) {
					for (String castingCostString : regexString) 
						if (manaString.getUntappedMana().contains(String.valueOf(castingCostString.charAt(0)))) {
							return true;
						}
					// got here, means does not contain?
					return false;
				}
				return true;
			}
		}
		return false;
		
	}
	
	private boolean checkForMulligan(int numberOfLands, int numberOfCards) {
		if ((numberOfLands < 2 || numberOfLands > 5) && numberOfCards > 5)
			return true;
		
		return false;
	}
	
	public static String sortString(String text) {
		char[] chars = text.toCharArray();
	     Arrays.sort(chars);
	     String sorted = new String(chars);
	     return sorted;
	}
	
	public static ArrayList<String> splitStringSinceRegexIsTooHard(String string) {

		int j = 0;
		ArrayList<String> newManaStringList = new ArrayList<String>();
		String newManaString = "";
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			// This works since charAt(i+1) for the last one is null I guess. 
			if (c != string.charAt(i+1)) {
				while(j<=i) {
					newManaString = newManaString + string.charAt(j);
					j++;
				}
				// make it so it doesn't add the "c" string
				if (!newManaString.contains("c")) {
					newManaStringList.add(newManaString);
				}
				newManaString = "";
			}
		}
		
		return newManaStringList;
	}
	
	public static native void console(String text)
	/*-{
	    console.log(text);
	}-*/;
	
	public static native void console(int text)
	/*-{
	    console.log(text);
	}-*/;
	
	public static native void console(float text)
	/*-{
	    console.log(text);
	}-*/;
	
}


