package com.mobylab.springbackend.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "beauty_service", schema = "project")
public class BeautyService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private double price;
    private int duration;

    @ManyToOne
    @JoinColumn(name = "salon_id", nullable = false)
    private BeautySalon beautySalon;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BeautySalon getBeautySalon() {
        return beautySalon;
    }

    public void setBeautySalon(BeautySalon beautySalon) {
        this.beautySalon = beautySalon;
    }
}
