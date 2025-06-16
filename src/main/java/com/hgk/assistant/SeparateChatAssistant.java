package com.hgk.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

/**
 * 创建记忆隔离，对话智能体
 * chatMemoryProvider
 * @SystemMessage 系统消息提示词,如果修改了。会失去前面的记忆;整个记忆只有一个系统消息
 */
@AiService(
        wiringMode = EXPLICIT,
        chatModel = "qwenChatModel",
        chatMemoryProvider = "chatMemoryProvider",
        tools = "calculatorTools" //配置tools,Function Calling 函数调用 也叫 Tools 工具
)
public interface SeparateChatAssistant {
    /**
     * 分离聊天记录
     * @param memoryId 聊天id
     * @param userMessage 用户消息
     * @return
     */
//    @SystemMessage("你是我的好朋友，请用东北话回答问题。今天是{{current_date}}")//系统消息提示词
    @SystemMessage(fromResource = "my-prompt-template.txt")
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);

    @UserMessage("你是我的好朋友，请用粤语回答问题。{{message}}")
    String chat2(@MemoryId int memoryId, @V("message") String userMessage);

    @SystemMessage(fromResource = "my-prompt-template3.txt")
    String chat3(
            @MemoryId int memoryId,
            @UserMessage String userMessage,
            @V("username") String username,
            @V("age") int age
    );
}

