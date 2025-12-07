package ChampionshipProject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvPlayerProviderTest {

    @TempDir
    Path tempDir;

    @Test
    void testGetPlayersFromFile() throws IOException {
        Path csvFile = tempDir.resolve("test_players.csv");
        String csvContent = """
                Name;Team;City;Position;Nationality;Agency;Transfer cost;Participations;Goals;Assists;Yellow cards;Red cards
                TestPlayer;TestTeam;TestCity;FORWARD;Russia;TestAgency;1000;10;5;2;1;0
                """;
        Files.writeString(csvFile, csvContent);

        CsvPlayerProvider provider = new CsvPlayerProvider(csvFile.toString());
        List<Player> players = provider.getPlayers();

        assertEquals(1, players.size());
        Player p = players.get(0);
        assertEquals("TestPlayer", p.name());
        assertEquals("TestTeam", p.team());
        assertEquals(1000, p.transferCost());
        assertEquals(5, p.goals());
    }
    @Test
    void testGetPlayers_WhenFileDoesNotExist_ThrowsException() {
        String nonExistentFilePath = tempDir.resolve("ghost_file.csv").toString();
        CsvPlayerProvider provider = new CsvPlayerProvider(nonExistentFilePath);

        assertThrows(RuntimeException.class, provider::getPlayers);
    }
    @Test
    void testGetPlayers_WithEmptyAgencyField() throws IOException {
        Path csvFile = tempDir.resolve("empty_agency.csv");
        String csvContent = """
                Name;Team;City;Position;Nationality;Agency;Transfer cost;Participations;Goals;Assists;Yellow cards;Red cards
                NoAgencyPlayer;TeamX;CityY;MIDFIELD;Brazil;;5000;1;1;1;1;1
                """;
        Files.writeString(csvFile, csvContent);

        CsvPlayerProvider provider = new CsvPlayerProvider(csvFile.toString());
        List<Player> players = provider.getPlayers();

        assertEquals(1, players.size());
        assertNull(players.get(0).agency());
    }
}