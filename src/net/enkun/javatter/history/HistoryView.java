package net.enkun.javatter.history;

import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import twitter4j.Status;
import twitter4j.User;

import net.enkun.javatter.history.HistoryObject.EventType;

import com.orekyuu.javatter.util.BackGroundColor;
import com.orekyuu.javatter.view.IJavatterTab;

public class HistoryView implements HistoryViewObserver, IJavatterTab, AdjustmentListener {

	private Component component;
	private JPanel history;
	private JScrollPane tp;
	
	private Queue<HistoryLogic> queue = new LinkedList<HistoryLogic>();
	private boolean queueFlag;
	private boolean queueEvent;
	private JPanel last;
	
	public HistoryView() {
		this.history = new JPanel();
		this.history.setBackground(BackGroundColor.color);
		this.history.setLayout(new BoxLayout(this.history, 3));
		tp = new JScrollPane(22, 31);
		tp.setViewportView(this.history);
		tp.getVerticalScrollBar().setUnitIncrement(20);
		tp.getVerticalScrollBar().addAdjustmentListener(this);
		this.component = tp;
	}
	
	@Override
	public void update(HistoryLogic model)
	{
		if ((model instanceof HistoryModel)) {
			if(tp.getVerticalScrollBar().getValue()==0&&!queueFlag){
				addObject((HistoryModel) model);
			}else{
				queue.add(model);
			}
		}
	}
	
	private JPanel createObject(HistoryModel model){
		User user = model.getUser();
		Status status = model.getStatus();
		EventType type = model.getType();
		HistoryObjectFactory factory = new HistoryObjectFactory(user, status, type);
		return (JPanel) factory.createHistoryObject().getComponent();
	}
	
	private void addObject(HistoryModel model){
		JPanel panel = createObject(model);
		panel.updateUI();
		if (this.history.getComponentCount() == 1000) this.history.remove(999);
		this.history.add(panel, 0);
		this.history.updateUI();
		last = panel;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent arg0) {
		if(arg0.getValue()==0){
			if(queueEvent){
				return;
			}
			queueEvent = true;
			Thread th=new Thread(){
				@Override
				public void run(){
					queueFlag=true;
					JPanel lastPanel = last;
					while(!queue.isEmpty()){
						addObject((HistoryModel) queue.poll());
					}
					queueFlag=false;
					if(lastPanel != null){
						tp.validate();
						tp.getVerticalScrollBar().setValue(lastPanel.getLocation().y);
					}
				}
			};
			th.start();
		}
		else{
			queueEvent = false;
		}
	}

	@Override
	public Component getComponent() {
		return this.component;
	}

}
