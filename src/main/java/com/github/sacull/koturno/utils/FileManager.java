package com.github.sacull.koturno.utils;

import com.github.sacull.koturno.entities.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static Logger logger = LoggerFactory.getLogger("FileManager");

    static public List<Host> loadHosts (String fileName) throws IOException {
        List<Host> result = new ArrayList<>();
        FileReader file = new FileReader(new File(fileName));
        BufferedReader fileContent = new BufferedReader(file);
        String line;
        while ((line = fileContent.readLine()) != null) {
            if (!line.trim().startsWith("#") && !line.trim().startsWith("//") && !(line.trim().length() < 1)) {
                Host hotsToAdd = parse(line);
                result.add(hotsToAdd);
            }
        }
        return result;
    }

    static private Host parse (String line) {
        int charCounter = 0;
        StringBuilder address = new StringBuilder();
        StringBuilder hostname = new StringBuilder();
        StringBuilder description = new StringBuilder();
        while (line.charAt(charCounter) == ' ') {
            charCounter++;
        }
        while ((charCounter < line.length()) && (line.charAt(charCounter) != ' ')) {
            address.append(line.charAt(charCounter));
            charCounter++;
        }
        while ((charCounter < line.length()) && (line.charAt(charCounter) == ' ')) {
            charCounter++;
        }
        String[] descriptionElements = line.substring(charCounter).split("\\*");
        if (descriptionElements.length > 1) {
            hostname.append(descriptionElements[0]);
            for (int i = 1; i < descriptionElements.length; i++) {
                description.append(descriptionElements[i]);
                description.append(' ');
            }
        } else if (descriptionElements.length == 1) {
            description.append(descriptionElements[0]);
        }
        return new Host(hostname.toString(), address.toString(), description.toString(), null);
    }
}
