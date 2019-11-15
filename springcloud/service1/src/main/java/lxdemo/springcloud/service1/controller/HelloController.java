package lxdemo.springcloud.service1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(HttpServletRequest httpServletReuqest){
        return "Hello, your user id:" + httpServletReuqest.getHeader("Authorization-UserId");
    }

}
