package net.enkun.javatter.history;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.orekyuu.javatter.viewobserver.UserEventViewObserver;

import twitter4j.Status;
import twitter4j.User;

public class ButtonClickEventListener implements ButtonClickEventLogic, ActionListener {

	private User user;
	private Status status;
	private UserEventViewObserver view;
	
	public enum Buttons{
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}

	@Override
	public void setHogeButton(Object obj) {
		// TODO 自動生成されたメソッド・スタブ
		
	}


}
