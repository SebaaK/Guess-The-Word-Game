package kots.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.Resource;

@Getter
@AllArgsConstructor
public class WordFileDto {

    private String name;
    private Resource soundFile;
}
