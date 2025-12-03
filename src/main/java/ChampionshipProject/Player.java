package ChampionshipProject;

public record Player(
        String name,
        String team,
        String city,
        String position,
        String nationality,
        String agency,
        long transferCost,
        int participations,
        int goals,
        int assists,
        int yellowCards,
        int redCards
) {}