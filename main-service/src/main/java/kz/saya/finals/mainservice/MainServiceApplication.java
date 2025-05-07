package kz.saya.finals.mainservice;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(value = {
        "kz.saya.sbasecore",
        "kz.saya.sbasesecurity",
        "kz.saya.finals.mainservice",
        "kz.saya.finals.common",
})
@EntityScan(value = {
        "kz.saya.sbasecore.Entity",
        "kz.saya.finals.mainservice.Entities",
        "kz.saya.finals.common.Entities",
})
@EnableFeignClients(value = {
        "kz.saya.finals.feigns",
})
public class MainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }

}
