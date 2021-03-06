package com.bae.sad.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Entity
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
    // Name of the user
    // Maybe let user choose to disclose name?
    private String name;

    @NotBlank
    @Size(max = 40)
    // Username of the user. Always display this.
    private String username;

    @Size(max = 40)
    // Facebook account
    private String facebookAccount;

    @Size(max = 40)
    // Instagram account
    private String instagramAccount;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @OneToOne
    private UserProfile userProfile;

    @NotBlank
    @Size(max = 100)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    // This is where we would declare the data structure to store matches

    public User() { }


    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInstagramAccount(String instagramAccount) {
        this.instagramAccount = instagramAccount;
    }

    public String getInstagramAccount() {
        return instagramAccount;
    }

    public void setFacebookAccount(String facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public String getFacebookAccount() {
        return facebookAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
