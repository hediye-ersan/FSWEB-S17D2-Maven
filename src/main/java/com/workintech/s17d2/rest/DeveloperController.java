package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    public Map<Integer, Developer> developers;
    private final Taxable taxable;

    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;

    }

    @PostConstruct
    public void init() {
        this.developers = new HashMap<>();
        developers.put(1, new Developer(1, "Alice", 50000, Experience.SENIOR));
        developers.put(2, new Developer(2, "Bob", 70000, Experience.MID));
        developers.put(3, new Developer(3, "Charlie", 100000, Experience.JUNIOR));
    }

    @GetMapping
    public List<Developer> getAllDevelopers(){
       return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer getDeveloper(@PathVariable int id){
        return developers.get(id);
    }
    @PostMapping
    public ResponseEntity<Developer> addDeveloper(@RequestBody Developer developer) {
        double taxRate = 0;
        if(developer instanceof JuniorDeveloper){
            developer.setSalary(developer.getSalary() - developer.getSalary() * taxable.getSimpleTaxRate() / 100);
        } else if (developer instanceof MidDeveloper) {
            developer.setSalary(developer.getSalary() - developer.getSalary() * taxable.getMiddleTaxRate() / 100);
        } else if (developer instanceof SeniorDeveloper) {
            developer.setSalary(developer.getSalary() - developer.getSalary() * taxable.getUpperTaxRate() / 100);
        }
        developers.put(developer.getId(), developer);
        return ResponseEntity.status(HttpStatus.CREATED).body(developer);
    }

    @PutMapping("/{id}")
    public void updateDeveloper(@PathVariable int id, @RequestBody Developer developer) {
        developers.put(id, developer);
    }

    @DeleteMapping("/{id}")
    public void deleteDeveloper(@PathVariable int id){
        developers.remove(id);
    }

}
