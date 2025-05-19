package kz.saya.finals.rankingservice.Service;

import kz.saya.finals.rankingservice.EventListeners.SkillEntityListener;
import kz.saya.finals.rankingservice.Repository.RankRepository;
import org.springframework.stereotype.Component;

@Component
public class SkillEntityListenerInjector {

    public SkillEntityListenerInjector(RankRepository rankRepository) {
        SkillEntityListener.setRankRepository(rankRepository);
    }
}
