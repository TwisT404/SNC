package snc.app.Controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import snc.app.Model.Station;
import snc.app.Services.StationService;

import java.util.List;

@Controller
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {

        this.stationService = stationService;
    }

    @GetMapping("")
    public String station(Model model) {
        List<Station> stations = stationService.afficherStations();
        model.addAttribute("stations", stations);
        return "stations/index";
    }

    @GetMapping("/ajout")
    public String ajouterStation(Model model) {
        model.addAttribute("station", new Station());
        return "stations/ajout";
    }

    @PostMapping("/ajout/envoie")
    public String ajoutEnvoie(@ModelAttribute("station") Station station) {
        stationService.ajoutStation(station);
        return "redirect:/stations";
    }
}
