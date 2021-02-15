package com.example.chatapp;

import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

public class msg_class {
    public String msgId, userId, msg, userGender, userName, empty;
    public msg_class() {
    }
    public msg_class(String empty) {
        this.empty = empty;
    }
    public msg_class(String msgId, String userName, String userId, String msg, String userGender) {
        this.msgId = msgId;
        this.userId = userId;
        this.msg = msg;
        this.userGender = userGender;
        this.userName = userName;
    }
    public msg_class(String msgId, String userName, String userId, String msg, String userGender, String empty) {
        this.msgId = msgId;
        this.userId = userId;
        this.msg = msg;
        this.userGender = userGender;
        this.userName = userName;
        this.empty = empty;
    }
    @Exclude
    Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("msgId", msgId);
        result.put("userId", userId);
        result.put("msg", msg);
        result.put("userGender", userGender);
        result.put("userName", userName);
        return result;
    }
}
