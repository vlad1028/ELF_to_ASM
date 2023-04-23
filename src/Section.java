public class Section {
    Node sh_name;
    Node sh_type;
    Node sh_addr;
    Node sh_offset;
    Node sh_size;
    Node sh_entsize;

    public Section(DataSource source, int begin) {
        this.sh_name = new Node(begin, DataTypes.Elf32_Word, source);
        this.sh_type = new Node(sh_name.begin + sh_name.size(), DataTypes.Elf32_Word, source);
        this.sh_addr = new Node(sh_type.begin + sh_type.size() + 4, DataTypes.Elf32_Addr, source);
        this.sh_offset = new Node(sh_addr.begin + sh_addr.size(), DataTypes.Elf32_Off, source);
        this.sh_size = new Node(sh_offset.begin + sh_offset.size(), DataTypes.Elf32_Word, source);
        this.sh_entsize = new Node(sh_size.begin + sh_size.size() + 3 * 4, DataTypes.Elf32_Word, source);
    }
}
