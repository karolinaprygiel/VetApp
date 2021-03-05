package uj.jwzp2021.gp.VetApp.controllers.thyme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uj.jwzp2021.gp.VetApp.services.VisitService;

@Controller
@RequestMapping("/ui")
public class VisitsThymeController {

    private final VisitService visitsService;

    @Autowired
    public VisitsThymeController(VisitService visitsService) {
        this.visitsService = visitsService;
    }

    @GetMapping("/visits")
    public String showAllVisits(Model model) {
        model.addAttribute("visits", visitsService.getAllVisits());
        return "visits";
    }

}
