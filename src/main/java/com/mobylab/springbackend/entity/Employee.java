package com.mobylab.springbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "employees", schema = "project")
public class Employee {
    @Id
    private UUID id;

}
