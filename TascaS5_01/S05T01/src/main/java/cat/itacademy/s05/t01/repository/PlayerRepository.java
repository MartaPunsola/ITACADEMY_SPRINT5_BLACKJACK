package cat.itacademy.s05.t01.repository;

import cat.itacademy.s05.t01.model.player.Player;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {

    //extends ReactiveSortingRepository<Player, Long>
    //extends ReactiveCrudRepository<PLayer, Long> com que he d'ordenar el ranking, potser aquest
    //triar-ne un

    /*exemple
    * @Query("select t.* from tag t join item_tag it on t.id = it.tag_id where it.item_id = :item_id order by t.name")
    Flux<Tag> findTagsByItemId(Long itemId);*/

    @Query("SELECT * FROM player WHERE lower(username) = lower(:playerName)")
    Mono<Player> getPlayerByName(String playerName);

    @Query("SELECT * FROM player ORDER BY games_Won DESC")
    Flux<Player> findAllSortedByGamesWon();

    //molt complex, però hi ha alguna cosa aprofitable
    //https://medium.com/pictet-technologies-blog/reactive-programming-with-spring-data-r2dbc-ee9f1c24848b


}
