package com.ga.sudokuSolver.Controller;

import com.ga.sudokuSolver.Service.SudokuService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;


@RestController
public class SudokuController {

    private final SudokuService sudokuService;

    public SudokuController(SudokuService sudokuService) {
        this.sudokuService = sudokuService;
    }

    @PostMapping("/solve")
    public ResponseEntity<Resource> solve(@RequestParam("file") MultipartFile file) throws Exception {
        Path solutionFile = sudokuService.solve(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(solutionFile.getFileName().toString()).build().toString())
                .contentType(MediaType.TEXT_PLAIN)
                .body(new FileSystemResource(solutionFile));
    }
}