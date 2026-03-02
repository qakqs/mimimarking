package cn.bugstack;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Configurable

public class Application {

    public static void main(String[] args){
        ConfigurableApplicationContext run = SpringApplication.run(Application.class);
        System.out.println();
    }

}
