package com.techiq.fileupload.controller;

import com.techiq.fileupload.model.File;
import com.techiq.fileupload.model.FileUploadResponse;
import com.techiq.fileupload.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public FileUploadResponse uploadFile(@RequestParam("file")MultipartFile file) throws Exception {
        File attachment = null;
        String downloadURL = "";

        attachment = fileService.saveFile(file);
        downloadURL = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/file/download/ " + attachment.getId())
                .toUriString();
        return new FileUploadResponse(attachment.getFileName(), downloadURL, file.getContentType(), file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long fileId) {
        Optional<File> file = fileService.downloadFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.get().getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "file; fileName=\"" + file.get().getFileName() + "\"")
                .body(new ByteArrayResource(file.get().getData()));
    }
}
