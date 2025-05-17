package kz.saya.finals.mainservice.Controllers;

import kz.saya.finals.mainservice.Entities.Region;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/regions")
class RegionController {
    @GetMapping
    public List<String> getAllRegions() {
        return Arrays.stream(Region.values()).map(Enum::name).toList();
    }
}
