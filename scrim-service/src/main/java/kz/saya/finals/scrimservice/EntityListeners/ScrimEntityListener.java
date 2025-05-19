package kz.saya.finals.scrimservice.EntityListeners;

import jakarta.persistence.PostUpdate;
import kz.saya.finals.scrimservice.Entities.Scrim;
import kz.saya.finals.scrimservice.Repositories.ScrimRepository;
import lombok.Setter;

public class ScrimEntityListener {
    @Setter
    private static ScrimRepository scrimRepository;

    @PostUpdate
    public void onPostUpdate(Scrim scrim) {
        System.out.println("Scrim entity updated: " + scrim);
        if(scrim.isEnded() && scrim.isRanked()){

        }
    }

}
