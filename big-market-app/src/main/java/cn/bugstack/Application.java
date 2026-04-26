package cn.bugstack;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Random;

@SpringBootApplication
@Configurable
@EnableScheduling
@EnableDubbo
public class Application {

    public static void main(String[] args){
        ConfigurableApplicationContext run = SpringApplication.run(Application.class);
    }

}
