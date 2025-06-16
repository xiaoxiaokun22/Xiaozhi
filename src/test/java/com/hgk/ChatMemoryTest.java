package com.hgk;

import com.hgk.assistant.Assistant;
import com.hgk.assistant.MemoryChatAssistant;
import com.hgk.assistant.SeparateChatAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class ChatMemoryTest {
    /**
     * 没有记忆测试
     */
    @Autowired
    private Assistant assistant;
    @Test
    public void testChatMemory() {
        String answer1 = assistant.chat("我是环环");
        System.out.println(answer1);
        String answer2 = assistant.chat("我是谁");
        System.out.println(answer2);
    }

    /**
     * 聊天记忆的简单实现
     */
    @Autowired
    private QwenChatModel qwenChatModel;
    @Test
    public void testChatMemory2() {
            //第一轮对话
        UserMessage userMessage1 = UserMessage.userMessage("我是环环");
        ChatResponse chatResponse1 = qwenChatModel.chat(userMessage1);
        AiMessage aiMessage1 = chatResponse1.aiMessage();
        //输出大语言模型的回复
        System.out.println(aiMessage1.text());
        //第二轮对话 需要第一轮的请求和响应
        UserMessage userMessage2 = UserMessage.userMessage("你知道我是谁吗");
        ChatResponse chatResponse2 = qwenChatModel.chat(Arrays.asList(userMessage1,
                aiMessage1, userMessage2));
        AiMessage aiMessage2 = chatResponse2.aiMessage();
        //输出大语言模型的回复
        System.out.println(aiMessage2.text());
    }

    /**
     * 使用ChatMemory实现聊天记忆
     * 聊天记忆的实现都是基于内存(ChatMemoryStore接口实现类: 1.默认SingleSlotChatMemoryStore 2.InMemoryChatMemoryStore)
     */
    @Test
    public void testChatMemory3() {
        //创建chatMemory
        //withMaxMessages(10) 既包括UserMessage ,又包括AiMessage
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        //创建AIService
        Assistant assistant = AiServices
                .builder(Assistant.class)
                .chatLanguageModel(qwenChatModel)
                .chatMemory(chatMemory)
                .build();
        //调用service的接口
        String answer1 = assistant.chat("我是环环");
        System.out.println(answer1);
        String answer2 = assistant.chat("我是谁");
        System.out.println(answer2);
    }

    /**
     * 使用AIService实现 聊天记忆
     * MemoryChatAssistant(chatMemory) + MemoryChatAssistantConfig + MemoryChatAssistant
     */
    @Autowired
    private MemoryChatAssistant memoryChatAssistant;
    @Test
    public void testChatMemory4() {
        String answer1 = memoryChatAssistant.chat("我是环环");
        System.out.println(answer1);
        String answer2 = memoryChatAssistant.chat("我是谁");
        System.out.println(answer2);
    }

    /**
     * 1>使用AIService实现 用户隔离聊天记忆 基于内存
     * SeparateChatAssistant(chatMemoryProvider) + SeparateChatAssistantConfig + SeparateChatAssistantConfig
     * 2>使用AIService实现 用户隔离聊天记忆 基于数据库持久化
     * mongodb
     *
     * [{
     * 	"contents": [{
     * 		"text": "我是环环",
     * 		"type": "TEXT"
     * 	    }],
     * 	"type": "USER"
     * }, {
     * 	"text": "你好，环环！很高兴见到你。有什么我可以帮助你的吗？如果你有任何问题或需要讨论什么话题，随时告诉我哦。",
     * 	"type": "AI"
     * }, {
     * 	"contents": [{
     * 		"text": "我是谁",
     * 		"type": "TEXT"
     *    }],
     * 	"type": "USER"
     * }, {
     * 	"text": "你刚刚告诉我你是环环。如果你是在问更具体的信息或者有其他身份，可以再告诉我一些细节，我会尽力帮助你！",
     * 	"type": "AI"
     * }]
     *
     */
    @Autowired
    private SeparateChatAssistant separateChatAssistant;
    @Test
    public void testChatMemory5() {
        String answer1 = separateChatAssistant.chat(1,"我是环环");
        System.out.println(answer1);
        String answer2 = separateChatAssistant.chat(1,"我是谁");
        System.out.println(answer2);
        //另一个用户提问
        String answer3 = separateChatAssistant.chat(2,"我是谁");
        System.out.println(answer3);
    }

}

