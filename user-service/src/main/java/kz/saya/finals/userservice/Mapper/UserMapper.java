package kz.saya.finals.userservice.Mapper;

import kz.saya.finals.common.DTOs.UserDTO;
import kz.saya.sbasecore.Entity.Roles;
import kz.saya.sbasecore.Entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLogin(user.getLogin());
        userDTO.setPassword(user.getPassword());

        List<String> roles = user.getRoles().stream()
                .filter(Objects::nonNull)
                .map(Roles::getName)
                .toList();

        userDTO.setRoles(roles);
        return userDTO;
    }
}

