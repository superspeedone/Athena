package com.superspeed.common.weixin.pojo;

public class BaseSendMessage {
	private Filter filter;
	private String msgtype;

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

}
