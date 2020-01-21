package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.Host;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class FileService {

    public boolean isValidAddress(String address) {
        InetAddress host;
        try {
            host = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            return false;
        }
        return true;
    }

    public Host parse(String line) {
        int charCounter = 0;
        line = line.replace('\t', ' ');
        StringBuilder address = new StringBuilder();
        StringBuilder name = new StringBuilder();
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
            name.append(descriptionElements[0]);
            for (int i = 1; i < descriptionElements.length; i++) {
                description.append(descriptionElements[i]);
                description.append(' ');
            }
        } else if (descriptionElements.length == 1) {
            description.append(descriptionElements[0]);
        }
        return new Host(name.toString(), address.toString(), description.toString(), null, null);
    }
}
