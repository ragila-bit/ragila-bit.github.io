import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Backend class implementing the BackendInterface.
 * This class loads songs from a CSV file and stores them in a tree structure.
 * It supports filtering songs based on energy and danceability, and retrieving the
 * most recent five songs based on these filters.
 */
public class Backend implements BackendInterface {

    // Tree structure to store and manage songs
    private IterableSortedCollection<Song> tree;
    // Filter boundaries
    private Integer lowEnergyBound = null;
    private Integer highEnergyBound = null;
    private Integer danceabilityThreshold = null;

    /**
     * Constructor to initialize the Backend with a tree structure.
     *
     * @param tree The tree structure that will store songs
     */
    public Backend(IterableSortedCollection<Song> tree) {
        this.tree = tree;
    }

    /**
     * Reads song data from a CSV file and inserts the data into the tree.
     *
     * @param filename the name of the CSV file to load data from
     * @throws IOException if there is an issue reading the file
     */
    @Override
    public void readData(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9) {
                    // Parse song fields from CSV
                    String title = data[0];
                    String artist = data[1];
                    String genre = data[2];
                    int year = Integer.parseInt(data[3]);
                    int bpm = Integer.parseInt(data[4]);
                    int energy = Integer.parseInt(data[5]);
                    int danceability = Integer.parseInt(data[6]);
                    int loudness = Integer.parseInt(data[7]);
                    int liveness = Integer.parseInt(data[8]);

                    // Create Song object and insert into the tree
                    Song song = new Song(title, artist, genre, year, bpm, energy, danceability, loudness, liveness);
                    tree.insert(song);
                }
            }
        }
    }

    /**
     * Retrieves a list of song titles that fall within the specified range of energy values.
     *
     * @param low  the minimum energy value (inclusive) or null for no lower bound
     * @param high the maximum energy value (inclusive) or null for no upper bound
     * @return a list of song titles ordered by energy within the specified range
     */
    @Override
    public List<String> getRange(Integer low, Integer high) {
        List<String> result = new ArrayList<>();
        this.lowEnergyBound = low;
        this.highEnergyBound = high;

        // Set iterator bounds in the tree
        tree.setIteratorMin(low == null ? null : new Song("", "", "", 0, 0, low, 0, 0, 0));
        tree.setIteratorMax(high == null ? null : new Song("", "", "", 0, 0, high, 0, 0, 0));

        // Iterate over songs and filter based on danceability
        for (Song song : tree) {
            if (danceabilityThreshold == null || song.getDanceability() > danceabilityThreshold) {
                result.add(song.getTitle());
            }
        }
        return result;
    }

    /**
     * Sets a filter to only include songs with danceability above the specified threshold.
     *
     * @param threshold the minimum danceability value
     * @return a list of song titles that pass the filter and energy range
     */
    @Override
    public List<String> setFilter(Integer threshold) {
        this.danceabilityThreshold = threshold;
        return getRange(lowEnergyBound, highEnergyBound);
    }

    /**
     * Retrieves a list of the five most recent songs that match the energy range and filter.
     *
     * @return a list of the five most recent song titles
     */
    @Override
    public List<String> fiveMost() {
        List<String> result = new ArrayList<>();
        int count = 0;

        // Iterate through the songs and select those that match the filters
        for (Song song : tree) {
            if ((danceabilityThreshold == null || song.getDanceability() > danceabilityThreshold) &&
                (lowEnergyBound == null || song.getEnergy() >= lowEnergyBound) &&
                (highEnergyBound == null || song.getEnergy() <= highEnergyBound)) {
                
                result.add(song.getTitle());
                count++;
                if (count == 5) break; // Only include the most recent five
            }
        }
        return result;
    }
}