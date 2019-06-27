package com.greenapper.services.impl;

import com.greenapper.services.FileSystemStorageService;
import com.greenapper.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
public class DefaultFileSystemStorageService implements FileSystemStorageService {

	@Autowired
	private SessionService sessionService;

	@Value("${groupomania.filestorage.rootdir}")
	private String rootStorageDir;

	private MessageDigest md;

	private Logger LOG = LoggerFactory.getLogger(this.getClass());

	/**
	 * Initializes the {@link MessageDigest} instance that will be used for hashing usernames and filenames for secure
	 * storage of files on the filesystem.
	 */
	public DefaultFileSystemStorageService() {
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Could not instantiate message digest for hashing filenames");
		}
	}

	@Override
	public String saveImage(final MultipartFile image) {
		if (image.getSize() > 0) {
			final String contentType = Objects.requireNonNull(image.getContentType()).replace("image/", "");
			String relativeStoragePath = "";
			try {
				/*
				 * Files are stored in a directory named after the hashed username, and the file name itself is hashed,
				 * so that anonymous users cannot trivially retrieve any stored file.
				 */
				String hashedFileName = new String(Base64.getEncoder().encode(md.digest(image.getBytes()))).replaceAll("/", "?");
				hashedFileName += "." + contentType;

				relativeStoragePath = getSessionUsernameHash() + "/" + hashedFileName;
				final File outputFile = new File(rootStorageDir + relativeStoragePath);

				Files.createDirectories(Paths.get(outputFile.getAbsolutePath()));

				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image.getBytes());
				BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
				ImageIO.write(bufferedImage, contentType, outputFile);
				LOG.info("Stored file with name: " + hashedFileName);
				return relativeStoragePath;
			} catch (FileAlreadyExistsException e) {
				LOG.info("File with name: \'" + relativeStoragePath + "\' already exists, returning \'" + relativeStoragePath + "\' to caller");
				return relativeStoragePath;
			} catch (IOException e) {
				LOG.error("Reading bytes from image with name + \'" + image.getName() + "\' and user \'" + sessionService.getSessionUser().getUsername() + "\' failed", e);
			}
		}

		return null;
	}

	@Override
	public Optional<byte[]> readImage(String name) {
		try {
			return Optional.of(Files.readAllBytes(Paths.get(rootStorageDir + name)));
		} catch (IOException e) {
			LOG.error("Could not read image with name: \'" + name + "\'");
		}
		return Optional.empty();
	}

	private String getSessionUsernameHash() {
		return new String(Base64.getEncoder().encode(md.digest(sessionService.getSessionUser().getUsername().getBytes()))).replaceAll("/", "?");
	}
}
