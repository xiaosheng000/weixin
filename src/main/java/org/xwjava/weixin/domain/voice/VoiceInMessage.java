package org.xwjava.weixin.domain.voice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.xwjava.weixin.domain.InMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)//JAXB从字段获取配置
@XmlRootElement(name="xml")//JAXB读取xml时根元素名称
public class VoiceInMessage extends InMessage {

	@XmlElement(name="MediaId")
	@JsonProperty("MediaId")
	private String mediaId;
	
	@XmlElement(name="Format")
	@JsonProperty("Format")
	private String format;
	
	public VoiceInMessage() {
		super.setMsgType("voice");
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return "VoiceInMessage [mediaId=" + mediaId + ", format=" + format + ", getToUserName()=" + getToUserName()
				+ ", getFromUserName()=" + getFromUserName() + ", getCreateTime()=" + getCreateTime()
				+ ", getMsgType()=" + getMsgType() + ", getMsgId()=" + getMsgId() + "]";
	}
	
}
