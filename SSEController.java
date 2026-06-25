package com.ai.doctor.controller;

import com.ai.doctor.enums.SSEMsgType;
import com.ai.doctor.utils.SSEServer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("sse")
@Tag(name = "SSE接口", description = "SSE接口") // 类级注解
public class SSEController {

    /**
     * @param userId
     * @return SseEmitter
     * @Description: 连接sse服务的接口
     */
    @Operation(summary = "连接sse服务的接口", description = "连接sse服务的接口") // 方法级注解
    @GetMapping(path = "connect", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter connect(@RequestParam("userId") String userId) {
        return SSEServer.connect(userId);
    }

    //发送单条消息
    @Operation(summary = "发送单条消息", description = "发送单条消息") // 方法级注解
    @GetMapping("/sendMessage")
    public Object sendMessage(@RequestParam("userId") String userId, @RequestParam String message){
        SSEServer.sendMessage(userId, message, SSEMsgType.MESSAGE);
        return "OK";
    }

    /**
     * @Description: 发送消息给所有客户端用户
     * @param message
     * @return Object
     */
    @Operation(summary = "发送消息给所有客户端用户", description = "发送消息给所有客户端用户") // 方法级注解
    @GetMapping("sendMessageAll")
    public Object sendMessageAll(@RequestParam("message") String message) {
        SSEServer.sendMessageToAllUsers(message);
        return "OK";
    }

    /**
     * 消息追加，用于流式stream推送
     * @param userId
     * @param message
     * @return
     * @throws Exception
     */
    @Operation(summary = "消息追加，用于流式stream推送", description = "消息追加，用于流式stream推送") // 方法级注解
    @GetMapping("sendMessageAdd")
    public Object sendMessageAdd(@RequestParam("userId") String userId,
                                 @RequestParam("message") String message) throws Exception {
        for (int i = 0 ; i < 10 ; i ++) {
            Thread.sleep(200);
            SSEServer.sendMessage(userId, message + "-" + i, SSEMsgType.ADD);
        }
        return "OK";
    }

    /**
     * @Description: 停止sse
     * @param userId
     * @return Object
     */
    @Operation(summary = "停止sse服务器", description = "消息追加，用于流式stream推送") // 方法级注解
    @GetMapping("stop")
    public Object stopServer(@RequestParam("userId") String userId)  {
        SSEServer.stopServer(userId);
        return "OK";
    }

    /**
     * 获取服务器在线人数
     * @return
     */
    @Operation(summary = "获取服务器在线人数", description = "获取服务器在线人数") // 方法级注解
    @GetMapping("getOnlineCount")
    public String getOnlineCount(){
        int onlineCounts = SSEServer.getOnlineCounts();
        return "在线人数:"+onlineCounts;
    }
}