package kots.controller;

import kots.controller.dto.WordMetadataDto;
import kots.model.Word;
import kots.repository.WordRepository;
import kots.service.WordService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WordControllerTest {

    public static final String MP3_MEDIA_TYPE = "audio/mpeg";
    public static final String WORDS_BASE_ENDPOINT = "/api/words/";
    public static final String TEST_FILE = "TestFile-";
    public static final byte[] FILE_IN_BYTES = "TestMusic".getBytes();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private WordService wordService;

    @BeforeEach
    void cleanUp() {
        wordRepository.deleteAll();
    }

    @Test
    public void shouldReceiveWordsList() throws Exception {
        // given
        int wordsCount = 2;
        generateWordsData(wordsCount);

        // when & then
        mockMvc.perform(get(WORDS_BASE_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", hasSize(wordsCount)));
    }

    @Test
    public void shouldReceiveWordFileToDownload() throws Exception {
        // given
        WordMetadataDto savedWord = generateWordsData(1).get(0);

        // when & then
        MockHttpServletResponse result = mockMvc.perform(get(WORDS_BASE_ENDPOINT + savedWord.getWord()))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThat(result.getContentType()).isEqualTo("audio/mpeg");
        assertThat(result.getContentAsByteArray()).isNotEmpty().isEqualTo(FILE_IN_BYTES);
    }

    private List<WordMetadataDto> generateWordsData(int count) {
        List<WordMetadataDto> savedWords = new ArrayList<>();
        for(int i = 1; i <= count; i++) {
            savedWords.add(wordService.store(generateMockFile("test " + i + ".mp3", MP3_MEDIA_TYPE), TEST_FILE + i));
        }
        return savedWords;
    }

    private MockMultipartFile generateMockFile(String originalFileName, String contentType) {
        return new MockMultipartFile(
                "file",
                originalFileName,
                contentType,
                FILE_IN_BYTES);
    }
}