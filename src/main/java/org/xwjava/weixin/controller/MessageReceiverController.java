package org.xwjava.weixin.controller;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;

import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xwjava.weixin.domain.InMessage;
import org.xwjava.weixin.service.MessageTypeMapper;

//控制器: 负责接收用户的请求参数、调用业务层逻辑代码、返回视图/结果给客服端（浏览器）
//Controller  基于JSP的控制器
//RestController  符合RestFul风格的WEB服务的控制器
//Restful不同的请求方法调用不同的处理处理程序，只包含数据，不包含视图 (HTML,JSP)
@RestController
@RequestMapping("/xiaowei_1/weixin/receiver")
public class MessageReceiverController {

	//日志记录器
	private static final Logger LOG = LoggerFactory.getLogger(MessageReceiverController.class);
	
	@Autowired
	private RedisTemplate<String, InMessage> inMessageTemplate;
	
	@GetMapping //只处理GET请求
	public String echo( 
			@RequestParam("singnature")String singnature,// 
			@RequestParam("timestamp")String timestamp,//
			@RequestParam("nonce")String nonce,//
			@RequestParam("echostr")String echostr
			)  
    { 
		//正常来讲，需要把timpstamp和nonce放入一个数据，并进行排序
		//接着把排序后的两个元素拼接成一个新的String
		//使用SHA-1算法对新的String进行加密
		//最后把加密的结果跟singnature进行比较，如果相同返回验证通过
		
		//原路返回echostr的值，返回以后微信公众号平台就能够认为：服务器对接成功
		return echostr;
	}
	
	@PostMapping
	//@RequestBody注解表示把请求内容获取出来，并且转换为String传入给xml参数。
	public String onMessage(
			@RequestParam("signature")String signature,// 
			@RequestParam("timestamp")String timestamp,//
			@RequestParam("nonce")String nonce,//
			@RequestBody String xml) {
		//收到消息
		//{}占位符,第一个{}会把第二个参数的值自动填入
		//LOG.trace必须要求日志记录器的配置为trace级别才能输出
		LOG.trace("收到的消息原文: \n{}\n--------------------", xml);
		
		//截取消息类型
		//<MsgType><![CDATA[text]]></MsgType>
		String type= xml.substring(xml.indexOf("<MsgType><![CDATA[") +18 );
		type = type.substring(0,type.indexOf("]]></MsgType>"));
		
		Class<InMessage> cla=MessageTypeMapper.getClass(type);
		
		//使用JAXB完成XML转换为Java对象的操作
		InMessage inMessage= JAXB.unmarshal(new StringReader(xml), cla);
		
		LOG.debug("转换得到的消息对象：\n{}\n", inMessage.toString());
		
		//把消息放入消息队列
		inMessageTemplate.execute(new RedisCallback<>() {

			//connection对象表示跟Redis数据库的连接
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				
				try {
				//发布消息的时候，需要准备两个Byte[]
				//一个作为通道名称来使用，类似于无线电广播，不同的频道声音是隔离的。通道名称是Redis用来隔离不同数据的
				//比如文本消息、图片消息处理方式不同，所以使用前端来隔离：text*表示文本消息、image*表示图片消息
				//建议在多人共享一个服务器的时候，每个人使用不同的数据库实例即可，并且建议在通道名称之前加上反向代理的前缀。
				
				String channel = "xiaowei_1" + inMessage.getMsgType();
				
				//消息内容要自己序列化才能放入队列中
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(out);
				oos.writeObject(inMessage);
				
			 Long l= connection.publish(channel.getBytes(), out.toByteArray());
			 System.out.println("发布结果："+ l);
				}catch(Exception e) {
					LOG.error("把消息队列放入队列是出现问题：",e.getLocalizedMessage(), e);
				}
				
				return null;
			}
		});
		
	

		//转换消息
		//把消息丢入队列
		//消费队列中的消息
		//产生客服消息
		
		return "success";
		
	}
}
