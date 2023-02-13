package com.techiq.fileupload.service;

import com.techiq.fileupload.model.File;
import com.techiq.fileupload.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@AllArgsConstructor
@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public File saveFile(MultipartFile multipartFile) throws Exception {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        if (fileName.contains("..")) {
            throw new Exception("Invalid file name! " + fileName);
        }
        File file = new File(fileName, multipartFile.getContentType(), multipartFile.getBytes());
        return fileRepository.save(file);
    }

    public Optional<File> downloadFile(Long id) {
        return fileRepository.findById(id);
    }
}
