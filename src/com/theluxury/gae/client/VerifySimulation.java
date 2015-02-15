package com.theluxury.gae.client;

import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VerifySimulation implements EntryPoint {
	private VerticalPanel mainPanel = new VerticalPanel();
	
	private HorizontalPanel cmcOfSpellPanel = new HorizontalPanel();
	private Label cmcOfSpellLabel = new Label("Casting cost of spell");
	private TextBox cmcOfSpellTextBox = new TextBox();

	@Override
	public void onModuleLoad() {
		console("loading verify");
		// TODO Auto-generated method stub
		mainPanel.add(cmcOfSpellPanel);
		
		cmcOfSpellPanel.add(cmcOfSpellLabel);
		cmcOfSpellPanel.add(cmcOfSpellTextBox);
		cmcOfSpellLabel.getElement().setClassName("landCounter-label");
		cmcOfSpellTextBox.getElement().setClassName("landCounter-form");
		cmcOfSpellTextBox.setWidth("100px");
		
		RootPanel.get("verification").add(mainPanel);
		
	}
	
	public static native void console(String text)
	/*-{
	    console.log(text);
	}-*/;
	
	

}
