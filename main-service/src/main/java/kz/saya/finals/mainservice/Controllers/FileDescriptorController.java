package kz.saya.finals.mainservice.Controllers;

import kz.saya.finals.common.DTOs.Auth.UserDTO;
import kz.saya.finals.feigns.Clients.UserServiceClient;
import kz.saya.sbasecore.Entity.FileDescriptor;
import kz.saya.sbasecore.POJOs.FileDescriptorDto;
import kz.saya.sbasecore.Service.FileDescriptorService;
import kz.saya.sbasesecurity.Security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileDescriptorController {

    private final FileDescriptorService fileDescriptorService;
    private final UserServiceClient userServiceClient;
    private final JwtUtils jwtUtils;

    @PostMapping("/upload")
    public ResponseEntity<FileDescriptor> uploadFile(
            @RequestBody FileDescriptorDto fileDescriptorDto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String login = auth.getName();
        UserDTO user = userServiceClient.getByLogin(login);
        try {
            fileDescriptorDto.setOwner(user.getId());
            FileDescriptor savedFile = fileDescriptorService.saveFile(fileDescriptorDto);
            return ResponseEntity.ok(savedFile);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable UUID id) {
        return fileDescriptorService.getFileDescriptor(id)
                .map(fileDescriptor -> {
                    ByteArrayResource resource = new ByteArrayResource(fileDescriptor.getFileData());
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDescriptor.getLabel() + "\"")
                            .contentType(MediaType.parseMediaType(fileDescriptor.getMimeType()))
                            .contentLength(fileDescriptor.getSize())
                            .body(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FileDescriptor>> getAllFiles() {
        return ResponseEntity.ok(fileDescriptorService.getAllFileDescriptors());
    }

    @GetMapping("/owner/{owner}")
    public ResponseEntity<List<FileDescriptor>> getFilesByOwner(@PathVariable UUID owner) {
        return ResponseEntity.ok(fileDescriptorService.getFileDescriptorsByOwner(owner));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FileDescriptor> updateFileDescriptor(
            @PathVariable UUID id,
            @RequestBody FileDescriptor updatedFileDescriptor) {
        return fileDescriptorService.updateFileDescriptor(id, updatedFileDescriptor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteFileDescriptor(@PathVariable UUID id) {
        return fileDescriptorService.softDeleteFileDescriptor(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteFileDescriptor(@PathVariable UUID id) {
        fileDescriptorService.deleteFileDescriptor(id);
        return ResponseEntity.noContent().build();
    }
} 