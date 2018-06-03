package com.kainos.controller;

import com.kainos.entity.Dataset;
import com.kainos.entity.PricingSample;
import com.kainos.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "data")
public class DataController {

    @Autowired
    private
    DataService dataService;

    @RequestMapping(method = RequestMethod.GET)
    public Dataset getSamples() throws IOException, ParseException {
        return this.dataService.getSamples();

    }
}
