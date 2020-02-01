package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.User;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FileService {

    private HostService hostService;
    private HGroupService hGroupService;

    @Autowired
    public FileService(HostService hostService, HGroupService hGroupService) {
        this.hostService = hostService;
        this.hGroupService = hGroupService;
    }

    public Map<String, Integer> hostsImport(User loggedUser,
                                            Map<String, Integer> report,
                                            MultipartFile file) throws IOException, CsvException {

        int importSuccess = 0;
        int importWarnings = 0;
        int importErrors = 0;

        List<Host> importList = new ArrayList<>();
        HGroup defaultGroup = hGroupService.getGroupByName("default");
        List<Host> hostsInDatabase = hostService.getAllHostsByUser(loggedUser);
        BufferedReader fileContent = new BufferedReader(new InputStreamReader(file.getInputStream()));
        if (Objects.equals(file.getContentType(), "text/plain")) {
            String line;
            while ((line = fileContent.readLine()) != null) {
                if (!line.trim().startsWith("#") && !line.trim().startsWith("//") && !(line.trim().length() < 1)) {
                    Host hostToAdd = parse(line);
                    hostToAdd.setOwner(loggedUser);
                    importList.add(hostToAdd);
                }
            }

            importErrors = importList.size();
            for (Host host : hostsInDatabase) {
                importList = importList.stream().filter(x -> !x.compareAddress(host)).collect(Collectors.toList());
            }
            importErrors -= importList.size();

            for (Host host : importList) {
                if (host.getName().equals("") || host.getName() == null) {
                    host.setHostGroup(defaultGroup);
                } else {
                    HGroup group = hGroupService.getGroupByName(host.getName());
                    if (group == null) {
                        group = new HGroup(host.getName(), "");
                        group = hGroupService.save(group);
                    }
                    host.setHostGroup(group);
                }
                host.setOwner(loggedUser);
                if (isValidAddress(host.getAddress())) {
                    importSuccess++;
                } else {
                    importWarnings++;
                }
            }
        } else if (Objects.equals(file.getContentType(), "application/vnd.ms-excel")
                || Objects.equals(file.getContentType(), "text/csv")) {
            CSVParserBuilder parserBuilder = new CSVParserBuilder()
                    .withEscapeChar('\\')
                    .withIgnoreLeadingWhiteSpace(true)
                    .withQuoteChar('"')
                    .withSeparator(';');
            CSVReaderBuilder readerBuilder = new CSVReaderBuilder(fileContent).withCSVParser(parserBuilder.build());
            CSVReader reader = readerBuilder.build();
            List<String[]> linesList = new ArrayList<>(reader.readAll());

            // CSV file structure
            // address;name;description;group
            for (String[] line : linesList) {
                Host hostToAdd = parse(line);
                if (!hostToAdd.getAddress().equals("") && hostToAdd.getHostGroup() == null) {
                    hostToAdd.setHostGroup(defaultGroup);
                }
                if (!hostToAdd.getAddress().equals("")) {
                    hostToAdd.setOwner(loggedUser);
                    importList.add(hostToAdd);
                }
            }

            importErrors = importList.size();
            for (Host host : hostsInDatabase) {
                importList = importList.stream().filter(x -> !x.compareAddress(host)).collect(Collectors.toList());
            }
            importErrors -= importList.size();

            for (Host host : importList) {
                if (isValidAddress(host.getAddress())) {
                    importSuccess++;
                } else {
                    importWarnings++;
                }
            }
        }

        if (importList.size() > 0) {
            for (Host host : importList) {
                hostService.save(host);
            }
        }

        report.replace("importSuccess", importSuccess);
        report.replace("importWarnings", importWarnings);
        report.replace("importErrors", importErrors);

        return report;
    }

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

    public Host parse(String[] line) {
        Host result = new Host("", "", "", null, null);
        if (line.length > 0 && !line[0].trim().startsWith("#") && !line[0].trim().startsWith("//")) {
            result.setAddress(line[0]);
            if (line.length > 1) {
                result.setName(line[1]);
                if (line.length > 2) {
                    result.setDescription(line[2]);
                    if (line.length > 3) {
                        HGroup group = hGroupService.getGroupByName(line[3]);
                        if (group == null) {
                            group = new HGroup(line[3], "");
                            group = hGroupService.save(group);
                        }
                        result.setHostGroup(group);
                    }
                }
            }
        }
        return result;
    }
}
