package com.mn.aston_test.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "pin")
    private Integer pin;

    public Account() {
    }

    public Account(String name, Integer pin) {
        this.name = name;
        this.pin = pin;
        this.balance = 0.0;
    }

    @Override
    public String toString() {
        return "Account [id = " + id + ", name = " + name + ", balance = " + balance + "]";
    }
}
