public class Node {
    public int value;
    public final int begin;
    private final int sizeInBytes;
    public Node(int begin, int sizeInBytes, int value) {
        this.begin = begin;
        this.sizeInBytes = sizeInBytes;
        this.value = value;
    }

    public Node(int begin, DataTypes type, final DataSource valueSource) {
        this.begin = begin;
        this.sizeInBytes = type.getSize();
        this.value = valueSource.getValue(begin, sizeInBytes);
    }

    public int size() {
        return sizeInBytes;
    }
}
