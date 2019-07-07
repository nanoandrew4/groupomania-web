package com.greenapper.controllers;

import com.greenapper.services.FileSystemStorageService;
import com.greenapper.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for fetching user stored images from their respective buckets for rendering in the frontend.
 */
@RestController
@RequestMapping("/images")
public class ImageFetcherController {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private FileSystemStorageService fileSystemStorageService;

	@GetMapping
	public byte[] findImage(@RequestParam final String fileName) {
		return fileSystemStorageService.readImage(fileName);
	}
}
