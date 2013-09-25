package net.enkun.javatter.history;

import net.enkun.javatter.history.HistoryObject.EventType;
import twitter4j.Status;
import twitter4j.User;

public class HistoryObjectFactory {
	private User user;
	private Status status;
	private EventType type;
	
	public HistoryObjectFactory(User user, Status status, EventType type) {
		this.user = user;
		this.status = status;
		this.type = type;
	}
	
	public HistoryObject createHistoryObject() {
		HistoryObject object=new HistoryObject(user, status, type);
		object.create();
		return object;
	}
}
