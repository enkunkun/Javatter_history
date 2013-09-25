package net.enkun.javatter.history;

import net.enkun.javatter.history.HistoryObject.EventType;
import twitter4j.Status;
import twitter4j.User;

public interface HistoryLogic {
	
	public User getUser();
	
	public Status getStatus();
	
	public EventType getType();
	
	public void onRetweet(User user, Status status);

	public void onFavorite(User user, Status status) ;

	public void onUnfavorite(User user, Status status);

	public void onFollow(User user);
	
	public void setView(HistoryViewObserver view);
}
