package net.enkun.javatter.history;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;

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
		if (status.isRetweet()) {
			if (isMe(status.getRetweetedStatus().getUser())) {
				model.onRetweet(status.getUser(), status.getRetweetedStatus());
			}
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
	
	@Override
	public void onDirectMessage(DirectMessage directMessage) {
		// TODO 検証
	}
	
	@Override
	public void onException(Exception ex) {
		ex.printStackTrace();
	}

	@Override
	public void onDeletionNotice(long directMessageId, long userId) {}
	
	@Override
	public void onBlock(User source, User blockedUser) {}
	
	@Override
	public void onFriendList(long[] friendIds) {}

	@Override
	public void onUnblock(User source, User unblockedUser) {}
	
	@Override
	public void onUserListCreation(User listOwner, UserList list) {}

	@Override
	public void onUserListDeletion(User listOwner, UserList list) {}

	@Override
	public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {}

	@Override
	public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {}

	@Override
	public void onUserListSubscription(User subscriber, User listOwner, UserList list) {}

	@Override
	public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {}

	@Override
	public void onUserListUpdate(User listOwner, UserList list) {}

	@Override
	public void onUserProfileUpdate(User updatedUser) {}

}
