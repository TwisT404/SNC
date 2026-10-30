package snc.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import snc.app.Model.Equipement;
import snc.app.Model.Station;
import snc.app.Services.EquipementService;
import snc.app.Services.StationService;

import java.util.List;

@Controller
@RequestMapping("/equipement")
public class EquipementController {

    private final EquipementService equipementService;
    private final StationService stationService;

    public EquipementController(EquipementService equipementService, StationService stationService) {
        this.equipementService = equipementService;
        this.stationService = stationService;
    }

    @GetMapping("")
    public String index(Model model) {

        List<Station> stations = stationService.afficherStations();
        List<Equipement> equipement = equipementService.afficherEquipements();

        model.addAttribute("stations", stations);
        model.addAttribute("equipements", equipement);
        return "equipements/index";
    }
}
