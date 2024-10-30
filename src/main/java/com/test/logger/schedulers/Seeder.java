package com.test.logger.schedulers;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.logger.entities.Person;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;

@Slf4j
@Component
@EnableScheduling
public class Seeder {

    @Scheduled(fixedRate = 5000)
    public void run() throws IOException, InterruptedException {
        String randomName = getRandomName();
        log.info("Iniciando os insert do %s".formatted(randomName));
        log.info(Person.builder().name(randomName).build().toString());
        log.info("Insert finalizado!");
    }

    private String getRandomName() throws IOException, InterruptedException{
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                                    .uri(URI.create("https://randomuser.me/api/"))
                                    .GET()
                                    .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode tree = new ObjectMapper().readTree(response.body());
        JsonNode jsonNode = tree.withArrayProperty("results").get(0);
        return jsonNode.get("name").get("first").asText();
    }
}
