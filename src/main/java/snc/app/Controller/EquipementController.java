package snc.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import snc.app.Model.Equipement;
import snc.app.Model.Incident;
import snc.app.Model.Station;
import snc.app.Services.EquipementService;
import snc.app.Services.IncidentService;
import snc.app.Services.StationService;

import java.util.List;

@Controller
@RequestMapping("/equipements")
public class EquipementController {

    private final EquipementService equipementService;
    private final StationService stationService;
    private  final IncidentService incidentService;

    public EquipementController(EquipementService equipementService, StationService stationService,  IncidentService incidentService) {
        this.equipementService = equipementService;
        this.stationService = stationService;
        this.incidentService = incidentService;
    }

    @GetMapping("")
    public String index(Model model) {

        List<Station> stations = stationService.afficherStations();
        List<Equipement> equipement = equipementService.afficherEquipements();

        model.addAttribute("stations", stations);
        model.addAttribute("equipements", equipement);
        return "equipements/index";
    }

    @GetMapping("/details/{id}")
    public String GetDetails(@PathVariable int id, Model model){
        Equipement equipement = equipementService.afficherEquipement(id);

        if (equipement == null) {
            return "error/404";
        }
        List<Incident> incidents = incidentService.afficherIncidentsProvenantDeEquipement(equipement.getId());

        model.addAttribute("incidents", incidents);
        model.addAttribute("equipement", equipement);

        return "equipements/details";
    }
}
