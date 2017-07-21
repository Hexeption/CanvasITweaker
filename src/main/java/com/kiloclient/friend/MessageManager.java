package com.kiloclient.friend;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.ui.interactable.slotlist.part.Message;

public class MessageManager {

	private static List<Message> messages = new CopyOnWriteArrayList<Message>();
	
	public static void addMessage(String kn, String n, String s, String i) {
		messages.add(new Message(n, s, i));
	}

	public static void loadMessages() {
		new Thread() {
			@Override
			public void run() {
				messages = APIHelper.getMessages(KiLO.getKiLO().getUserControl().clientID);
			}
		}.start();
	}
	
	public static List<Message> getList() {
		return messages;
	}
	
	public static void addMessage(Message m) {
		messages.add(m);
	}
	
	public static void addMessage(int index, Message m) {
		messages.add(index, m);
	}
	
	public static void removeMessage(Message m) {
		messages.remove(m);
	}
	
	public static void removeMessage(int index) {
		messages.remove(messages.get(index));
	}
	
	public static Message getMessage(int index) {
		if (messages.size() == 0) {
			return null;
		}
		return messages.get(index);
	}
	
	public static int getIndex(Message m) {
		return messages.indexOf(m);
	}
	
	public static int getSize() {
		return getList().size();
	}
}
