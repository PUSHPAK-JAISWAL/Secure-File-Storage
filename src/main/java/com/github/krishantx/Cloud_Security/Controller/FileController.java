package com.github.krishantx.Cloud_Security.Controller;

import com.github.krishantx.Cloud_Security.model.FileModel;
import com.github.krishantx.Cloud_Security.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("fileName") String fileName) throws Exception {
        return ResponseEntity.ok(fileService.uploadFile(file, fileName));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable int id) throws IOException {
        return fileService.downloadFile(id);
    }

    @GetMapping("/my-uploads")
    public ResponseEntity<List<FileModel>> myUploads() {
        return ResponseEntity.ok(fileService.myUploads());
    }
}