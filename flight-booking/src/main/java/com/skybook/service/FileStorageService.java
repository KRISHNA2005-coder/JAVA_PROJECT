package com.skybook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileStorageService {

    @Value("${skybook.storage.path:./data}")
    private String storagePath;

    private final ObjectMapper objectMapper;

    public FileStorageService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        // Register JavaTimeModule for LocalDateTime support
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        System.out.println("FileStorageService constructor called");
    }

    @PostConstruct
    public void init() {
        System.out.println("FileStorageService.init() called - Creating storage directory...");
        System.out.println("Storage path: " + storagePath);
        
        try {
            Path path = Paths.get(storagePath);
            System.out.println("Absolute path: " + path.toAbsolutePath());
            
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("✅ Created storage directory at: " + path.toAbsolutePath());
            } else {
                System.out.println("✅ Storage directory already exists at: " + path.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to create storage directory: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not create storage directory", e);
        }
    }

    public void save(String fileName, Object data) {
        System.out.println("Saving to file: " + fileName);
        try {
            File file = new File(storagePath + "/" + fileName + ".json");
            System.out.println("Full file path: " + file.getAbsolutePath());
            objectMapper.writeValue(file, data);
            System.out.println("✅ Successfully saved: " + fileName + ".json");
        } catch (IOException e) {
            System.err.println("❌ Failed to save data to file: " + fileName);
            e.printStackTrace();
            throw new RuntimeException("Failed to save data to file: " + fileName, e);
        }
    }

    public <T> List<T> loadList(String fileName, Class<T> clazz) {
        System.out.println("Loading list from file: " + fileName);
        try {
            File file = new File(storagePath + "/" + fileName + ".json");
            System.out.println("Checking file: " + file.getAbsolutePath());
            
            if (!file.exists()) {
                System.out.println("File does not exist, returning empty list");
                return new ArrayList<>();
            }
            
            List<T> result = objectMapper.readValue(file, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            System.out.println("✅ Loaded " + result.size() + " items from " + fileName);
            return result;
        } catch (IOException e) {
            System.err.println("Warning: Could not read " + fileName + ".json, creating fresh file. Error: " + e.getMessage());
            e.printStackTrace();
            File file = new File(storagePath + "/" + fileName + ".json");
            file.delete();
            return new ArrayList<>();
        }
    }

    public <T> T load(String fileName, Class<T> clazz) {
        try {
            File file = new File(storagePath + "/" + fileName + ".json");
            if (!file.exists()) {
                return null;
            }
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            System.err.println("Warning: Could not read " + fileName + ".json. Error: " + e.getMessage());
            File file = new File(storagePath + "/" + fileName + ".json");
            file.delete();
            return null;
        }
    }

    public void delete(String fileName) {
        File file = new File(storagePath + "/" + fileName + ".json");
        if (file.exists()) {
            file.delete();
        }
    }
}