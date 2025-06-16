package com.hgk.assistant;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

/**
 * 聊天记忆智能体
 * chatMemory
 * @UserMessage 用户提示词,每次提问都会携带
 */
@AiService(
        wiringMode = EXPLICIT,
        chatModel = "qwenChatModel",
        chatMemory = "chatMemory"
)
public interface MemoryChatAssistant {
    @UserMessage("你是我的好朋友，请用上海话回答问题，并且添加一些表情符号。 {{msg}}")
    String chat(@V("msg") String message);
}
