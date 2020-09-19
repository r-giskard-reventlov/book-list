package uk.co.foundationsedge.interfaces.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

}
