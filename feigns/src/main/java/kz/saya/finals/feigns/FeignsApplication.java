package kz.saya.finals.feigns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FeignsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignsApplication.class, args);
    }

}
