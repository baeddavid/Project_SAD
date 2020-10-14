package com.bae.sad.controller;

import com.bae.sad.repository.ImageRepository;
import com.bae.sad.model.Image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@RestController
@RequestMapping(path = "image")
public class ImageController {
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    ImageRepository imageRepository;

    @PostMapping("/upload")
    public BodyBuilder uploadImage(@RequestParam("imageFile") MultipartFile file) throws Exception {
        System.out.println("Original image byte size - "  + file.getBytes().length);
        Image img = new Image(file.getOriginalFilename(), file.getContentType(), compressBytes(file.getBytes()));
        imageRepository.save(img);
        return ResponseEntity.status(HttpStatus.OK);
    }

    @GetMapping("/get/{imageName}")
    public Image getImage(@PathVariable("imageName") String imageName) throws IOException {
        final Optional<Image> retrievedImage = imageRepository.findByName(imageName);
        Image img = new Image(retrievedImage.get().getName(), retrievedImage.get().getType(), decompressBytes(retrievedImage.get().getPicByte()));
        return img;
    }

    private static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while(!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            logger.error("Error with compressing image metadata. Message - {}", e.getMessage());
        }
        System.out.println("Compressed iamge byte size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        try {
            while(!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch(IOException e) {
            logger.error("Error with decompressing image meta data. Message - {}", e.getMessage());
        } catch (DataFormatException e) {
            logger.error("Error with data format. Message - {}", e.getMessage());
        }
        return outputStream.toByteArray();
    }

}
