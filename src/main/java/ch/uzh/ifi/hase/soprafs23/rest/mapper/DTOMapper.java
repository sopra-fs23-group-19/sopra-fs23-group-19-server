package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.GameTurn;
import ch.uzh.ifi.hase.soprafs23.entity.Room;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.game.GameTurnPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.RoomGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.room.RoomPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.user.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);


    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "bestScore", ignore = true)
    @Mapping(target = "totalScore", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "currentScore", ignore = true)
    User convertUserLoginPostDTOtoEntity(UserLoginPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "token", target = "token")
    UserLoginGetDTO convertEntityToUserLoginGetDTO(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "bestScore", ignore = true)
    @Mapping(target = "totalScore", ignore = true)
    @Mapping(target = "currentScore", ignore = true)
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "token", target = "token")
    @Mapping(target = "bestScore", source="bestScore")
    @Mapping(target = "totalScore", source="totalScore")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "bestScore", ignore = true)
    @Mapping(target = "totalScore", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "currentScore", ignore = true)
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "roomName", target = "roomName")
    @Mapping(source = "ownerId", target = "ownerId")
    @Mapping(target = "players", ignore = true)
    @Mapping(source = "mode", target = "mode")
    @Mapping(target = "status", ignore = true)
    Room convertRoomPostDTOtoEntity(RoomPostDTO roomPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "roomName", target = "roomName")
    @Mapping(source = "ownerId", target = "ownerId")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "mode", target = "mode")
    @Mapping(source = "status", target = "status")
    RoomGetDTO convertEntityToRoomGetDTO(Room room);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "drawingPlayerId", target = "drawingPlayerId")
    @Mapping(source = "allPlayersIds", target = "allPlayersIds")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "wordsToBeChosen", target = "wordsToBeChosen")
    @Mapping(source = "targetWord", target = "targetWord")
    @Mapping(source = "drawingPhase", target = "drawingPhase")
    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "gameTurnStatus", target = "gameTurnStatus")
    GameTurnGetDTO convertEntityToGameTurnGetDTO(GameTurn gameTurn);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "targetWord", target = "targetWord")
    @Mapping(target = "drawingPlayer", ignore = true)
    @Mapping(target = "allPlayersIds", ignore = true)
    @Mapping(target = "gameId", ignore = true)
    @Mapping(target = "wordsToBeChosen", ignore = true)
    @Mapping(target = "drawingPhase", ignore = true)
    @Mapping(target = "gameTurnStatus", ignore = true)
    @Mapping(target = "playersScores", ignore = true)
    GameTurn convertGameTurnPutDTOtoEntity(GameTurnPutDTO gameTurnPutDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    UserNameDTO convertEntityToUserNameDTO(User user);
}
