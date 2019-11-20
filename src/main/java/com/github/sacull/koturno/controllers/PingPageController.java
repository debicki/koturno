package com.github.sacull.koturno.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Controller
@RequestMapping("/ping")
public class PingPageController {

    @GetMapping
    @ResponseBody
    public String execPingCommand(String address) {
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
            e.printStackTrace();
        }

        return linesToPrint.toString();
    }
}
