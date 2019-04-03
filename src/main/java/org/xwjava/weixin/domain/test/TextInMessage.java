package org.xwjava.weixin.domain.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.xwjava.weixin.domain.InMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)//JAXB从字段获取配置
@XmlRootElement(name="xml")//JAXB读取xml时根元素名称
public class TextInMessage extends InMessage {

	@XmlElement(name="Context")
	@JsonProperty("Content")
	private String context;
	
	public TextInMessage() {
		super.setMsgType("text");
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
	//如果想要打印一些有意义的消息出来，必须重写此方法，否则只能打印一个内存地址
	@Override
	public String toString() {
		return "TextInMessage [context=" + context + ", getToUserName()=" + getToUserName() + ", getFromUserName()="
				+ getFromUserName() + ", getCreateTime()=" + getCreateTime() + ", getMsgType()=" + getMsgType()
				+ ", getMsgId()=" + getMsgId() + "]";
	}
	
}
