package com.hgk.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        wiringMode = EXPLICIT,
//        chatModel = "qwenChatModel",
        streamingChatModel = "qwenStreamingChatModel", //配置流式输出,使用阿里通义千问
        chatMemoryProvider = "chatMemoryProviderXiaozhi", //配置隔离持久化记忆
        tools = "appointmentTools", //配置tools,Function Calling 函数调用 也叫 Tools 工具
//        contentRetriever = "contentRetrieverXiaozhi" //配置内置的向量存储
        contentRetriever = "contentRetrieverXiaozhiPincone" //配置pinecone向量存储
)
public interface XiaozhiAgent {
    //非流式输出
//    @SystemMessage(fromResource = "zhaozhi-prompt-template.txt")
//    String chat(@MemoryId Long memoryId, @UserMessage String userMessage);

    //流式输出
    @SystemMessage(fromResource = "zhaozhi-prompt-template.txt")
    Flux<String> chat(@MemoryId Long memoryId, @UserMessage String userMessage);
}
