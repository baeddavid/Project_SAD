package com.bae.sad.model;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
// TODO We need to figure out how to store user matches. Are we going to use a graph, list, or something else??? ASAP
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        // Each user gets a random generated value for their id value
        private Long id;

        @NotBlank
        @Size(max = 40)
        private String name;

        @NotBlank
        @Size(max = 40)
        private String username;

        @NaturalId
        @NotBlank
        @Size(max = 40)
        @Email
        private String email;

        @NotBlank
        @Size(max = 100)
        private String password;

       public User() { }

        public User(String name, String email, String password) {
                this.name = name;
                this.email = email;
                this.password = password;
        }
}
