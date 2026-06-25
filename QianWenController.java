package com.ai.doctor.controller;

import com.ai.doctor.utils.Results;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.output.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接入千问AI平台
 */
@RestController
@Slf4j
@Tag(name = "千问", description = "接入千问AI平台") // 类级注解
public class QianWenController {

    /**
     * 通义千问大模型
     */
    @Autowired
    private QwenChatModel qwenChatModel;

    @Operation(summary = "千问问答", description = "千问问答") // 方法级注解
    @GetMapping("/qianwen")
    public Results testDashScopeQwen(@RequestParam("question") String question) {
        log.info("前端请求问题:"+question);
        //向模型提问
        String answer = qwenChatModel.chat(question);
        //输出结果
        log.info(answer);
        return Results.success(answer);
    }

    @Operation(summary = "千问生成图片", description = "千问生成图片") // 方法级注解
    @GetMapping("/createimg")
    public Results testDashScopeWanx(@RequestParam("question") String question){
        log.info("前端请求问题:"+question);
        WanxImageModel wanxImageModel = WanxImageModel.builder()
                .modelName("wanx2.1-t2i-plus")
                .apiKey("sk-a060871d7ef34469a01b54e7bea1422c")
                .build();
        Response<Image> response = wanxImageModel.generate(question);
        String answer=response.content().url().toString();
        log.info(answer);
        return Results.success(answer);
    }
}
