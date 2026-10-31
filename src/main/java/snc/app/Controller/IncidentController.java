package snc.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import snc.app.Model.Incident;
import snc.app.Services.IncidentService;

import java.util.List;

@Controller
@RequestMapping("/incident")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        List<Incident> incidentsNonResolus = incidentService.afficherIncidentsNonResolus();
        model.addAttribute("incidents_non_resolus", incidentsNonResolus);
        return "dashboard";
    }


}
