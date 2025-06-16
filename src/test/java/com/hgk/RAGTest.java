package com.hgk;

import dev.langchain4j.community.model.dashscope.QwenTokenizer;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenizer;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.List;

@SpringBootTest
public class RAGTest {

    /**
     * 来自 langchain4j 模块的文本文档解析器（TextDocumentParser），它能够解析纯文本格式的文件
     * （例如 TXT、HTML、MD 等,不能加载pdf等）
     */
    @Test
    public void testReadDocument() {
        String dir = "D:\\资料\\学习资料\\大模型\\硅谷小智（医疗版）\\资料\\knowledge\\";
//        //使用FileSystemDocumentLoader读取指定目录下的知识库文档
//        //并使用默认的文档解析器TextDocumentParser对文档进行解析
//        Document document = FileSystemDocumentLoader.loadDocument("D:\\资料\\学习资料\\大模型\\硅谷小智（医疗版）\\资料\\knowledge\\测试.txt");
//        System.out.println(document.text());
//        // 加载单个文档 txt,md
//        Document document = FileSystemDocumentLoader.loadDocument("E:/knowledge/file.txt"
//                , new TextDocumentParser());

//        // 从一个目录中加载所有文档
//        List<Document> documents = FileSystemDocumentLoader.loadDocuments("E:/knowledge"
//                , new TextDocumentParser());

        // 从一个目录中加载所有的.txt文档
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.txt");
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(dir
                ,pathMatcher, new TextDocumentParser());
        for (Document doc: documents){
            System.out.println("===========================");
            System.out.println(doc.metadata());
            System.out.println(doc.text());
        }

//        // 从一个目录及其子目录中加载所有文档
//        List<Document> documents = FileSystemDocumentLoader.loadDocumentsRecursively(dir + "knowledge"
//                , new TextDocumentParser());
    }

//    /**
//     * 解析PDF
//     * TODO ApachePdfBoxDocumentParser加载失败
//     */
//    @Test
//    public void testParsePDF() {
//        String dir = "D:\\资料\\学习资料\\大模型\\硅谷小智（医疗版）\\资料\\knowledge\\";
//        Document document = FileSystemDocumentLoader.loadDocument(
//                dir + "医院信息.pdf",new ApachePdfBoxDocumentParser()
//        );
//        System.out.println(document.metadata());
//        System.out.println(document.text());
//    }

    /**
     * 加载文档并存入向量数据库 简单实现langchain4j-easy-rag
     * 使用的内置文档分割器
     */
    @Test
    public void testReadDocumentAndStore() {
        String dir = "D:\\资料\\学习资料\\大模型\\硅谷小智（医疗版）\\资料\\knowledge\\";
        //使用FileSystemDocumentLoader读取指定目录下的知识库文档
        //并使用默认的文档解析器对文档进行解析(TextDocumentParser)
        Document document = FileSystemDocumentLoader.loadDocument(dir + "人工智能.md");
        //为了简单起见，我们暂时使用基于内存的向量存储
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        //ingest
        //1、分割文档：默认使用递归分割器，将文档分割为多个文本片段，每个片段包含不超过 300个token，并且有 30个token的重叠部分保证连贯性
        //DocumentByParagraphSplitter(DocumentByLineSplitter(DocumentBySentenceSplitter(DocumentByWordSplitter)))
        //2、文本向量化：使用一个LangChain4j 内置的轻量化向量模型 对每个文本片段进行向量化
        //3、将原始文本和向量存储到向量数据库中(InMemoryEmbeddingStore)
        EmbeddingStoreIngestor.ingest(document, embeddingStore);
        //查看向量数据库内容
        System.out.println(embeddingStore);
    }

    /**
     * 文档分割
     * 自定义文档分割器
     */
    @Test
    public void testDocumentSplitter() {
        String dir = "D:\\资料\\学习资料\\大模型\\硅谷小智（医疗版）\\资料\\knowledge\\";
        //使用FileSystemDocumentLoader读取指定目录下的知识库文档
        //并使用默认的文档解析器对文档进行解析(TextDocumentParser)
        Document document = FileSystemDocumentLoader.loadDocument(dir + "人工智能.md");

        //为了简单起见，我们暂时使用基于内存的向量存储
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        //自定义文档分割器
        //按段落分割文档：每个片段包含不超过 300个token，并且有 30个token的重叠部分保证连贯性
        //注意：当段落长度总和小于设定的最大长度时，就不会有重叠的必要。
        DocumentByParagraphSplitter documentSplitter = new DocumentByParagraphSplitter(
                300,
                30,
                //token分词器：按token计算
                new HuggingFaceTokenizer());

        //默认的token分词器, 按字符计算
        //DocumentByParagraphSplitter documentSplitter = new DocumentByParagraphSplitter(300, 30);

        EmbeddingStoreIngestor
                .builder()
                .embeddingStore(embeddingStore)
                .documentSplitter(documentSplitter)
                .build()
                .ingest(document);
    }


    @Value("${langchain4j.community.dashscope.chat-model.api-key}")
    String apiKey;

    @Test
    public void testTokenCount() {
        String text = "这是一个示例文本，用于测试 token 长度的计算。";
        UserMessage userMessage = UserMessage.userMessage(text);

        //计算 token 长度
        QwenTokenizer tokenizer = new QwenTokenizer(apiKey,"qwen-max");

//        HuggingFaceTokenizer tokenizer = new HuggingFaceTokenizer();
        int count = tokenizer.estimateTokenCountInMessage(userMessage);
        System.out.println("token长度：" + count);
    }

}
