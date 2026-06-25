package com.ai.doctor.controller;

import com.ai.doctor.beans.ChatRecord;
import com.ai.doctor.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    ChatRecordService chatRecordService;

    @GetMapping("/getRecordList")
    public List<ChatRecord> getRecordList(@RequestParam("userName") String userName) {
       return  chatRecordService.getChatRecordList(userName);
    }

}
