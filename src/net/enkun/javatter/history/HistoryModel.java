package net.enkun.javatter.history;

import net.enkun.javatter.history.HistoryObject.EventType;
import twitter4j.Status;
import twitter4j.User;

public class HistoryModel implements HistoryLogic{
	
	private User user;
	private Status status;
	private EventType type;
	private HistoryViewObserver view;
	
	@Override
	public User getUser() {
		return this.user;
	}
	
	@Override
	public Status getStatus() {
		return this.status;
	}
	
	@Override
	public EventType getType() {
		return this.type;
	}
	
	@Override
	public void onRetweet(User user, Status status) {
		this.user = user;
		this.status = status;
		this.type = EventType.Retweet;
		view.update(this);
	}
	
	@Override
	public void onFavorite(User user, Status status) {
		this.user = user;
		this.status = status;
		this.type = EventType.Favorite;
		view.update(this);
	}
	
	@Override
	public void onUnfavorite(User user, Status status) {
		this.user = user;
		this.status = status;
		this.type = EventType.Unfavorite;
		view.update(this);
	}
	
	@Override
	public void onFollow(User user) {
		this.user = user;
		this.status = null;
		this.type = EventType.Follow;
		view.update(this);
	}
	
	@Override
	public void setView(HistoryViewObserver view) {
		this.view = view;
	}

}
