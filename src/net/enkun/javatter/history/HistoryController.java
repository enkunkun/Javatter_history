package net.enkun.javatter.history;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import com.orekyuu.javatter.account.TwitterManager;
import com.orekyuu.javatter.controller.UserStreamController;

public class HistoryController extends UserStreamController {
	
	private HistoryLogic model;
	
	public void setModel(HistoryLogic model) {
		this.model = model;
	}
	
	public boolean isMe(User user) {
		try {
			return user.getScreenName().equals(TwitterManager.getInstance().getTwitter().getScreenName());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//被リツイート
	@Override
	public void onStatus(Status status) {
		if (status.isRetweet() && isMe(status.getRetweetedStatus().getUser())) {
			model.onRetweet(status.getUser(), status.getRetweetedStatus());
		}
	}

	@Override
	public void onFavorite(User source, User target, Status favoritedStatus) {
		if (!isMe(source)) {
			model.onFavorite(source, favoritedStatus);
		}
	}


	@Override
	public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
		if (!isMe(source)) {
			model.onUnfavorite(source, unfavoritedStatus);
		}
	}

	@Override
	public void onFollow(User source, User followedUser) {
		if (!isMe(source)) {
			model.onFollow(source);
		}
	}
	
	//TODO
	@Override
	public void onDirectMessage(DirectMessage directMessage) {}
	
	@Override
	public void onBlock(User source, User blockedUser) {}

}
