final class StringTable {
    final int STRING_TABLE_INDEX;
    private static final char ZERO = '\0';
    private final DataSource source;

    public StringTable(DataSource source, int begin) {
        this.source = source;
        this.STRING_TABLE_INDEX = begin;
    }

    public String getString(int begin) {
        StringBuilder sb = new StringBuilder();
        char c = (char) source.getValue(STRING_TABLE_INDEX + begin, 1);
        int offset = 0;
        while (c != ZERO) {
            sb.append(c);
            offset++;
            c = (char) source.getValue(STRING_TABLE_INDEX + begin + offset, 1);
        }
        return sb.toString();
    }
}