package kz.saya.finals.common.DTOs.Scrim;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ScrimToRankDTO {
    ScrimDto scrimDto;
    ScrimResultsDto scrimResultsDto;
    List<TabInfoDto> tabInfoDtos;
}
