package ru.ibs.intern.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.ibs.intern.Token;
import ru.ibs.intern.service.FakeService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@RestController
@EnableAutoConfiguration(exclude = HibernateJpaAutoConfiguration.class)
@RequestMapping(value = "/fakeApiHH", consumes = {MediaType.ALL_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)

public class FakeController {

    @Autowired
    FakeService fakeService;

    public static final String OAUTH_TOKEN_ENDPOINT = "oauth/token";

    public static String token;

    @PostMapping(value =OAUTH_TOKEN_ENDPOINT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Token auth(@RequestBody MultiValueMap<String,String> paramMap) {
        token = fakeService.verifyClientCredentials(paramMap);
        return new Token(token, "bearer");
    }

    @GetMapping(value ="resumes", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String resumes(@RequestParam(required = false) String page) throws IOException {
        String str =  ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader("Authorization");
        if (("Bearer " + token).equals(str)) {
            if (page != null) {
                File resource = new ClassPathResource("data/page" + page + ".json").getFile();
                return new String(Files.readAllBytes(resource.toPath()));
            } else {
                File resource = new ClassPathResource("data/page0.json").getFile();
                return new String(Files.readAllBytes(resource.toPath()));
            }

        } else {
            return null;
        }
    }

    @GetMapping(value ="resumes/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String resumes1(@PathVariable(name = "id") String id) throws IOException {
        String str =  ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader("Authorization");
        if (("Bearer " + token).equals(str)) {
            File resource = new ClassPathResource("data/" + id + ".json").getFile();
            return new String(Files.readAllBytes(resource.toPath()));
        } else {
            return null;
        }
    }

}
