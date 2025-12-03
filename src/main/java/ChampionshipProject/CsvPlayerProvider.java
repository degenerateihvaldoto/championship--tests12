package ChampionshipProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvPlayerProvider implements IPlayerProvider {
    private final String filePath;

    public CsvPlayerProvider(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Player> getPlayers() {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines
                    .skip(1)
                    .map(this::mapLineToPlayer)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }

    private Player mapLineToPlayer(String line) {
        String[] fields = line.split(";");
        String agency = fields.length > 5 && !fields[5].trim().isEmpty() ? fields[5] : null;
        return new Player(
                fields[0], fields[1], fields[2], fields[3], fields[4], agency,
                Long.parseLong(fields[6]), Integer.parseInt(fields[7]),
                Integer.parseInt(fields[8]), Integer.parseInt(fields[9]),
                Integer.parseInt(fields[10]), Integer.parseInt(fields[11])
        );
    }
}