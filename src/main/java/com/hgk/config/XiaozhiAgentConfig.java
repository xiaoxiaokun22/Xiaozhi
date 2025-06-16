package com.hgk.config;

import com.hgk.store.MongoChatMemoryStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * 小智
 * 1.配置隔离聊天记忆功能
 * 2.持久化存储器mongoChatMemoryStore
 * 3.RAG知识库存入向量数据库，向量搜索功能集成
 */
@Configuration
public class XiaozhiAgentConfig {

    /**
     * 1.隔离聊天记忆配置
     * 2.持久化记忆配置
     */
    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;
    @Bean
    ChatMemoryProvider chatMemoryProviderXiaozhi() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(mongoChatMemoryStore)
                .build();
    }

//    /**
//     * RAG知识库存入向量数据库，向量搜索功能集成
//     * 基于: 内置向量模型 + 内置向量存储InMemoryEmbeddingStore
//     * @return
//     */
//    @Bean
//    ContentRetriever contentRetrieverXiaozhi() {
//        String dir = "D:\\资料\\学习资料\\大模型\\硅谷小智（医疗版）\\资料\\knowledge\\";
//
//        //使用FileSystemDocumentLoader读取指定目录下的知识库文档
//        //并使用默认的文档解析器对文档进行解析
//        Document document1 = FileSystemDocumentLoader.loadDocument(dir + "医院信息.md");
//        Document document2 = FileSystemDocumentLoader.loadDocument(dir + "科室信息.md");
//        Document document3 = FileSystemDocumentLoader.loadDocument(dir + "神经内科.md");
//        List<Document> documents = Arrays.asList(document1, document2, document3);
//
//        //使用内存向量存储
//        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
//
//        //使用默认的文档分割器
//        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
//
//        //从嵌入存储（EmbeddingStore）里检索和查询内容相关的信息
//        return EmbeddingStoreContentRetriever.from(embeddingStore);
//    }

    @Autowired
    private EmbeddingStore embeddingStore;
    @Autowired
    private EmbeddingModel embeddingModel;
    /**
     * 向量数据库 搜索功能集成
     * 基于: 阿里百炼的向量模型 + pinecone向量存储
     * @return
     */
    @Bean
    ContentRetriever contentRetrieverXiaozhiPincone() {
        // 创建一个 EmbeddingStoreContentRetriever 对象，用于从嵌入存储中检索内容
        return EmbeddingStoreContentRetriever
                .builder()
                // 设置用于生成嵌入向量的嵌入模型
                .embeddingModel(embeddingModel)
                // 指定要使用的嵌入存储
                .embeddingStore(embeddingStore)
                // 设置最大检索结果数量，这里表示最多返回 1 条匹配结果
                .maxResults(1)
                // 设置最小得分阈值，只有得分大于等于 0.8 的结果才会被返回
                .minScore(0.8)
                // 构建最终的 EmbeddingStoreContentRetriever 实例
                .build();
    }

}
