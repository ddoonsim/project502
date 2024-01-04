package org.choongang.admin.config.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.entities.Configs;
import org.choongang.admin.config.repositories.ConfigsRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfigInfoService {

    private final ConfigsRepository repository ;

    public <T> Optional<T> get(String code, Class<T> clazz) {
        return get(code, clazz, null) ;
    }

    public <T> Optional<T> get(String code, TypeReference<T> typeReference) {
        return get(code, null, typeReference) ;
    }

    /**
     * 조회
     */
    public <T> Optional<T> get(String code, Class<T> clazz, TypeReference<T> typeReference) {
        Configs config = repository.findById(code).orElse(null) ;

        if (config == null || !StringUtils.hasText(config.getData())) {
            // 데이터가 없을 때 메서드 즉시 종료
            return Optional.ofNullable(null) ;
        }

        ObjectMapper om = new ObjectMapper() ;    // JSON 데이터 <--> 자바 클래스 변환할 수 있는 기능
        om.registerModule(new JavaTimeModule()) ;

        String jsonString = config.getData() ;
        try {
            T data = null ;
            if (clazz == null) {    // 복합구조일 때, TypeReference로 처리
                data = om.readValue(jsonString, new TypeReference<T>() {}) ;        // JSON 데이터 --> 복합 타입으로 변환
            } else {    // Class로 처리
                data = om.readValue(jsonString, clazz);
            }
            return Optional.ofNullable(data) ;    // JSON 데이터 --> 자바 클래스 변환
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.ofNullable(null) ;
        }
    }
}
