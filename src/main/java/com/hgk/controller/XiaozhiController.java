package com.hgk.controller;

import com.hgk.bean.ChatForm;
import com.hgk.common.HttpResult;
import com.hgk.service.AppointmentService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hgk.assistant.XiaozhiAgent;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Tag(name = "硅谷小智")
@RestController
@RequestMapping("/xiaozhi")
public class XiaozhiController {

    private static final Long IMAGE_MAX_SIZE = 20 * 1048576L;

    @Autowired
    private XiaozhiAgent xiaozhiAgent;

    @Autowired
    private AppointmentService appointmentService;

    @Operation(summary = "对话")
    @PostMapping(value = "/chat", produces = "text/stream;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatForm chatForm) {
        return xiaozhiAgent.chat(chatForm.getMemoryId(), chatForm.getMessage());
    }


    @Operation(summary = "上传文件")
    @PostMapping(value = "upload")
    public HttpResult<String> uploadFile(@RequestParam("file") @ApiParam(value = "文件") MultipartFile file) {
        if (file.isEmpty()) {
            return HttpResult.returnFail("文件不能为空");
        }
        if (file.getSize() > IMAGE_MAX_SIZE) {
            return HttpResult.returnFail("文件不能超过20M");
        }
//        if (FILE_CONTENT_TYPE.equals(file.getContentType())) {
//            return HttpResult.returnFail("仅支持文件类型: pdf");
//        }
        if (StringUtils.isEmpty(file.getOriginalFilename())) {
            return HttpResult.returnFail("文件名不能为空");
        }
        try {
            //上传到对象存储Minio
            String uploadFileUrl = appointmentService.uploadFile(file);
            if (StringUtils.isEmpty(uploadFileUrl)) {
                HttpResult.returnFail("文件上传异常!");
            }
            //文档存入向量数据库
            appointmentService.saveEmbedding(uploadFileUrl);
            return HttpResult.returnSuccess(uploadFileUrl);
        } catch (Exception e) {
            log.error("文件上传错误", e);
            return HttpResult.returnFail("文件上传异常!");
        }
    }
}

