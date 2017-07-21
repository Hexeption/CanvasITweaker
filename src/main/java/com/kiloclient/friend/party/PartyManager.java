package com.kiloclient.friend.party;

import java.util.ArrayList;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;

public class PartyManager {
	
	private ArrayList<Party> parties = new ArrayList();
	
	public PartyManager() {
	}
	
	public void updateParties() {
		this.parties = APIHelper.getParties(KiLO.getKiLO().getUserControl().clientID);
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
