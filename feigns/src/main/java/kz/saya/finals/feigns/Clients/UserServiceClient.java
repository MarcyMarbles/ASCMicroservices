package kz.saya.finals.feigns.Clients;

import kz.saya.finals.common.DTOs.RegisterRequestDTO;
import kz.saya.finals.common.DTOs.UserDTO;
import kz.saya.sbasesecurity.Feigns.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", path = "/api/user", configuration = FeignAuthConfig.class)
public interface UserServiceClient {
    @GetMapping("/public/exists/{login}")
    Boolean userExists(@PathVariable("login") String login);

    @PostMapping("/public/create")
    Boolean createUser(
            @RequestBody RegisterRequestDTO registerRequestDTO
    );

    @GetMapping("/public/get-by-login/{login}")
    UserDTO getByLogin(@PathVariable("login") String login);

}
