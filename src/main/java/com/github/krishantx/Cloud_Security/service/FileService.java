package com.github.krishantx.Cloud_Security.service;

import com.github.krishantx.Cloud_Security.model.FileModel;
import com.github.krishantx.Cloud_Security.model.UserModel;

import com.github.krishantx.Cloud_Security.repo.FileRepo;
import com.github.krishantx.Cloud_Security.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepo fileRepo;

    @Autowired
    private UserRepo userRepo;

    private final Path storageRoot = Paths.get("storage").toAbsolutePath().normalize();

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public ResponseEntity<?> downloadFile(int fileId) throws IOException {
        FileModel fileModel = fileRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!fileModel.getOwner().getUsername().equals(getCurrentUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        Path filePath = storageRoot.resolve(Integer.toString(fileId)).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) return ResponseEntity.notFound().build();

        String contentType = Files.probeContentType(filePath);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModel.getFileName() + "\"")
                .body(resource);
    }

    public String uploadFile(MultipartFile multipartFile, String fileName) throws Exception {
        UserModel owner = userRepo.findByUsername(getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        FileModel fileModel = fileRepo.save(new FileModel(fileName, owner));

        Files.createDirectories(storageRoot);
        Path targetPath = storageRoot.resolve(Integer.toString(fileModel.getFileId()));
        Files.copy(multipartFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return "Success";
    }

    public List<FileModel> myUploads() {
        return fileRepo.findByOwnerUsername(getCurrentUsername());
    }
}