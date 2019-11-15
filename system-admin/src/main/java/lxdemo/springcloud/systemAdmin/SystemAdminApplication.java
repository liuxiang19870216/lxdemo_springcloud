package lxdemo.springcloud.systemAdmin;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringCloudApplication
public class SystemAdminApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(SystemAdminApplication.class, args);
	}

}

