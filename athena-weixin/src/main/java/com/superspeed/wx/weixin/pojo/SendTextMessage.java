package com.superspeed.common.weixin.pojo;

public class SendTextMessage extends BaseSendMessage {
	private Text text;

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

}
