import java.util.HashMap;

public class fileELF {
    private final DataSource source;
    private final Header header;
    private final SectionTable sectionTable;
    public final SymTable symTable;
    private final StringTable shstrTable;

    public final StringTable strTable;

    private int dataOffset = -1;
    private final int virtualAddress;
    private final HashMap<Integer, String> marks;

    public fileELF(DataSource source) {
        this.source = source;
        this.header = new Header(source);
        this.sectionTable = new SectionTable(source, header.e_shoff.value, header.e_shentsize.value, header.e_shnum.value);
        this.shstrTable = new StringTable(source, sectionTable.sections[header.e_shstrndx.value].sh_offset.value);
        virtualAddress = header.e_entry.value;

        Section s = findSection(".strtab", 3);
        this.strTable = new StringTable(source, s.sh_offset.value);
        s = findSection(".symtab", 2);
        this.symTable = new SymTable(source, s.sh_offset.value, s.sh_size.value, s.sh_entsize.value);
        this.marks = symTable.getMarks(strTable);
        this.marks.put(-1, "0"); // next mark
    }

    public String parse() {
        int[] commands = getCommands();
        String[] result = new String[commands.length];
        for (int i = 0; i < commands.length; i++) {
            Parser parser = new Parser(commands[i], virtualAddress + i * 4, marks);
            result[i] = parser.parse();
        }

        StringBuilder sb = new StringBuilder(".text");
        for (int i = 0; i < result.length; i++) {
            int address = virtualAddress + i * 4;
            String m = marks.get(address);
            if (m != null) {
                sb.append("\n%08x   <%s>:\n".formatted(address, m));
            }
            sb.append("   %05x:\t%08x\t".formatted(address, commands[i]));
            sb.append(result[i] + "\n");
        }
        return sb.toString();
    }

    private int[] getCommands() {
        Section s = findSection(".text", 1);
        dataOffset = s.sh_offset.value;
        int commandsCnt = s.sh_size.value / 4;
        int[] commands = new int[commandsCnt];
        for (int i = 0; i < commandsCnt; i++) {
            commands[i] = source.getValue(dataOffset + i * 4, 4);
        }
        return commands;
    }

    private Section findSection(String name, int type) {
        for (Section s : sectionTable.sections) {
            if (s.sh_type.value == type && shstrTable.getString(s.sh_name.value).equals(name)) {
                return s;
            }
        }
        return null;
    }
}