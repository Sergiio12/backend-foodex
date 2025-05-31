package es.sasensior.foodex.presentation.restcontrollers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController { 
	
	@Value("${upload.dir}")
    private String uploadDir;
	
	@Value("${static.dir}")
    private String staticDir;

    @GetMapping("/{imgOrigen}/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imgOrigen, @PathVariable String imageName) {
        Path imagePath;

        if (imgOrigen.equalsIgnoreCase("static")) {
            imagePath = Paths.get(staticDir).resolve(imageName);
        } else if (imgOrigen.equalsIgnoreCase("upload")) {
            imagePath = Paths.get(uploadDir).resolve(imageName);
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        Resource resource = new FileSystemResource(imagePath);

        if (resource.exists()) {
            MediaType mediaType = getMediaType(imageName);
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private MediaType getMediaType(String imageName) {
        if (imageName.endsWith(".jpg") || imageName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (imageName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (imageName.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
