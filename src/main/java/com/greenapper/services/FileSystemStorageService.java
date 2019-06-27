package com.greenapper.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Service that handles reading and writing files to the filesystem.
 */
public interface FileSystemStorageService {
	/**
	 * Saves the supplied {@link MultipartFile} as an image to the filesystem.
	 *
	 * @param image Multipart file containing the image to store
	 * @return The path the file was stored to, or null if it was not stored
	 */
	String saveImage(final MultipartFile image);

	/**
	 * Attempts to retrieve a previously stored image given its path.
	 *
	 * @param path Path to the desired image
	 * @return An array of bytes representing the image, wrapped in an optional
	 */
	Optional<byte[]> readImage(final String path);
}
