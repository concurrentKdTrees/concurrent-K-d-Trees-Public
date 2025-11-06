import java.util.*;

public class SequentialKDTreeTest {
    public static void main(String[] args) {
        int dimensions = 2;
        SequentialKDTree tree = new SequentialKDTree(dimensions);

        // Insert points
        tree.insert(new int[]{10, 20});
        tree.insert(new int[]{5, 25});
        tree.insert(new int[]{15, 30});
        tree.insert(new int[]{30, 40});

        // Contains check
        System.out.println("Contains [10, 20]: " + tree.contains(new int[]{10, 20}));
        System.out.println("Contains [0, 0]: " + tree.contains(new int[]{0, 0}));

        // Delete a point
        tree.delete(new SequentialKDTree.Point(10, 20));
        System.out.println("After deletion, contains [10, 20]: " + tree.contains(new int[]{10, 20}));

        // Range query
        SequentialKDTree.Point min = new SequentialKDTree.Point(0, 0);
        SequentialKDTree.Point max = new SequentialKDTree.Point(20, 35);
        List<SequentialKDTree.Point> rangeResults = tree.rangeQuery(min, max);
        System.out.println("Range Query Results (0,0)-(20,35): " + rangeResults.size());

        // Nearest neighbor
        SequentialKDTree.Point query = new SequentialKDTree.Point(13, 29);
        SequentialKDTree.Point nearest = tree.nearestNeighbor(query);
        System.out.println("Nearest to [13, 29]: " + Arrays.toString(nearest.coords));

        // Cleanup and deletion count
        tree.cleanup();
        System.out.println("Physical deletions: " + tree.getPhysicalDeletionCount());
    }
}
