package ChampionshipProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResolverTest {

    @Mock
    private IPlayerProvider playerProvider;

    private Resolver resolver;

    @BeforeEach
    void setUp() {
        List<Player> testPlayers = Arrays.asList(
                new Player("Ivan", "TeamA", "CityA", "DEFENDER", "Germany", "Agency1", 100, 10, 5, 2, 1, 0),
                new Player("Oleg", "TeamA", "CityA", "FORWARD", "Russia", null, 50, 5, 10, 0, 0, 1),
                new Player("Hans", "TeamB", "CityB", "DEFENDER", "Germany", "Agency1", 200, 20, 0, 0, 0, 5)
        );

        when(playerProvider.getPlayers()).thenReturn(testPlayers);

        resolver = new Resolver(playerProvider);
    }

    @Test
    void testGetCountWithoutAgency() {
        assertEquals(1, resolver.getCountWithoutAgency());
    }

    @Test
    void testMaxDefenderGoalsCount() {
        assertEquals(5, resolver.getMaxDefenderGoalsCount());
    }

    @Test
    void testExpensiveGermanPosition() {
        assertEquals("Защитник", resolver.getTheExpensiveGermanPlayerPosition());
    }

    @Test
    void testGetTeams() {
        Set<String> teams = resolver.getTeams();
        assertEquals(2, teams.size());
    }

    @Test
    void testTopTeamsByGoals() {
        Map<String, Integer> top = resolver.getTop5TeamsByGoalsCount();
        assertEquals(15, top.get("TeamA"));
        assertEquals(0, top.get("TeamB"));
    }

    @Test
    void testAgencyWithMinPlayers() {
        assertEquals("Agency1", resolver.getAgencyWithMinPlayersCount());
    }

    @Test
    void testRudestTeam() {
        assertEquals("TeamB", resolver.getTheRudestTeam());
    }
}