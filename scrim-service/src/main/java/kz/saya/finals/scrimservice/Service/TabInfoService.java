package kz.saya.finals.scrimservice.Service;

import kz.saya.finals.scrimservice.Entities.ScrimResults;
import kz.saya.finals.scrimservice.Entities.TabInfo;
import kz.saya.finals.scrimservice.Repositories.ScrimResultsRepository;
import kz.saya.finals.scrimservice.Repositories.TabInfoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class TabInfoService {

    private final TabInfoRepository tabInfoRepository;

    @Autowired
    public TabInfoService(TabInfoRepository tabInfoRepository) {
        this.tabInfoRepository = tabInfoRepository;
    }

    public TabInfo createTabInfo(TabInfo tabInfo) {
        return tabInfoRepository.save(tabInfo);
    }

    public List<TabInfo> getByScrim(UUID scrimId) {
        return tabInfoRepository.findByScrimId(scrimId);
    }

    public TabInfo updateTabInfo(UUID id, TabInfo tabInfo) {
        TabInfo existing = tabInfoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "TabInfo not found"));
        BeanUtils.copyProperties(tabInfo, existing, "id");
        return tabInfoRepository.save(existing);
    }

    public void deleteTabInfo(UUID id) {
        tabInfoRepository.deleteById(id);
    }
}
