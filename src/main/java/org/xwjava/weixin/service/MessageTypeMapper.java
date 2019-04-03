package org.xwjava.weixin.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.xwjava.weixin.domain.InMessage;
import org.xwjava.weixin.domain.image.ImageInMessage;
import org.xwjava.weixin.domain.link.LinkInMessage;
import org.xwjava.weixin.domain.location.LocationInMessage;
import org.xwjava.weixin.domain.shortvideo.ShortVideoInMessage;
import org.xwjava.weixin.domain.test.TextInMessage;
import org.xwjava.weixin.domain.video.VideoInMessage;
import org.xwjava.weixin.domain.voice.VoiceInMessage;

public class MessageTypeMapper {

	private static Map<String, Class<? extends InMessage>> typeMap=new ConcurrentHashMap<>();
	//通过Map记录消息类型和类的关系
	static {
		typeMap.put("text", TextInMessage.class);
		typeMap.put("image", ImageInMessage.class);
		typeMap.put("link", LinkInMessage.class);
		typeMap.put("location", LocationInMessage.class);
		typeMap.put("shortvideo", ShortVideoInMessage.class);
		typeMap.put("video", VideoInMessage.class);
		typeMap.put("voice", VoiceInMessage.class);
	}
	
	//通过消息类型获取对应的类
	@SuppressWarnings("unchecked")
	public static <T extends InMessage> Class<T>getClass(String type){
		return (Class<T>) typeMap.get(type);
	}
}
