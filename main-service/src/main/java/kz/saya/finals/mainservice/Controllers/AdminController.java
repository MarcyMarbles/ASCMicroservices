package kz.saya.finals.mainservice.Controllers;

import jakarta.annotation.security.PermitAll;
import kz.saya.finals.mainservice.Services.SteamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/testing")
public class AdminController {

    @Autowired
    private SteamService steamService;

    @GetMapping("/steam/getSteamID64")
    @PermitAll
    public String getSteamID64(@RequestParam String steamURL) {
        return steamService.getSteamId(steamURL);
    }

    @GetMapping("/steam/getUserData/{steamId}")
    @PermitAll
    public Object getUserData(@PathVariable String steamId) {
        return steamService.getUserData(steamId);
    }

    @GetMapping("/steam/getUserGames/{steamId}")
    @PermitAll
    public Object getUserGames(@PathVariable String steamId) {
        return steamService.getUserGames(steamId);
    }

    @GetMapping("/steam/getUserAchievements/{steamId}/{appId}")
    @PermitAll
    public Object getUserAchievements(@PathVariable String steamId, @PathVariable String appId) {
        return steamService.getUserAchievements(steamId, appId);
    }

}
