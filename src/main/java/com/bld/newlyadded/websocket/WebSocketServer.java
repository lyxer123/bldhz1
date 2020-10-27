package com.bld.newlyadded.websocket;


import com.alibaba.fastjson.JSONObject;
import com.bld.newlyadded.config.WebSocketConfig;
import com.bld.newlyadded.entity.SystemEntity;
import com.bld.newlyadded.untils.GetSystem;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
//@ConditionalOnClass(value = WebSocketConfig.class)

@ServerEndpoint(value = "/imserver/")
@Component
public class WebSocketServer {

    /** 记录当前在线连接数 */
    //private static AtomicInteger onlineCount = new AtomicInteger(0);

    @Autowired
    private GetSystem getSystem;

    /** 存放所有在线的客户端 */
    private static Map<String, Session> clients = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        //onlineCount.incrementAndGet(); // 在线数加1
        clients.put(session.getId(), session);
        // log.info("有新连接加入：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        //onlineCount.decrementAndGet(); // 在线数减1
        clients.remove(session.getId());
        // log.info("有一连接关闭：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // log.info("服务端收到客户端[{}]的消息:{}", session.getId(), message);
       // this.sendMessage(message, session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        //log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 群发消息
     *
     * @param message
     *            消息内容
     */
    public void sendMessage() {
        try {
            SystemEntity system = getSystem.getSystem();
            String s = JSONObject.toJSONString(system);
            for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
                Session toSession = sessionEntry.getValue();
                toSession.getBasicRemote().sendText(s);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
