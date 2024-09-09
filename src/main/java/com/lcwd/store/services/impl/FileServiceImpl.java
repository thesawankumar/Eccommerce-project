package com.lcwd.store.services.impl;

import com.lcwd.store.exceptions.BadApiRequest;
import com.lcwd.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        logger.info("File name : {}", originalFilename);
        
        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = filename + extension;
        String fullPathWithFileName = path + File.separator + fileNameWithExtension;
        
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg")) {
            //file Save
            File folder=new File(path);
            if(!folder.exists())
            {
                //create the folder
                folder.mkdirs();
            }
            //upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        } else {
            throw new BadApiRequest("File With this " + extension + "Not Allowed !!");
        }
    }
    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath=path +File.separator+name;
         InputStream inputStream=new FileInputStream(fullPath);
         return inputStream;
    }
}
