import java.util.*;

public class SequentialKDTree {
    final int k; // Number of dimensions
    final Map<Long, Node> shardRoots = new HashMap<>();
    public int physicalDeletions = 0;

    public SequentialKDTree(int dimensions) {
        this.k = dimensions;
    }

    public static class Point {
        final int[] coords;

        public Point(int... coords) {
            this.coords = coords;
        }

        public int get(int index) {
            return coords[index];
        }

        public double distanceTo(Point other) {
            double dist = 0;
            for (int i = 0; i < coords.length; i++) {
                int d = coords[i] - other.coords[i];
                dist += d * d;
            }
            return Math.sqrt(dist);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Point && Arrays.equals(coords, ((Point) o).coords);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(coords);
        }
    }

    static class Node {
        final Point point;
        final int depth;
        Node left = null, right = null;
        boolean isDeleted = false;

        Node(Point point, int depth) {
            this.point = point;
            this.depth = depth;
        }
    }

    private long shardKey(Point p) {
        int a = (k > 0) ? (p.get(0) >>> 8) : 0;
        int b = (k > 1) ? (p.get(1) >>> 8) : 0;
        int c = (k > 2) ? (p.get(2) >>> 8) : 0;
        return ((long) a << 16) ^ ((long) b << 8) ^ c;
    }

    public void insert(Point point) {
        if (point.coords.length != k)
            throw new IllegalArgumentException("Point dimensionality mismatch");

        long key = shardKey(point);
        Node current = shardRoots.get(key);

        if (current == null) {
            shardRoots.put(key, new Node(point, 0));
            return;
        }

        insertIterative(current, point);
    }

    private void insertIterative(Node current, Point point) {
        while (true) {
            int cd = current.depth % k;

            if (current.point.equals(point)) {
                current.isDeleted = false; // revive if previously deleted
                return;
            }

            if (point.get(cd) < current.point.get(cd)) {
                if (current.left == null) {
                    current.left = new Node(point, current.depth + 1);
                    return;
                } else {
                    current = current.left;
                }
            } else {
                if (current.right == null) {
                    current.right = new Node(point, current.depth + 1);
                    return;
                } else {
                    current = current.right;
                }
            }
        }
    }

    public boolean contains(Point point) {
        if (point.coords.length != k)
            throw new IllegalArgumentException("Point dimensionality mismatch");

        Node current = shardRoots.get(shardKey(point));
        return current != null && containsRecursive(current, point);
    }

    private boolean containsRecursive(Node node, Point point) {
        while (node != null) {
            if (!node.isDeleted && node.point.equals(point)) return true;
            int cd = node.depth % k;
            node = point.get(cd) < node.point.get(cd) ? node.left : node.right;
        }
        return false;
    }

    public void delete(Point point) {
        if (point.coords.length != k)
            throw new IllegalArgumentException("Point dimensionality mismatch");

        Node current = shardRoots.get(shardKey(point));
        if (current != null) deleteRecursive(current, point);
    }

    private void deleteRecursive(Node node, Point point) {
        while (node != null) {
            if (node.point.equals(point)) {
                node.isDeleted = true;
                return;
            }
            int cd = node.depth % k;
            node = point.get(cd) < node.point.get(cd) ? node.left : node.right;
        }
    }

    public void cleanup() {
        for (Map.Entry<Long, Node> entry : shardRoots.entrySet()) {
            Node cleaned = cleanRecursive(null, entry.getValue());
            if (cleaned != null) {
                shardRoots.put(entry.getKey(), cleaned);
            }
        }
    }

    private Node cleanRecursive(Node parent, Node node) {
        if (node == null) return null;

        node.left = cleanRecursive(node, node.left);
        node.right = cleanRecursive(node, node.right);

        if (node.isDeleted) {
            Node replacement = (node.left != null && node.right == null) ? node.left
                                : (node.right != null && node.left == null) ? node.right
                                : null;
            if (replacement != null || (node.left == null && node.right == null)) {
                physicalDeletions++;
                return replacement;
            }
        }

        return node;
    }

    public Point nearestNeighbor(Point target) {
        if (target.coords.length != k)
            throw new IllegalArgumentException("Target dimensionality mismatch");

        Point best = null;
        double bestDist = Double.MAX_VALUE;

        for (Node root : shardRoots.values()) {
            best = nearestRecursive(root, target, best, bestDist);
            bestDist = (best != null) ? best.distanceTo(target) : bestDist;
        }
        return best;
    }

    private Point nearestRecursive(Node node, Point target, Point best, double bestDist) {
        if (node == null) return best;

        if (!node.isDeleted) {
            double d = node.point.distanceTo(target);
            if (d < bestDist) {
                best = node.point;
                bestDist = d;
            }
        }

        int cd = node.depth % k;
        Node near = target.get(cd) < node.point.get(cd) ? node.left : node.right;
        Node far = target.get(cd) < node.point.get(cd) ? node.right : node.left;

        best = nearestRecursive(near, target, best, bestDist);
        bestDist = (best != null) ? best.distanceTo(target) : bestDist;

        if (Math.abs(target.get(cd) - node.point.get(cd)) < bestDist) {
            best = nearestRecursive(far, target, best, bestDist);
        }
        return best;
    }

    public List<Point> rangeQuery(Point min, Point max) {
        List<Point> result = new ArrayList<>();
        for (Node root : shardRoots.values()) {
            rangeRecursive(root, min, max, result);
        }
        return result;
    }

    private void rangeRecursive(Node node, Point min, Point max, List<Point> out) {
        if (node == null) return;

        boolean in = true;
        for (int i = 0; i < k; i++) {
            if (node.point.get(i) < min.get(i) || node.point.get(i) > max.get(i)) {
                in = false;
                break;
            }
        }

        if (in && !node.isDeleted) {
            out.add(node.point);
        }

        rangeRecursive(node.left, min, max, out);
        rangeRecursive(node.right, min, max, out);
    }

    public void insert(int[] coords) {
        insert(new Point(coords));
    }

    public boolean contains(int[] coords) {
        return contains(new Point(coords));
    }

    public Point generateRandomPoint(int range) {
        Random rand = new Random();
        int[] coords = new int[k];
        for (int i = 0; i < k; i++) coords[i] = rand.nextInt(range);
        return new Point(coords);
    }

    public int getPhysicalDeletionCount() {
        return physicalDeletions;
    }
}
