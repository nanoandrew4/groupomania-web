package com.greenapper.services.impl;

import com.greenapper.exceptions.UnknownIdentifierException;
import com.greenapper.forms.ImageForm;
import com.greenapper.logging.LogManager;
import com.greenapper.services.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

@Service
public class DefaultFileSystemStorageService implements FileSystemStorageService {

	@Value("${groupomania.filestorage.rootdir}")
	private String rootStorageDir;

	private MessageDigest md;

	@Autowired
	private LogManager LOG;

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
	public String saveImage(final String ownerUsername, final ImageForm image) {
		if (image != null && image.getSize() > 0) {
			final String contentType = Objects.requireNonNull(image.getType()).replace("image/", "");
			String relativeStoragePath = "";
			try {
				relativeStoragePath = generateFileNameForStorage(ownerUsername, image, contentType);
				final File outputFile = new File(rootStorageDir + relativeStoragePath);

				Files.createDirectories(Paths.get(outputFile.getAbsolutePath()));

				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image.getBytes());
				BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
				ImageIO.write(bufferedImage, contentType, outputFile);
				LOG.info("Stored file with name: " + relativeStoragePath);
				return relativeStoragePath;
			} catch (FileAlreadyExistsException e) {
				LOG.info("File with name: \'" + relativeStoragePath + "\' already exists, returning \'" + relativeStoragePath + "\' to caller");
				return relativeStoragePath;
			} catch (IOException e) {
				LOG.error("Reading bytes from image with name + \'" + image.getName() + "\' and user \'" + ownerUsername + "\' failed", e);
			}
		}

		return null;
	}

	@Override
	public byte[] readImage(final String name) {
		LOG.info("Attempting to read image with name: " + name);
		try {
			return Files.readAllBytes(Paths.get(rootStorageDir + name));
		} catch (IOException e) {
			LOG.error("Could not read image with name: \'" + name + "\'");
		}
		throw new UnknownIdentifierException("No image with name: \'" + name + "\' found");
	}

	public String generateFileNameForStorage(final String ownerUsername, final ImageForm imageForm, final String contentType) {
		/*
		 * Files are stored in a directory named after the hashed username, and the file name itself is hashed,
		 * so that anonymous users cannot trivially retrieve any stored file.
		 */
		String hashedFileName = new String(Base64.getEncoder().encode(md.digest(imageForm.getBytes())))
				.replaceAll("/", "?").replaceAll("\\+", "?");
		hashedFileName += "." + contentType;

		return getSessionUsernameHash(ownerUsername) + "/" + hashedFileName;
	}

	private String getSessionUsernameHash(final String ownerUsername) {
		return new String(Base64.getEncoder().encode(md.digest(ownerUsername.getBytes())))
				.replaceAll("/", "?").replaceAll("\\+", "?");
	}
}
