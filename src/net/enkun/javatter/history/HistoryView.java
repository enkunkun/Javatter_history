package net.enkun.javatter.history;

import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import net.enkun.javatter.history.HistoryObjectFactory.EventType;

import com.orekyuu.javatter.logic.UserStreamLogic;
import com.orekyuu.javatter.model.TimeLineModel;
import com.orekyuu.javatter.util.BackGroundColor;
import com.orekyuu.javatter.util.TweetObjectFactory;
import com.orekyuu.javatter.view.IJavatterTab;

import twitter4j.Status;
import twitter4j.User;

public class HistoryView implements HistoryViewObserver, IJavatterTab, AdjustmentListener {

	private Component component;
	private JPanel history;
	private HistoryViewObserver observer;
	private JScrollPane tp;
	
	//Private Queue<Status> = new LinkedList<status>();
	
	public HistoryView() {
		this.history = new JPanel();
		this.history.setBackground(BackGroundColor.color);
		this.history.setLayout(new BoxLayout(this.history, 3));
		this.tp = new JScrollPane(22, 31);
		this.tp.setViewportView(this.history);
		this.tp.getVerticalScrollBar().setUnitIncrement(20);
		this.tp.getVerticalScrollBar().addAdjustmentListener(this);
		this.component = tp;
		//this.observer = observer;
	}
	
	private void addObject(User user, Status status, EventType type){
		HistoryObjectFactory factory = new HistoryObjectFactory(user, status, type);
		JPanel panel = factory.createHistoryObject(this.observer);
		panel.updateUI();
		if (this.history.getComponentCount() == 1000) this.history.remove(999);
		this.history.add(panel, 0);
		this.history.updateUI();
	}
	
	@Override
	public void update(HistoryLogic model)
	{
		if ((model instanceof HistoryModel)) {
//			if(tp.getVerticalScrollBar().getValue()==0){
				addObject(model.getUser(), model.getStatus(), model.getType());
//			}else{
//				queue.add(model.getStatus());
//				setNumber(queue.size());
//			}
		}
	}
		
	private void setNumber(int num){
		JTabbedPane tab=(JTabbedPane) component.getParent();
		Pattern p=Pattern.compile("^History(\\(\\d+\\))?$");
		for(int i=0;i<tab.getTabCount();i++){
			if(p.matcher(tab.getTitleAt(i)).matches()){
				if(num!=0){
					tab.setTitleAt(i, "History("+num+")");
				}else{
					tab.setTitleAt(i, "History");
				}
			}
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent arg0) {
//		if(arg0.getValue()==0){
//			while(!queue.isEmpty()){
//				addObject(queue.poll());
//			}
//			setNumber(0);
//		}
	}

	@Override
	public Component getComponent() {
		// TODO 自動生成されたメソッド・スタブ
		return this.component;
	}
	
	

}
