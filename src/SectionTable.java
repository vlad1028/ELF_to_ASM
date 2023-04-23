final class SectionTable {
    final int TABLE_OFFSET;
    final int ENTRY_SIZE;
    final int ENTRIES_NUMBER;

    public final Section[] sections;

    public SectionTable(final DataSource source, int seqOffset, int entrySize, int entriesNumber) {
        this.TABLE_OFFSET = seqOffset;
        this.ENTRY_SIZE = entrySize;
        this.ENTRIES_NUMBER = entriesNumber;

        sections = new Section[entriesNumber];
        int begin = TABLE_OFFSET;
        for (int i = 0; i < entriesNumber; i++) {
            sections[i] = new Section(source, begin);
            begin += ENTRY_SIZE;
        }
    }
}