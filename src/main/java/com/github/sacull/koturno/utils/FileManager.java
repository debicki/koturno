package com.github.sacull.koturno.utils;

import com.github.sacull.koturno.entities.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
                try {
                    Host hotsToAdd = parse(line);
                    result.add(hotsToAdd);
                } catch (UnknownHostException e) {
                    logger.error("Unidentified host in line: \"{}\"", line);
                }
            }
        }
        return result;
    }

    static private Host parse (String line) throws UnknownHostException {
        int charCounter = 0;
        StringBuilder address = new StringBuilder();
        while (line.charAt(charCounter) == ' ') {
            charCounter++;
        }
        while (line.charAt(charCounter) != ' ') {
            address.append(line.charAt(charCounter));
            charCounter++;
        }
        while (line.charAt(charCounter) == ' ') {
            charCounter++;
        }
        return new Host(InetAddress.getByName(address.toString()), line.substring(charCounter), null);
    }
}
