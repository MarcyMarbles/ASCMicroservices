package kz.saya.finals.scrimservice.Repositories;

import kz.saya.finals.scrimservice.Entities.TabInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TabInfoRepository extends JpaRepository<TabInfo, UUID> {
    List<TabInfo> findByScrimId(UUID scrimId);
}