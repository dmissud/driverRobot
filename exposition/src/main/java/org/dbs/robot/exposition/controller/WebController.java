package org.dbs.robot.exposition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller to handle web navigation and redirects.
 */
@Controller
public class WebController {

    /**
     * Redirects the root URL to the Swagger UI page.
     *
     * @return RedirectView to the Swagger UI
     */
    @GetMapping("/")
    public RedirectView redirectToSwaggerUI() {
        return new RedirectView("/swagger-ui.html");
    }
}