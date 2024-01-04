package org.choongang.admin.config.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.entities.Configs;
import org.choongang.admin.config.repositories.ConfigsRepository;
import org.springframework.stereotype.Service;

/**
 * CRUD 작업을 위한 Service 클래스
 */
@Service
@RequiredArgsConstructor
public class ConfigSaveService {

    private final ConfigsRepository repository ;

    public void save(String code, Object data) {
        Configs configs = repository.findById(code).orElseGet(Configs::new) ;    // 데이터가 있으면 반환, 없으면 새 데이터 생성

        ObjectMapper om = new ObjectMapper() ;    // JSON 데이터 <--> 자바 클래스 변환하는 기능
        om.registerModule(new JavaTimeModule()) ;    // 날짜 있는 경우 호환을 위해 필요

        try {
            String jsonString = om.writeValueAsString(data) ;    // 자바 ➡️ json
            configs.setCode(code);
            configs.setData(jsonString);
            repository.saveAndFlush(configs) ;    // DB에 JSON 형태의 데이터 추가
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
