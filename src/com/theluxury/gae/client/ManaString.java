package com.theluxury.gae.client;


public class ManaString {
	
	private String mana;
	private String untappedMana;
	private String tappedMana;
	
	public ManaString() {
		this.mana = "";
		this.untappedMana = "";
		this.tappedMana = "";
	}
	
	public ManaString(ManaString manaString, Land land, char c) {
		// Adds mana to can cast
    	this.mana = LandCounter.sortString(manaString.getMana() + c);
    	
    	// Then add mana to either tapped or untapped, make into fcn I guess. 
    	if (land.isComesIntoPlayTapped()) {
    		this.tappedMana = LandCounter.sortString(manaString.getTappedMana() + c);
    		this.untappedMana = manaString.getUntappedMana();
    	} else {
    		this.untappedMana = LandCounter.sortString(manaString.getUntappedMana() + c);
    		this.tappedMana = manaString.getTappedMana();
    	}
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ManaString))
			return false;
		
		ManaString manaString = (ManaString) object;
		return (this.getMana().equals(manaString.getMana()) && 
				this.getUntappedMana().equals(manaString.getUntappedMana()) && 
				this.getTappedMana().equals(manaString.getTappedMana()));
	}
	
	public String getMana() {
		return mana;
	}
	public void setMana(String mana) {
		this.mana = mana;
	}
	public String getUntappedMana() {
		return untappedMana;
	}
	public void setUntappedMana(String untappedMana) {
		this.untappedMana = untappedMana;
	}
	public String getTappedMana() {
		return tappedMana;
	}
	public void setTappedMana(String tappedMana) {
		this.tappedMana = tappedMana;
	}

}
