package cn.bugstack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 拦截所有路径
                .allowedOrigins("*")
                // .allowedOrigins("http://localhost:3000", "https://your-app.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                // .allowCredentials(true) // 是否允许携带 cookie，⚠️ 注意：这里不能同时设为 true！
                .maxAge(10000); // 预检请求缓存时间（秒）
    }
}