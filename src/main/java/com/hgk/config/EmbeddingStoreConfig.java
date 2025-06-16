package com.hgk.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeEmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeServerlessIndexConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Pinecone向量数据库配置
 * 代码级别，设置向量数据库的apiKey,index,namespace等
 */
@Configuration
public class EmbeddingStoreConfig {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Value("${gk.pinecone.api-key}")
    String apiKey;

    @Value("${gk.pinecone.xiaozhi-index}")
    String xiaozhiIndex;

    @Value("${gk.pinecone.xiaozhi-namespace}")
    String xiaozhiNamespace;

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        //创建向量存储
        EmbeddingStore<TextSegment> embeddingStore = PineconeEmbeddingStore.builder()
                .apiKey(apiKey)
                .index(xiaozhiIndex)//如果指定的索引不存在，将创建一个新的索引
                .nameSpace(xiaozhiNamespace) //如果指定的名称空间不存在，将创建一个新的名称空间
                .createIndex(PineconeServerlessIndexConfig.builder()
                                .cloud("AWS") //指定索引部署在 AWS 云服务上。
                                .region("us-east-1") //指定索引所在的 AWS 区域为 us-east-1。
                                .dimension(embeddingModel.dimension()) //指定索引的向量维度，该维度与 embeddedModel 生成的向量维度相同。
                        .build())
                .build();
        return embeddingStore;
    }
    }
