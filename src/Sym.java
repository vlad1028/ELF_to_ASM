public class Sym {
    Node st_name;
    Node st_value;
    Node st_size;
    Node st_info;
    Node st_other;
    Node st_shndx;

    public Sym(DataSource source, int begin) {
        this.st_name = new Node(begin, DataTypes.Elf32_Word, source);
        this.st_value = new Node(st_name.begin + st_name.size(), DataTypes.Elf32_Addr, source);
        this.st_size = new Node(st_value.begin + st_value.size(), DataTypes.Elf32_Word, source);
        this.st_info = new Node(st_size.begin + st_size.size(), DataTypes.Elf32_Char, source);
        this.st_other = new Node(st_info.begin + st_info.size(), DataTypes.Elf32_Char, source);
        this.st_shndx = new Node(st_other.begin + st_other.size(), DataTypes.Elf32_Half, source);
    }

    public int getBind() {
        return (st_info.value >>> 4);
    }
    public int getType() {
        return (st_info.value & (0xf));
    }
}
