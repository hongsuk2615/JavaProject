package com.springboot.springproject.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class CrudController {

    @GetMapping("/crud")
    public String crudList() {
        return "crud/board";
    }

    @GetMapping("/crud/board/write")
    public String boardWrite() {
        return "crud/boardwrite";
    }

    @GetMapping("/crud/board/content/{id}")
    public String boardWrite(@PathVariable int id) {
        return "crud/boardContent";
    }
}
