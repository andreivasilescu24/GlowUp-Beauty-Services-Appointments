package com.mobylab.springbackend.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "employees", schema = "project")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "salon_id", nullable = false)
    private BeautySalon beautySalon;
    private int experience;

    private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BeautySalon getBeautySalon() {
        return beautySalon;
    }

    public void setBeautySalon(BeautySalon beautySalon) {
        this.beautySalon = beautySalon;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
