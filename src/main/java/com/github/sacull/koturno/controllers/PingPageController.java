package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;

@Controller
@RequestMapping("/ping")
@Slf4j
public class PingPageController {

    private final UserService userService;

    @Autowired
    public PingPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
//    @ResponseBody
    public String execPingCommand(Model model,
                                  Principal principal,
                                  String address) {

        model.addAttribute("firstUser", userService.countUsers() == 0);

        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
        } else {
            model.addAttribute("loggedUser", null);
        }

        StringBuilder linesToPrint = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "ping -n 4 " + address);

        try {
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                linesToPrint.append(line);
                linesToPrint.append("<br>");
            }
        } catch (IOException e) {
            log.warn("Ping command error: {}", e.getMessage());
        }

//        return linesToPrint.toString();
        model.addAttribute("result", linesToPrint);

        return "/WEB-INF/views/ping.jsp";
    }
}
