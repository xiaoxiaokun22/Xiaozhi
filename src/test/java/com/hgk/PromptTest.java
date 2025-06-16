package com.hgk;

import com.hgk.assistant.MemoryChatAssistant;
import com.hgk.assistant.SeparateChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PromptTest {
    @Autowired
    private SeparateChatAssistant separateChatAssistant;
    @Test
    public void testSystemMessage() {
        String answer = separateChatAssistant.chat(5,"今天几号");
        System.out.println(answer);
    }

    /**
     * @V注解
     */
    @Autowired
    private MemoryChatAssistant memoryChatAssistant;
    @Test
    public void testUserMessage() {
        String answer1 = memoryChatAssistant.chat("我是环环");
        System.out.println(answer1);

        String answer2 = memoryChatAssistant.chat("我18了");
        System.out.println(answer2);

        String answer3 = memoryChatAssistant.chat("你知道我是谁吗");
        System.out.println(answer3);
    }

    /**
     * 如果有两个或两个以上的参数，我们必须要用 @V
     */
    @Test
    public void testV() {
        String answer1 = separateChatAssistant.chat2(9, "我是环环");
        System.out.println(answer1);
        String answer2 = separateChatAssistant.chat2(9, "我是谁");
        System.out.println(answer2);
    }

    /**
     * 将 @SystemMessage 和 @V 结合使用
     */
    @Test
    public void testUserInfo() {
        String answer = separateChatAssistant.chat3(10, "我是谁，我多大了", "翠花", 18);
        System.out.println(answer);
    }

}
