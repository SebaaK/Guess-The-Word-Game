package kots.controller;

import kots.controller.dto.WordMetadataDto;
import kots.model.WordDifficulty;
import kots.repository.WordRepository;
import kots.service.WordService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
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

    @AfterEach
    void cleanUp() {
        wordRepository.deleteAll();
    }

    @Test
    @WithAnonymousUser
    public void shouldReceiveUnauthorizedStatusWhenQueryEndpointAnonymousUser() throws Exception {
        // given
        generateWordsData(5);
        String wordName = TEST_FILE + 1;

        // when & then
        mockMvc.perform(get(WORDS_BASE_ENDPOINT))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get(WORDS_BASE_ENDPOINT + wordName))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(multipart(WORDS_BASE_ENDPOINT)
                        .file(generateMockFile("TestFile.mp3", "audio/mpeg"))
                        .param("wordName", "TestWordName")
                )
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete(WORDS_BASE_ENDPOINT + wordName))
                .andExpect(status().isUnauthorized());
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
                .andExpect(jsonPath("$", hasSize(wordsCount)))
                .andExpect(jsonPath("$[0].word", is(TEST_FILE + 1)))
                .andExpect(jsonPath("$[0].difficulty", is(WordDifficulty.HARD.toString())));
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

    @Test
    public void shouldThrowExceptionWhenWordIsNoExists() throws Exception {
        mockMvc.perform(get(WORDS_BASE_ENDPOINT + "noExistWord"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Word not found")));
    }

    @Test
    public void shouldReceiveStatusForbiddenUserNoHaveAdminRoleWhenCreateNewWord() throws Exception {
        mockMvc.perform(multipart(WORDS_BASE_ENDPOINT)
                        .file(generateMockFile("file.mp3", MP3_MEDIA_TYPE))
                        .param("wordName", "TestWord")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void shouldCreateNewWord() throws Exception {
        String testWord = "TestWord";
        mockMvc.perform(multipart(WORDS_BASE_ENDPOINT)
                        .file(generateMockFile("file.mp3", MP3_MEDIA_TYPE))
                        .param("wordName", testWord)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.word", is(testWord)))
                .andExpect(jsonPath("$.difficulty", is(WordDifficulty.HARD.toString())));
        assertTrue(wordRepository.existsWordByWord(testWord));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void shouldReceiveConflictStatusWhenCreateNewWord() throws Exception {
        // given
        generateWordsData(1);

        // when & then
        mockMvc.perform(multipart(WORDS_BASE_ENDPOINT)
                        .file(generateMockFile("file.mp3", MP3_MEDIA_TYPE))
                        .param("wordName", TEST_FILE + 1)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("That word already exist")));
    }

    @Test
    public void shouldReceiveStatusForbiddenUserNoHaveAdminRoleWhenDeleteWord() throws Exception {
        // given
        generateWordsData(1);

        // when & then
        String wordName = TEST_FILE + 1;
        mockMvc.perform(delete(WORDS_BASE_ENDPOINT + wordName))
                .andExpect(status().isForbidden());
        assertTrue(wordRepository.existsWordByWord(wordName));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void shouldReceiveNoContentStatusWhenDeleteWord() throws Exception {
        // given
        generateWordsData(1);
        String wordName = TEST_FILE + 1;

        // when & then
        mockMvc.perform(delete(WORDS_BASE_ENDPOINT + wordName))
                .andExpect(status().isNoContent());
        assertFalse(wordRepository.existsWordByWord(wordName));
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