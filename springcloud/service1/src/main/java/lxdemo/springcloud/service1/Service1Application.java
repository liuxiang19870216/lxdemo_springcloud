package lxdemo.springcloud.service1;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringCloudApplication
public class Service1Application implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(Service1Application.class, args);
    }
}
