package com.greenapper.controllers;

import com.greenapper.dtos.ErrorDTO;
import com.greenapper.services.FileSystemStorageService;
import com.greenapper.services.SessionService;
import io.swagger.annotations.*;
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
@Api(value = "/images", description = "Controller for handling images")
public class ImageFetcherController {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private FileSystemStorageService fileSystemStorageService;

	@GetMapping
	@ApiOperation(value = "Retrieves an image given its file name", notes = "The image name/URLs are recovered from DTOs that contain them")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Image retrieved successfully"),
			@ApiResponse(code = 404, message = "The image could not be found", response = ErrorDTO.class)
	})
	public byte[] findImage(@RequestParam @ApiParam(value = "Name of the image to retrieve", required = true) final String fileName) {
		return fileSystemStorageService.readImage(fileName);
	}
}
