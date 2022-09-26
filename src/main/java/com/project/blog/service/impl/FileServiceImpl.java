package com.project.blog.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.blog.services.FileService;

@Service
public class FileServiceImpl implements FileService{

	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {
		//get file name : abc.png
		String fileName = file.getOriginalFilename();
		
		//random file name generate
		String randomID = UUID.randomUUID().toString();
		String randomFileNameWithExtension = randomID.concat(fileName.substring(fileName.lastIndexOf('.')));
		
		//get file path
		String filePath = path + File.separator + randomFileNameWithExtension;
		
		//create folder if not created
		File f = new File(path);
		
		if(!f.exists())
			f.mkdir();
		
		//copy file
		Files.copy(file.getInputStream(), Paths.get(filePath));
		
		
		return randomFileNameWithExtension;
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		String fullPath = path + File.separator + fileName;
		InputStream is = new FileInputStream(fullPath);
		return is;
	}

}
