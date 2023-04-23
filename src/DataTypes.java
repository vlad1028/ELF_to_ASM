public enum DataTypes {

    Elf32_Addr(4),
    Elf32_Half(2),
    Elf32_Off(4),
    Elf32_Sword(4),
    Elf32_Word(4),
    Elf32_Char(1);

    private int sizeInBytes;

    DataTypes(int size) {
        this.sizeInBytes = size;
    }

    public int getSize() {
        return sizeInBytes;
    }
}
