package kots.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "games")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Game {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime started;
    private LocalDateTime finished;
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;
    @ManyToOne
    private Word word;
    @ManyToOne
    private User user;
    @ElementCollection(targetClass = Character.class)
    private List<Character> foundChars;

    @PrePersist
    void setupTime() {
        started = LocalDateTime.now();
    }
}
