package kots.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "games")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Game {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private LocalDateTime started;
    private LocalDateTime finished;
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;
    @ManyToOne
    private Word word;
    @ManyToOne
    private User user;
}
