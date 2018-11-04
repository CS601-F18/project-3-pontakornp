package cs601.project3;

public class SlackPostMessageAPI {
	private String url;
	private String token;
	private String channel;
	private String text;
	
	public SlackPostMessageAPI(String text) {
		Config config = new Config();
		config.setVariables();
		this.setUrl("https://slack.com/api/chat.postMessage");
		this.setToken(config.getToken());
		this.setChannel(config.getChannel());
		this.setText(text);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
