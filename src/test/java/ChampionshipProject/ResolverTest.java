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
        // --- ИСПРАВЛЕНИЕ ЗДЕСЬ ---
        // Убираем "StrangeGuy" из общих данных, чтобы он не мешал другим тестам.
        List<Player> testPlayers = Arrays.asList(
                new Player("Ivan", "TeamA", "CityA", "DEFENDER", "Germany", "Agency1", 100, 10, 5, 2, 1, 0),
                new Player("Oleg", "TeamA", "CityA", "FORWARD", "Russia", null, 50, 5, 10, 0, 0, 1),
                new Player("Hans", "TeamB", "CityB", "DEFENDER", "Germany", "Agency1", 200, 20, 0, 0, 0, 5)
        );

        when(playerProvider.getPlayers()).thenReturn(testPlayers);
        resolver = new Resolver(playerProvider);
    }

    @Test
    void testGetPlayers() {
        assertNotNull(resolver.getPlayers());
        assertEquals(3, resolver.getPlayers().size());
    }

    @Test
    void testGetCountWithoutAgency() {
        assertEquals(1, resolver.getCountWithoutAgency());
    }

    @Test
    void testMaxDefenderGoalsCount() {
        assertEquals(5, resolver.getMaxDefenderGoalsCount());
    }

    // Теперь этот тест снова работает, потому что Hans - самый дорогой немец (200)
    @Test
    void testExpensiveGermanPosition() {
        assertEquals("Защитник", resolver.getTheExpensiveGermanPlayerPosition());
    }

    // --- ИСПРАВЛЕНИЕ ЗДЕСЬ ---
    // Создаем отдельный тест для проверки default-ветки в switch
    @Test
    void testExpensiveGermanPositionWithUnknown() {
        // Создаем специальные данные ТОЛЬКО для этого теста
        List<Player> specialData = List.of(
                new Player("StrangeGuy", "TeamC", "CityC", "WATERBOY", "Germany", "Agency2", 300, 1, 0, 0, 0, 0)
        );
        // "Учим" наш мок возвращать эти данные
        when(playerProvider.getPlayers()).thenReturn(specialData);
        // Создаем Resolver с этими специальными данными
        Resolver specialResolver = new Resolver(playerProvider);

        // Проверяем, что для "WATERBOY" вернется "Неизвестная позиция"
        assertEquals("Неизвестная позиция", specialResolver.getTheExpensiveGermanPlayerPosition());
    }

    @Test
    void testGetPlayersByPosition() {
        Map<String, String> result = resolver.getPlayersByPosition();
        assertTrue(result.containsKey("DEFENDER"));
        assertTrue(result.get("DEFENDER").contains("Ivan"));
        assertTrue(result.get("DEFENDER").contains("Hans"));
    }

    @Test
    void testGetTeams() {
        Set<String> teams = resolver.getTeams();
        assertEquals(2, teams.size());
        assertTrue(teams.contains("TeamA"));
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
    @Test
    void testExpensiveGermanPosition_WhenNoGermans() {
        List<Player> noGermansList = List.of(
                new Player("Oleg", "TeamA", "CityA", "FORWARD", "Russia", null, 50, 5, 10, 0, 0, 1)
        );

        when(playerProvider.getPlayers()).thenReturn(noGermansList);
        Resolver specialResolver = new Resolver(playerProvider);

        assertEquals("Игроки из Германии не найдены", specialResolver.getTheExpensiveGermanPlayerPosition());
    }
    @Test
    void testMethodsWithEmptyPlayerList() {
        when(playerProvider.getPlayers()).thenReturn(List.of());
        Resolver emptyResolver = new Resolver(playerProvider);

        assertEquals(0, emptyResolver.getMaxDefenderGoalsCount());
        assertEquals("Игроки из Германии не найдены", emptyResolver.getTheExpensiveGermanPlayerPosition());
        assertEquals("Агентства не найдены", emptyResolver.getAgencyWithMinPlayersCount());
        assertEquals("Команды не найдены", emptyResolver.getTheRudestTeam());
    }

    @Test
    void testAgencyWithMinPlayers_WhenAllHaveNoAgency() {
        List<Player> noAgencyList = List.of(
                new Player("Oleg", "TeamA", "CityA", "FORWARD", "Russia", null, 50, 5, 10, 0, 0, 1)
        );
        when(playerProvider.getPlayers()).thenReturn(noAgencyList);
        Resolver resolverWithNoAgencies = new Resolver(playerProvider);

        assertEquals("Агентства не найдены", resolverWithNoAgencies.getAgencyWithMinPlayersCount());
    }
    @Test
    void testTranslatePosition_AllCases() {
        when(playerProvider.getPlayers()).thenReturn(List.of(
                new Player("Gerd", "Team", "City", "FORWARD", "Germany", "Agency", 500, 1,1,1,1,1)
        ));
        assertEquals("Нападающий", new Resolver(playerProvider).getTheExpensiveGermanPlayerPosition());

        when(playerProvider.getPlayers()).thenReturn(List.of(
                new Player("Lothar", "Team", "City", "MIDFIELD", "Germany", "Agency", 500, 1,1,1,1,1)
        ));
        assertEquals("Полузащитник", new Resolver(playerProvider).getTheExpensiveGermanPlayerPosition());

        when(playerProvider.getPlayers()).thenReturn(List.of(
                new Player("Oliver", "Team", "City", "GOALKEEPER", "Germany", "Agency", 500, 1,1,1,1,1)
        ));
        assertEquals("Вратарь", new Resolver(playerProvider).getTheExpensiveGermanPlayerPosition());
    }
}