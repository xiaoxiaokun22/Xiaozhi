package com.hgk.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import org.springframework.stereotype.Component;

/**
 * Tools使用
 * Function Calling 函数调用 也叫 Tools 工具
 * @ToolMemoryId注解，如果你有多个用户，或每个用户有多个聊天记忆，并且希望在 @Tool 方法中对它们进行区分，那么这个功能会很有用
 */
@Component
public class CalculatorTools {
//    @Tool
//    double sum(double a, double b) {
//        System.out.println("调用加法运算");
//        return a + b;
//    }
//    @Tool
//    double squareRoot(double x) {
//        System.out.println("调用平方根运算");
//        return Math.sqrt(x);
//    }

    @Tool(name = "加法", value = "返回两个参数相加之和")
    double sum(@ToolMemoryId int memoryId,
            @P(value="加数1", required = true) double a,
            @P(value="加数2", required = true) double b) {
        System.out.println("调用加法运算 " + memoryId);
        return a + b;
    }
    @Tool(name = "平方根", value = "返回给定参数的平方根")
    double squareRoot(@ToolMemoryId int memoryId, double x) {
        System.out.println("调用平方根运算 " + memoryId);
        return Math.sqrt(x);
    }
}
