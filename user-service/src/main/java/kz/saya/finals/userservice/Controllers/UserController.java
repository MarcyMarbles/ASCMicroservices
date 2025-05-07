package kz.saya.finals.userservice.Controllers;

import kz.saya.finals.common.DTOs.RegisterRequestDTO;
import kz.saya.finals.common.DTOs.UserDTO;
import kz.saya.finals.common.Mappers.UserMapper;
import kz.saya.sbasecore.Entity.User;
import kz.saya.sbasecore.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/public/exists/{login}")
    public Boolean userExists(@PathVariable String login) {
        return userService.isUserAlreadyCreated(login);
    }

    @PostMapping("/public/create")
    Boolean createUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        User user = new User();
        user.setLogin(registerRequestDTO.getLogin());
        user.setPassword(registerRequestDTO.getPassword());
        try {
            userService.createUser(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @GetMapping("/public/get-by-login/{login}")
    public UserDTO getByLogin(@PathVariable String login) {
        User user = userService.getUserByLogin(login);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return UserMapper.toDTO(user);
    }


}
