package ug.edu.breeder.breeder.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ug.edu.breeder.breeder.service.BreederService;
import ug.edu.breeder.breeder.service.serviceimpl.BreederLoginInput;
import ug.edu.breeder.breeder.service.serviceimpl.BreederOutput;
import ug.edu.breeder.breeder.service.serviceimpl.BreederRegistrationInput;

@RestController
@RequestMapping("/api/v1/authorization")
@CrossOrigin(origins = "http://localhost:3000")
class BreederController {

    private final BreederService breederService;

    BreederController(BreederService breederService) {
        this.breederService = breederService;
    }

    @PostMapping("/login")
    ResponseEntity<BreederOutput> login(@RequestBody BreederLoginInput breederLoginInput) {
        BreederOutput breederOutput = breederService.loginBreeder(breederLoginInput);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(breederLoginInput.email(), breederLoginInput.password());

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, headers.getFirst("Authorization")).body(breederOutput);
    }

    @PostMapping("/register")
    ResponseEntity<BreederOutput> register(@RequestBody BreederRegistrationInput breederRegistrationInput) {
        BreederOutput breederOutput = breederService.registerBreeder(breederRegistrationInput);
        return ResponseEntity.status(HttpStatus.OK).body(breederOutput);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<BreederOutput> deregister(@PathVariable("id") Long id){
        breederService.deregisterBreeder(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
