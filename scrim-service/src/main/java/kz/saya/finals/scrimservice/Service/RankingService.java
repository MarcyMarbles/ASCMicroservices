package kz.saya.finals.scrimservice.Service;

import kz.saya.finals.common.DTOs.Scrim.ScrimToRankDTO;
import kz.saya.finals.feigns.Clients.RankingServiceClient;
import kz.saya.finals.scrimservice.Entities.Scrim;
import kz.saya.finals.scrimservice.Entities.ScrimResults;
import kz.saya.finals.scrimservice.Entities.TabInfo;
import kz.saya.finals.scrimservice.Mapper.ScrimMapper;
import kz.saya.finals.scrimservice.Mapper.ScrimResultMapper;
import kz.saya.finals.scrimservice.Mapper.TabInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RankingService {
    private final RankingServiceClient rankingServiceClient;
    private final ScrimResultsService scrimResultsService;
    private final TabInfoService tabInfoService;

    public RankingService(RankingServiceClient rankingServiceClient, ScrimResultsService scrimResultsService, TabInfoService tabInfoService) {
        this.rankingServiceClient = rankingServiceClient;
        this.scrimResultsService = scrimResultsService;
        this.tabInfoService = tabInfoService;
    }

    public void processResults(Scrim scrim, ScrimResults scrimResults, List<TabInfo> tabInfos) {
        ScrimToRankDTO scrimToRankDTO = new ScrimToRankDTO()
                .setScrimDto(ScrimMapper.toDto(scrim))
                        .setScrimResultsDto(ScrimResultMapper.toDto(scrimResults))
                                .setTabInfoDtos(TabInfoMapper.toListDto(tabInfos));
        rankingServiceClient.proceedResults(scrimToRankDTO);
    }
}
