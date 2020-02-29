package app.corona.coronatracker.contoller;

import app.corona.coronatracker.model.CoronaStats;
import app.corona.coronatracker.service.CoronaTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HelloController {
    @Autowired
    private CoronaTrackerService coronaTrackerService;
    
    @GetMapping("/home")
    public String homePage(Model model) {
        List<CoronaStats> coronaStats = coronaTrackerService.latestCoronaStats();
        int totalReportedCases = coronaStats.stream().mapToInt(CoronaStats::getCount).sum();
        model.addAttribute("locationStats", coronaStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        return "home";
    }
}
