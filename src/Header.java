public class Header {
    Node e_entry;
    Node e_shoff;
    Node e_shentsize;
    Node e_shnum;
    Node e_shstrndx;

    public Header(final DataSource source) {
        e_entry = new Node(24, DataTypes.Elf32_Addr, source);
        e_shoff = new Node(32, DataTypes.Elf32_Off, source);
        e_shentsize = new Node(46, DataTypes.Elf32_Half, source);
        e_shnum = new Node(48, DataTypes.Elf32_Half, source);
        e_shstrndx = new Node(50, DataTypes.Elf32_Half, source);
    }
}
