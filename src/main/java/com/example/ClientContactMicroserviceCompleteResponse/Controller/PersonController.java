package com.example.ClientContactMicroserviceCompleteResponse.Controller;


import com.example.ClientContactMicroserviceCompleteResponse.Model.Person;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
public class PersonController {

    private final RestTemplate restTemplate;
    String url = "http://localhost:8080";

    public PersonController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/people/{name}/{email}/{age}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> addPerson(@PathVariable(name = "name")String name,
                                  @PathVariable(name = "email")String email,
                                  @PathVariable(name = "age") int age){
        Person person = new Person(name, email, age);
        try {

            ResponseEntity<Void> response = restTemplate.postForEntity(url + "/contacts", person, Void.class);
            ResponseEntity<Person[]>  people = restTemplate.getForEntity(url + "/contacts", Person[].class);
            HttpHeaders httpHeaders = people.getHeaders();
            int total = Integer.parseInt(httpHeaders.get("total").get(0));

            if(total == 0){
                return null;
            }

            return Arrays.asList(people.getBody());

        } catch (HttpClientErrorException e){

            e.printStackTrace();
            return null;

        }

    }
}
