package com.mobylab.springbackend.service.dto;

public class BeautySalonDto {
    private String name;
    private String address;
    private String email;
    private String phone;
    private int numEmployees;
    private String city;

    public String getName() {
        return name;
    }

    public BeautySalonDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public BeautySalonDto setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public BeautySalonDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public BeautySalonDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public int getNumEmployees() {
        return numEmployees;
    }

    public BeautySalonDto setNumEmployees(int numEmployees) {
        this.numEmployees = numEmployees;
        return this;
    }

    public String getCity() {
        return city;
    }

    public BeautySalonDto setCity(String city) {
        this.city = city;
        return this;
    }
}
