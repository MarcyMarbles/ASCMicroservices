package kz.saya.finals.common.DTOs.Teams;

import lombok.Data;

@Data
public class TeamUpdateDto{
    private String name;
    private String tag;
    private String description;
    private String country;
    private byte[] logo; // check on front if this is image
}
