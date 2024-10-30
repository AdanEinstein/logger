package com.test.logger.controllers;

import com.test.logger.logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private Logger logger;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFile() {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(tick -> readLinesFromFile())
                .map(String.class::cast);
    }

    private Flux<Object> readLinesFromFile() {
        Path path = Paths.get(logger.getFileAppenderFileName());
        return Flux.create(sink -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sink.next(line);
                }
                // Espera novas linhas
                while (true) {
                    if (reader.ready()) {
                        line = reader.readLine();
                        if (line != null) {
                            sink.next(line);
                        }
                    }
                }
            } catch (Exception e) {
                sink.error(e);
            }
        }).doOnError(e -> {
            System.err.println("Erro no fluxo: " + e.getMessage());
        }).doOnComplete(() -> {
            System.out.println("Fluxo completado.");
        });
    }
}
