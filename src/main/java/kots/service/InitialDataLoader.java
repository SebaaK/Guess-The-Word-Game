package kots.service;

import kots.model.Role;
import kots.model.User;
import kots.model.Word;
import kots.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
@Log4j2
class InitialDataLoader {

    @Value("${csvData.fileName:words.csv}")
    private String fileName;

    private final WordRepository wordRepository;
    private final WordChecker wordChecker;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void setUpData(ApplicationReadyEvent event) {
        List<Word> wordList = new ArrayList<>();
        try {
            File file = ResourceUtils.getFile("classpath:" + fileName);
            Scanner rowScanner = new Scanner(file);
            while(rowScanner.hasNext()) {
                wordList.add(completeEntityToSave(rowScanner.next().split(",")));
            }
            rowScanner.close();
        } catch (FileNotFoundException e) {
            log.warn("Initial data error service: " + e.getMessage());
        }
        wordRepository.saveAll(wordList);
    }

    @EventListener
    public void setUpUsersAndRolesData(ApplicationReadyEvent event) {
        userService.saveRole(new Role(1L, "USER"));
        userService.saveUser(new User(1L, "sebastian", passwordEncoder.encode("password"), new HashSet<>()));
        userService.addRoleToUser("sebastian", "USER");

        userService.saveRole(new Role(2L, "ADMIN"));
        userService.saveUser(new User(2L, "sebastian_admin", passwordEncoder.encode("password"), new HashSet<>()));
        userService.addRoleToUser("sebastian_admin", "ADMIN");
    }

    private Word completeEntityToSave(String[] splitRow) {
        return new Word(Long.parseLong(splitRow[0]), splitRow[1], wordChecker.getDifficulty(splitRow[1]), splitRow[2].getBytes());
    }
}
