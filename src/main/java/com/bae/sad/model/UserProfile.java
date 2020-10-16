package com.bae.sad.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "UserProfile")
//  A class that contains all the information of the user such as age, bio, pictures, etc. Not to be confused with the user model which is intended for account creation and authentication.
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private Integer age;

    @NotBlank
    @Size(min = 120, max = 500)
    private String bio;

    @NotBlank
    private String cityAndState;

    @NotBlank
    @Size(max = 30)
    private String education;

    @NotBlank
    @Size(max = 30)
    private String occupation;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Gender preferredGender;

    // This needs to be deleted
    @Enumerated(EnumType.STRING)
    private LikeStatus likeStatus;

    // Does this make sense to have here?
    @OneToMany(fetch = FetchType.EAGER)
    private List<UserProfile> matches;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Image> pictureList;

    public UserProfile() { }

    public UserProfile(Integer age, String bio, String cityAndState, String education, String occupation, Gender gender, Gender preferredGender, List<Image> pictureList) {
        this.age = age;
        this.bio = bio;
        this.cityAndState = cityAndState;
        this.education = education;
        this.occupation = occupation;
        this.gender = gender;
        this.preferredGender = preferredGender;
        this.pictureList = pictureList;
    }

    public Long getId() {
        return id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCityAndState() {
        return cityAndState;
    }

    public void setCityAndState(String cityAndState) {
        this.cityAndState = cityAndState;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(Gender preferredGender) {
        this.preferredGender = preferredGender;
    }

    public LikeStatus getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(LikeStatus likeStatus) {
        this.likeStatus = likeStatus;
    }

    public List<Image> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<Image> pictureList) {
        this.pictureList = pictureList;
    }
}
