package com.hgk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgk.entity.Appointment;
import org.springframework.web.multipart.MultipartFile;

public interface AppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);

    String uploadFile(MultipartFile file);

    void saveEmbedding(String uploadFileUrl);
}

