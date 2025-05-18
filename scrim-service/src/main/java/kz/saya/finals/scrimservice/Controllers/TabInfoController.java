package kz.saya.finals.scrimservice.Controllers;

import kz.saya.finals.common.DTOs.Scrim.TabInfoDto;
import kz.saya.finals.scrimservice.Entities.TabInfo;
import kz.saya.finals.scrimservice.Mapper.TabInfoMapper;
import kz.saya.finals.scrimservice.Service.TabInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scrims/{scrimId}/tab-info")
public class TabInfoController {

    private final TabInfoService tabInfoService;

    @Autowired
    public TabInfoController(TabInfoService tabInfoService) {
        this.tabInfoService = tabInfoService;
    }

    @PostMapping
    public ResponseEntity<TabInfo> create(@RequestBody TabInfo tabInfo) {
        return new ResponseEntity<>(tabInfoService.createTabInfo(tabInfo), HttpStatus.CREATED);
    }

    @GetMapping
    public List<TabInfoDto> list(@PathVariable UUID scrimId) {
        return TabInfoMapper.toListDto(tabInfoService.getByScrim(scrimId));
    }

    @PutMapping("/{id}")
    public TabInfo update(@PathVariable UUID id, @RequestBody TabInfo tabInfo) {
        return tabInfoService.updateTabInfo(id, tabInfo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        tabInfoService.deleteTabInfo(id);
    }
}
