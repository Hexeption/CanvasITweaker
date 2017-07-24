package com.canvasclient.friend.party;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;

import java.util.ArrayList;

public class PartyManager {
	
	private ArrayList<Party> parties = new ArrayList();
	
	public PartyManager() {
	}
	
	public void updateParties() {
		this.parties = APIHelper.getParties(Canvas.getCanvas().getUserControl().clientID);
	}
	
	public Party getParty(int partyID) {
		Party party = null;
		for (Party p : this.parties) {
			if (p.partyID == partyID)
				party = p;
		}
		return party;
	}
	
	public void addParty(Party party) {
		this.parties.add(party);
	}
	
	public void removeParty(Party party) {
		this.parties.remove(party);
	}
	
	public ArrayList<Party> getParties() {
		return this.parties;
	}
	
}
