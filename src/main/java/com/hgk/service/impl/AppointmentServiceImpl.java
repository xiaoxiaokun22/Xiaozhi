package com.hgk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgk.entity.Appointment;
import com.hgk.mapper.AppointmentMapper;
import com.hgk.service.AppointmentService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment>
        implements AppointmentService {

    @Autowired
    private EmbeddingStore embeddingStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * 查询订单是否存在
     * @param appointment
     * @return
     */
    @Override
    public Appointment getOne(Appointment appointment) {
        LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Appointment::getUsername, appointment.getUsername());
        queryWrapper.eq(Appointment::getIdCard, appointment.getIdCard());
        queryWrapper.eq(Appointment::getDepartment, appointment.getDepartment());
        queryWrapper.eq(Appointment::getDate, appointment.getDate());
        queryWrapper.eq(Appointment::getTime, appointment.getTime());
        Appointment appointmentDB = baseMapper.selectOne(queryWrapper);
        return appointmentDB;
    }

    /**
     * TODO 文件上传到Minio
     * @param file
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file) {
        return "";
    }

    /**
     * TODO Minio文档 入 向量数据库
     * @param uploadFileUrl
     */
    @Override
    public void saveEmbedding(String uploadFileUrl) {
//        //文本向量化并存入向量数据库：将每个片段进行向量化，得到一个嵌入向量
//        EmbeddingStoreIngestor
//                .builder()
//                .embeddingStore(embeddingStore)
//                .embeddingModel(embeddingModel)
//                .build()
//                .ingest(documents);
    }
}

