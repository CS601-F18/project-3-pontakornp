package cs601.project3;

public class SlackPostMessageAPI {
	private String url;
	private String method;
	private String token;
	private String channel;
	private String text;
	
	public SlackPostMessageAPI(String text) {
		Config config = new Config();
		config.setVariables();
		this.url = "https://slack.com/";
		this.method = "api/chat.postMessage";
		this.token = config.getToken();
		this.channel = config.getChannel();
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
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
	
	public String getTargetUrl() {
		return url + method;
	}
	
	public String getUrlParameters() {
		return "channel=" + channel + "&text=" + text;
	}

	
}
