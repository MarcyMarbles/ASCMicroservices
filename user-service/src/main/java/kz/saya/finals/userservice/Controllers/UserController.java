package kz.saya.finals.userservice.Controllers;

import kz.saya.finals.common.DTOs.RegisterRequestDTO;
import kz.saya.finals.common.DTOs.RolesCreationDto;
import kz.saya.finals.common.DTOs.UserDTO;
import kz.saya.finals.common.Mappers.UserMapper;
import kz.saya.sbasecore.Entity.Roles;
import kz.saya.sbasecore.Entity.User;
import kz.saya.sbasecore.Repositories.RoleRepository;
import kz.saya.sbasecore.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.management.relation.Role;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

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

    @PostMapping("/roles/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Roles> addRole(@RequestBody RolesCreationDto role) {
        Roles roles = roleRepository.findByName(getRoleCode(role)).orElse(null);
        if (roles != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(roles);
        }
        roles = new Roles();
        roles.setName(getRoleCode(role));
        roles.setDescription(role.getDescription());
        return ResponseEntity.ok(roles);
    }

    private String getRoleCode(RolesCreationDto rolesCreationDto) {
        if (rolesCreationDto.getCode().contains("ROLE_")) {
            return rolesCreationDto.getCode();
        }
        return "ROLE_" + rolesCreationDto.getCode();
    }


}
