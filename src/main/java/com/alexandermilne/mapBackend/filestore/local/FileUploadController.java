package com.alexandermilne.mapBackend.filestore.local;

import com.alexandermilne.mapBackend.filestore.local.storage.StorageFileNotFoundException;
import com.alexandermilne.mapBackend.filestore.local.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = {"*", "http://localhost:3000", "http://localhost"})
@RequestMapping("api/v1/user-videos")
public class FileUploadController {
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute(
                "files", storageService.loadAll().map(
                    path -> MvcUriComponentsBuilder.fromMethodName(
                            FileUploadController.class,
                            "serveFile",
                            path.getFileName().toString()
                    ).build().toUri().toString()
                ).collect(Collectors.toList())
        );
        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public void handleFileUpload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("userId") UUID userId,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(userId, file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

       // return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}




















