package ChampionshipProject;

public class Main {
    public static void main(String[] args) {
        String filePath = "fakePlayers.csv";

        // 1. Создаем провайдера
        IPlayerProvider provider = new CsvPlayerProvider(filePath);

        // 2. Создаем резолвер
        IResolver resolver = new Resolver(provider);

        System.out.println("РЕЗУЛЬТАТЫ ЧЕМПИОНАТА\n");

        // 1 зад
        System.out.println(" 1. Игроков без агентства ");
        System.out.println("Ответ: " + resolver.getCountWithoutAgency());

        // 2 зад
        System.out.println("\n 2. Максимальное число голов среди защитников ");
        System.out.println("Ответ: " + resolver.getMaxDefenderGoalsCount());

        // 3 зад
        System.out.println("\n 3. Позиция самого дорогого немца ");
        System.out.println("Ответ: " + resolver.getTheExpensiveGermanPlayerPosition());

        // 4 зад
        System.out.println("\n 4. Игроки по позициям (пример вывода) ");
        resolver.getPlayersByPosition().forEach((position, names) -> {
            String shortNames = names.length() > 50 ? names.substring(0, 50) + "..." : names;
            System.out.println(position + ": " + shortNames);
        });

        // 5 зад
        System.out.println("\n5. Список всех команд ");
        System.out.println("Количество команд: " + resolver.getTeams().size());
        System.out.println("Команды: " + resolver.getTeams());

        // 6 зад
        System.out.println("\n 6. Топ-5 забивающих команд");
        resolver.getTop5TeamsByGoalsCount().forEach((team, goals) ->
                System.out.println("Команда " + team + ": " + goals + " голов")
        );

        // 7 зад
        System.out.println("\n 7. Агентство с минимумом игроков ");
        System.out.println("Ответ: " + resolver.getAgencyWithMinPlayersCount());

        // 8 зад
        System.out.println("\n 8. Самая грубая команда (по красным карточкам) ");
        System.out.println("Ответ: " + resolver.getTheRudestTeam());

        System.out.println("\n-------");
    }
}