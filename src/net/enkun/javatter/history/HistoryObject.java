package net.enkun.javatter.history;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import twitter4j.Status;
import twitter4j.User;

import com.orekyuu.javatter.controller.ProfileController;
import com.orekyuu.javatter.model.ProfileModel;
import com.orekyuu.javatter.plugin.JavatterPluginLoader;
import com.orekyuu.javatter.util.BackGroundColor;
import com.orekyuu.javatter.util.IconCache;
import com.orekyuu.javatter.view.IJavatterTab;
import com.orekyuu.javatter.view.ProfileView;

public class HistoryObject implements IJavatterTab, HyperlinkListener, MouseListener {
	private Component component;
	private User user;
	private Status status;
	private EventType type;
	private JPanel base;
	private JPanel imgPanel;
	
	public enum EventType {
		Retweet,
		Favorite,
		Unfavorite,
		Follow
	}

	protected HistoryObject(User user, Status status, EventType type){
		this.user = user;
		this.status = status;
		this.type = type;
	}
	
	/**
	 * コンポーネントを作成する
	 */
	protected void create(){
		component=createTweetObject();
	}
	
	private JPanel createTweetObject() {
		base = new JPanel();
		base.setBackground(BackGroundColor.color);
		base.setAlignmentX(0.0F);
		base.setAlignmentY(0.0F);
		base.setMaximumSize(new Dimension(375, Integer.MAX_VALUE));
		base.setLayout(new BorderLayout());

		base.add(createImage(), "Before");
		base.add(createText(), "Center");

		return base;
	}

	private JPanel createImage()
	{
		imgPanel = new JPanel();
		imgPanel.setBackground(BackGroundColor.color);
		imgPanel.setLayout(new BoxLayout(imgPanel, 3));
		try {
			imgPanel.add(createImageLabel(user));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		imgPanel.setAlignmentX(0.0F);
		imgPanel.setAlignmentY(0.0F);

		return imgPanel;
	}

	private JLabel createImageLabel(User user) throws MalformedURLException{
		IconCache cache = IconCache.getInstance();
		ImageIcon icon = cache.getIcon(new URL(user.getProfileImageURL()));

		JLabel label=new JLabel(icon);
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label.addMouseListener(this);
		return label;
	}

	private JPanel createText() {
		JPanel textPanel = new JPanel();
		textPanel.setMaximumSize(new Dimension(375, Integer.MAX_VALUE));
		textPanel.setLayout(new BoxLayout(textPanel, 3));

		JLabel userName = new JLabel();
		userName.setMaximumSize(new Dimension(275, Integer.MAX_VALUE));
		Font font = new Font("ＭＳ ゴシック", 1, 13);
		userName.setFont(font);
		userName.setText("@" + user.getScreenName() + "に" + type + "されました");
		textPanel.add(userName);

		JTextPane textArea = new JTextPane();
		String tweetText = this.status == null ? "" : this.status.getText();
		textArea.setContentType("text/html");
		textArea.setEditable(false);
		textArea.setText(createHTMLText(tweetText));
		textArea.setBackground(BackGroundColor.color);
		textArea.setAlignmentX(0.0F);
		textArea.setAlignmentY(0.0F);
		textArea.addHyperlinkListener(this);
		textPanel.add(textArea);

		textPanel.add(createButtons());
		textPanel.setAlignmentY(0.0F);
		textPanel.setAlignmentX(0.0F);
		textPanel.setBackground(BackGroundColor.color);

		return textPanel;
	}

	private String createHTMLText(String tweet) {
		String urlRegex = "(?<![\\w])https?://(([\\w]|[^ -~])+(([\\w\\-]|[^ -~])+([\\w]|[^ -~]))?\\.)+(aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|xxx|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|ss|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|za|zm|zw)(?![\\w])(/([\\w\\.\\-\\$&%/:=#~!]*\\??[\\w\\.\\-\\$&%/:=#~!]*[\\w\\-\\$/#])?)?";
		Pattern p = Pattern.compile(urlRegex);
		Matcher matcher = p.matcher(tweet);
		String t = tweet;
		while (matcher.find()) {
			String s = matcher.group();
			t = t.replaceFirst(s, "<a href='" + s + "'>" + s + "</a>");
		}
		t = t.replaceAll("\n", "<br>");
		return t;
	}

	private JPanel createButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, 2));
		panel.setAlignmentX(0.0F);
		panel.setAlignmentY(0.0F);

		return panel;
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			URL url = e.getURL();
			Desktop dp = Desktop.getDesktop();
			try {
				dp.browse(url.toURI());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public Component getComponent() {
		return component;
	}

	@Override
	public void mouseClicked(MouseEvent paramMouseEvent) {		
		ProfileView view=new ProfileView(JavatterPluginLoader.getTweetObjectBuilder());
		ProfileModel model=new ProfileModel();
		ProfileController controller=new ProfileController(user);
		view.setController(controller);
		model.setView(view);
		controller.setModel(model);
		controller.setView(view);

		controller.init();
	}

	@Override
	public void mousePressed(MouseEvent paramMouseEvent) {
	}

	@Override
	public void mouseReleased(MouseEvent paramMouseEvent) {
	}

	@Override
	public void mouseEntered(MouseEvent paramMouseEvent) {
	}

	@Override
	public void mouseExited(MouseEvent paramMouseEvent) {
	}

}
