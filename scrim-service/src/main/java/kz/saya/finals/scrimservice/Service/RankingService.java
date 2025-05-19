package kz.saya.finals.scrimservice.Service;

import kz.saya.finals.feigns.Clients.RankingServiceClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RankingService {
    private final RankingServiceClient rankingServiceClient;

    public RankingService(RankingServiceClient rankingServiceClient) {
        this.rankingServiceClient = rankingServiceClient;
    }

    private void processResults(UUID scrimId) {
        rankingServiceClient.proceedResults(scrimId);
    }
}
