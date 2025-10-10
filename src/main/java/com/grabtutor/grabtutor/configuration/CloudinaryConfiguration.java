package com.grabtutor.grabtutor.configuration;

import com.cloudinary.Cloudinary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CloudinaryConfiguration {
    @Value("${cloudinary.cloud_name}")
    String CLOUD_NAME;
    @Value("${cloudinary.api_key}")
    String API_KEY;
    @Value("${cloudinary.api_secret}")
    String API_SECRET;
    @Bean
    public Cloudinary configKey(){
//        Map<String, String> config = new HashMap<>();
//        config.put("cloud_name", CLOUD_NAME);
//        config.put("api_key", API_KEY);
//        config.put("api_secret", API_SECRET);
//        return new Cloudinary(config);
        return new Cloudinary(com.cloudinary.utils.ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET,
                "secure", true
        ));
    }
}
