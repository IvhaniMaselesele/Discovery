package za.co.discovery.assignment.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import za.co.discovery.assignment.services.PlanetService;

@Controller
public class RootController {
    PlanetService planetService;

    @Autowired
    public RootController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/planetsList")
    public String getPlanetsList(Model model) {
        //ModelAndView mav = new ModelAndView("planetsList");
        model.addAttribute("planets", planetService.getPlanets());
        return "planetsList";
    }


    /*
    @RequestMapping("planet/{id}")
    public String showPlanet(@PathVariable Integer id, Model model){
        model.addAttribute("product", productService.getProductById(id));
        return "productshow";
    }*/


}
