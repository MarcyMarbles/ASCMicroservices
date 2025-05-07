package kz.saya.finals.scrimservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "kz.saya.finals.feigns")
@ComponentScan(basePackages = {
        "kz.saya.sbasecore",
        "kz.saya.sbasesecurity",
        "kz.saya.finals.scrimservice"
})
public class ScrimServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrimServiceApplication.class, args);
    }

}
