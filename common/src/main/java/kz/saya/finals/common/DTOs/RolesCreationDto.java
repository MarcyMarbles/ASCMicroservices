package kz.saya.finals.common.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RolesCreationDto {
    private String code; // ADMIN or ROLE_ADMIN
    private String description;
}
