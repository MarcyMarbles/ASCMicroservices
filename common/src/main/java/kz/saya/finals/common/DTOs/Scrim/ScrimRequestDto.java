package kz.saya.finals.common.DTOs.Scrim;

import kz.saya.finals.common.Enums.ScrimType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ScrimRequestDto {
    private String name; // Название скрима
    private UUID gameId; // ID игры
    private ScrimType scrimType; // Тип скрима (Solo, Duo, Squad)
    private boolean ranked;
}
