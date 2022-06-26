package com.winkey.practice.palindservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Venkatesh Rajendran
 */

@Data
@Entity
@Table(name = "palindrome_table")
@NoArgsConstructor
public class PalindromeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @NonNull
    @Column(name = "data", nullable = false, unique = true)
    private String data;

    public PalindromeEntity(String data) {
        this.data = data;
    }
}
