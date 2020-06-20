package com.mobilecourse.backend;

import com.mobilecourse.backend.controllers.MessageController;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Hashtable;


@Controller
@ServerEndpoint("/websocket/{uid}")
public class WebSocketServer {

    public static Hashtable<Integer, WebSocketServer> getWebSocketTable() {
        return webSocketTable;
    }

    private static Hashtable<Integer, WebSocketServer> webSocketTable = new Hashtable<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //用于标识客户端的uid
    private int uid = -1;


    private static MessageController messageController;

    @Autowired
    public void setMessageController(MessageController messageController) {
        WebSocketServer.messageController = messageController;
    }


    //推荐在连接的时候进行检查，防止有人冒名连接
    @OnOpen
    public void onOpen(Session session, @PathParam("uid")int uid) {
        this.session = session;
        this.uid = uid;
        webSocketTable.put(uid, this);
        System.out.println(uid + "成功连接websocket");
        String messages = messageController.getMessages(uid);
        this.sendMessage(messages);
        messageController.deleteMessage(uid);
    }

    // 在关闭连接时移除对应连接
    @OnClose
    public void onClose() {
        webSocketTable.remove(this.uid);
    }

    // 收到消息时候的处理
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jsonTo = JSONObject.parseObject(message);
        String mes = (String) jsonTo.get("content");
        sendMessageTo(mes, (Integer) jsonTo.get("to"));

        System.out.println(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        webSocketTable.remove(this.uid);
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }


    public void sendMessageTo(String message, int to) {
        WebSocketServer toS = webSocketTable.get(to);
        JSONObject msg = new JSONObject();
        msg.put("sender_id", uid);
        msg.put("to", to);
        msg.put("send_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                Timestamp.valueOf(LocalDateTime.now())));

        msg.put("content", message);
        String s = msg.toJSONString();
        if(toS == null){
            messageController.addMessage(message, to, uid);
        }
        else{
            toS.sendMessage(s);
        }

        sendMessage(s);
        System.out.println(s);
    }
}
