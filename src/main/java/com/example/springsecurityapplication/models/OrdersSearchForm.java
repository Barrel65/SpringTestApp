package com.example.springsecurityapplication.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;

@Entity
public class OrdersSearchForm {
    @Pattern(regexp = "[a-zA-Z0-9]{4}", message = "Название должно состоять из 4 символов и содержать только цифры и буквы")
    private String search;

    @Id
    private Long id;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

