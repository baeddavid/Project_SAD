package com.bae.sad.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.bae.sad.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
}
