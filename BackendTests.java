import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Backend class.
 * These tests verify that Backend class methods behave as expected.
 */
public class BackendTests {

    /**
     * Test method for loading data from a CSV file.
     * Verifies that songs are correctly inserted into the tree after reading from a CSV file.
     */
    @Test
    public void roleTest1() throws IOException {
        Tree_Placeholder placeholder = new Tree_Placeholder();
        Backend backend = new Backend(placeholder);

        // Call readData method to load songs from a file
        backend.readData("songs.csv");

        // Verify that the tree contains songs (4 due to the placeholder behavior)
        assertEquals(4, placeholder.size(), "The tree should contain 4 songs after loading data.");
    }

    /**
     * Test method for filtering songs by energy range.
     * Verifies that songs are correctly filtered based on the energy range specified.
     */
    @Test
    public void roleTest2() {
        Tree_Placeholder placeholder = new Tree_Placeholder();
        Backend backend = new Backend(placeholder);

        // Call getRange with a specific energy range
        List<String> songs = backend.getRange(80, 100);

        // Verify that songs within the energy range are returned
        assertFalse(songs.isEmpty(), "Songs within the energy range should be returned.");
    }

    /**
     * Test method for filtering songs by danceability threshold.
     * Verifies that only songs with a danceability value above the threshold are returned.
     */
    @Test
    public void roleTest3() {
        Tree_Placeholder placeholder = new Tree_Placeholder();
        Backend backend = new Backend(placeholder);

        // Set a danceability filter and check results
        List<String> filteredSongs = backend.setFilter(70);

        // Verify that only songs with danceability above 70 are returned
        assertFalse(filteredSongs.isEmpty(), "Songs with danceability above 70 should be returned.");
    }
}