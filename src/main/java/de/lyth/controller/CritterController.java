package de.lyth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is example for check if the application run successfully.
 * The class was written by the Udacity team and imported by ME.
 * @author Udacity Team
 */
@RestController
public class CritterController {

    @GetMapping("/test")
    public String test() {
        return "Critter Stater installed successfully!";
    }

}
