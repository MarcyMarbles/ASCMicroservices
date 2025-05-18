package kz.saya.finals.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "kz.saya.finals.feigns")
@ComponentScan(
        basePackages = {
                "kz.saya.sbasecore",
                "kz.saya.sbasesecurity",
                "kz.saya.finals.notificationservice",
        }
)
@EntityScan(
        basePackages = {
                "kz.saya.finals.notificationservice.Entities",
                "kz.saya.sbasecore.Entity"
        }
)
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

}
