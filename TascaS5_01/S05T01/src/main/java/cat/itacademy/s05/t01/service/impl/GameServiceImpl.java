package cat.itacademy.s05.t01.service.impl;

import cat.itacademy.s05.t01.exception.custom.GameNotFoundException;
import cat.itacademy.s05.t01.management.GameManager;
import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.model.dto.GameDTO;
import cat.itacademy.s05.t01.model.dto.PlayerDTO;
import cat.itacademy.s05.t01.model.enums.Move;
import cat.itacademy.s05.t01.model.enums.PlayerStatus;
import cat.itacademy.s05.t01.model.player.PlayerPlaying;
import cat.itacademy.s05.t01.repository.GameRepository;
import cat.itacademy.s05.t01.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerServiceImpl playerService;
    @Autowired
    private GameManager gameManager;


    @Override
    public Mono<GameDTO> newGame(String playerName) {
        return gameManager.initiateGame()
                .flatMap(game -> addPlayer(game, playerName))
                .flatMap(gameRepository::save)
                .map(savedGame -> new GameDTO(savedGame.getId(), savedGame.getPlayer().getUsername()));
    }

    @Override
    public Mono<Game> addPlayer(Game game, String playerName) {
        return playerService.getPlayerByName(playerName)
                .switchIfEmpty(playerService.createPlayer(playerName))
                .flatMap(player -> {
                    PlayerPlaying newPlayer = new PlayerPlaying(player.getId(), player.getUsername());
                    game.setPlayer(newPlayer);
                    return Mono.just(game);
                });
    }

    @Override
    public Mono<GameDTO> getGamebyId(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with id " + id + " not found.")))
                .map(foundGame -> new GameDTO(foundGame.getId(), foundGame.getPlayer().getUsername(), foundGame.getStatus()));
    }

    @Override
    public Mono<Void> play(String id, Move move, int bet) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with id " + id + " not found.")))
                .flatMap(game -> gameManager.play(game, move, bet)) // Encadena play
                .flatMap(game -> gameManager.croupierMakesMove(game)) // Encadena croupierMakesMove
                .flatMap(game -> gameManager.determineFinalStatus(game)) // Encadena determineFinalStatus
                .flatMap(gameRepository::save) // Guarda el joc actualitzat
                .flatMap(game -> playerService.addPlayedGame(game.getPlayer().getId())) // Registra la partida jugada
                .then(); // Retorna Mono<Void> quan tot s'ha completat
        //veure si pot retornar un DTO!!


    }

    @Override
    public Mono<Void> deleteGame(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with id " + id + " not found.")))
                .flatMap(g -> gameRepository.deleteById(id));
    }

}