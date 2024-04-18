package com.chat.chatapp.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.chat.chatapp.model.ChatMessage;
import com.chat.chatapp.model.ChatMessageGroup;
import com.chat.chatapp.service.ChatService;
import com.chat.chatapp.service.UserAndGroupService;

@RestController
@CrossOrigin
public class ChatController {
	
    @Autowired
    ChatService chatService;
    
    @Autowired
    UserAndGroupService userAndGroupService;

        @MessageMapping("/chat/{to}")
        @SendTo("/topic/messages/")
        public ChatMessage sendMessagePersonal(@DestinationVariable String to, ChatMessage message) {
            chatService.sendMessage(to,message);
            return message;
        }

        @GetMapping("/listmessage/{from}/{to}")
        public List<Map<String,Object>> getListMessageChat(@PathVariable("from") Integer from, @PathVariable("to") Integer to){
            return chatService.getListMessage(from, to);
        }

        @MessageMapping("/chat/group/{to}")
        @SendTo("/topic/messages/group/")
        public ChatMessageGroup sendMessageToGroup(@DestinationVariable Integer to, ChatMessageGroup message) {
            chatService.sendMessageGroup(to,message);
            return message;
        }

        @GetMapping("/listmessage/group/{groupid}")
        public List<Map<String,Object>> getListMessageGroupChat(@PathVariable("groupid") Integer groupid){
            return chatService.getListMessageGroups(groupid);
        }
        
        @GetMapping("/fetchAllUsers/{myId}")
        public List<Map<String,Object>> fetchAll(@PathVariable("myId") String myId) {
            return userAndGroupService.fetchAll(myId);

        }

        @GetMapping("/fetchAllGroups/{groupid}")
        public List<Map<String,Object>> fetchAllGroups(@PathVariable("groupid") String groupId) {
           return  userAndGroupService.fetchAllGroup(groupId);
        }

}