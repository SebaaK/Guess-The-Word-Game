package kots.repository;

import kots.model.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, String>  {

    Optional<Game> findByUserNameAndId(String id, String user_name);
}
