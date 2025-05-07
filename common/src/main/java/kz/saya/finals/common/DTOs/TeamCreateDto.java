package kz.saya.finals.common.DTOs;

import lombok.Data;

import java.util.UUID;

@Data
public class TeamCreateDto {
    private String name;
    private String tag;
    private String description;
    private String country;
    private UUID creatorId;
}

