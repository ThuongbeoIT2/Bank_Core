package com.example.secumix.security.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class ConfigCloudinary {
    @Bean
    public Cloudinary getCloudinary(){
        Map config = new HashMap();
        config.put("cloud_name", "dqvr7kat6");
        config.put("api_key", "712758645674169");
        config.put("api_secret", "CN0CG-NGsuJIpwn2UdLCBvX72FE");
        config.put("secure", true);
       //{ CLOUDINARY_URL=cloudinary://712758645674169:CN0CG-NGsuJIpwn2UdLCBvX72FE@dqvr7kat6}
        return new Cloudinary(config);
    }
}
