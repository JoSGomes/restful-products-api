package com.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ColaboratorController {

    @GetMapping("/colaborator/{filter}")
    public String AllColaborators(@PathVariable String filter){
        return String.format("%s", filter);
    }
}