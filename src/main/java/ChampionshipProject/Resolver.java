package ChampionshipProject;

import java.util.*;
import java.util.stream.Collectors;

public class Resolver implements IResolver {

    private final List<Player> players;

    public Resolver(IPlayerProvider playerProvider) {
        this.players = playerProvider.getPlayers();
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public int getCountWithoutAgency() {
        return (int) players.stream().filter(p -> p.agency() == null).count();
    }

    @Override
    public int getMaxDefenderGoalsCount() {
        return players.stream()
                .filter(p -> "DEFENDER".equals(p.position()))
                .mapToInt(Player::goals).max().orElse(0);
    }

    @Override
    public String getTheExpensiveGermanPlayerPosition() {
        return players.stream()
                .filter(p -> "Germany".equals(p.nationality()))
                .max(Comparator.comparingLong(Player::transferCost))
                .map(p -> translatePosition(p.position()))
                .orElse("Игроки из Германии не найдены");
    }

    private String translatePosition(String position) {
        return switch (position) {
            case "GOALKEEPER" -> "Вратарь";
            case "DEFENDER" -> "Защитник";
            case "MIDFIELD" -> "Полузащитник";
            case "FORWARD" -> "Нападающий";
            default -> "Неизвестная позиция";
        };
    }

    @Override
    public Map<String, String> getPlayersByPosition() {
        return players.stream()
                .collect(Collectors.groupingBy(Player::position,
                        Collectors.mapping(Player::name, Collectors.joining(", "))));
    }

    @Override
    public Set<String> getTeams() {
        return players.stream().map(Player::team).collect(Collectors.toSet());
    }

    @Override
    public Map<String, Integer> getTop5TeamsByGoalsCount() {
        return players.stream()
                .collect(Collectors.groupingBy(Player::team, Collectors.summingInt(Player::goals)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public String getAgencyWithMinPlayersCount() {
        return players.stream().filter(p -> p.agency() != null)
                .collect(Collectors.groupingBy(Player::agency, Collectors.counting()))
                .entrySet().stream().min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("Агентства не найдены");
    }

    @Override
    public String getTheRudestTeam() {
        return players.stream()
                .collect(Collectors.groupingBy(Player::team, Collectors.averagingDouble(Player::redCards)))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("Команды не найдены");
    }
}