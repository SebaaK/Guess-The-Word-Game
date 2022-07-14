package kots.controller;

import kots.controller.dto.WordFileDownloadDto;
import kots.controller.dto.WordMetadataDto;
import kots.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @GetMapping
    public ResponseEntity<List<WordMetadataDto>> getWords() {
        return ResponseEntity.ok(wordService.getWords());
    }

    @GetMapping(value = "/{wordName}", produces = "audio/mpeg")
    public ResponseEntity<Resource> getWordFile(@PathVariable String wordName) {
        WordFileDownloadDto wordDto = wordService.getWord(wordName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + wordDto.getName() + ".mp3\"")
                .body(wordDto.getSoundFile());
    }

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<WordMetadataDto> uploadWord(@RequestParam MultipartFile file,
                                                      @RequestParam("wordName") String wordName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wordService.store(file, wordName));
    }

    @DeleteMapping("/{wordName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteWord(@PathVariable String wordName) {
        wordService.deleteWord(wordName);
        return ResponseEntity.noContent().build();
    }
}
