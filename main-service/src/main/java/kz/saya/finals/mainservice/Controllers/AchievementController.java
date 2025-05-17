package kz.saya.finals.mainservice.Controllers;

import kz.saya.finals.mainservice.Entities.Achievement;
import kz.saya.finals.mainservice.Repositories.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/achievements")
class AchievementController {

    @Autowired
    private AchievementRepository achievementRepository;

    @GetMapping
    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    @GetMapping("/{id}")
    public Achievement getAchievementById(@PathVariable UUID id) {
        return achievementRepository.findById(id).orElseThrow();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can create achievements
    // System will create Achievement automatically based on the system's pre defined rules
    public Achievement createAchievement(@RequestBody Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    @PutMapping("/{id}")
    public Achievement updateAchievement(@PathVariable UUID id, @RequestBody Achievement updated) {
        Achievement achievement = achievementRepository.findById(id).orElseThrow();
        achievement.setName(updated.getName());
        achievement.setDescription(updated.getDescription());
        achievement.setProgress(updated.getProgress());
        achievement.setIcon(updated.getIcon());
        return achievementRepository.save(achievement);
    }

    @DeleteMapping("/{id}")
    public void deleteAchievement(@PathVariable UUID id) {
        achievementRepository.deleteById(id);
    }

}