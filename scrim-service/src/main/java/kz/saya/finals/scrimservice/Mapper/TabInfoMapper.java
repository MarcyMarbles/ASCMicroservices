package kz.saya.finals.scrimservice.Mapper;

import kz.saya.finals.common.DTOs.Scrim.TabInfoDto;
import kz.saya.finals.scrimservice.Entities.TabInfo;

import java.util.List;

public class TabInfoMapper {
    public static TabInfoDto toDto(TabInfo tabInfo) {
        return new TabInfoDto()
                .setId(tabInfo.getId())
                .setScrimId(tabInfo.getScrim().getId())
                .setPlayerId(tabInfo.getPlayerId())
                .setKills(tabInfo.getKills())
                .setAssists(tabInfo.getAssists())
                .setDeaths(tabInfo.getDeaths())
                .setDamage(tabInfo.getDamage())
                .setHeal(tabInfo.getHeal())
                .setNickname(tabInfo.getNickname())
                .setRevives(tabInfo.getRevives())
                .setScore(tabInfo.getScore())
                .setPosition(tabInfo.getPosition());
    }

    public static List<TabInfoDto> toListDto(List<TabInfo> teams) {
        return teams.stream().map(TabInfoMapper::toDto).toList();
    }
}
