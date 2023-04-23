import java.util.HashMap;

public class SymTable {
    final int TABLE_OFFSET;
    final int ENTRY_SIZE;
    final int ENTRIES_NUMBER;

    public final Sym[] elements;

    public SymTable(final DataSource source, int offset, int size, int entrySize) {
        this.TABLE_OFFSET = offset;
        this.ENTRY_SIZE = entrySize;
        this.ENTRIES_NUMBER = size / entrySize;

        elements = new Sym[ENTRIES_NUMBER];
        int begin = TABLE_OFFSET;
        for (int i = 0; i < ENTRIES_NUMBER; i++) {
            elements[i] = new Sym(source, begin);
            begin += entrySize;
        }
    }

    public HashMap<Integer, String> getMarks(StringTable stringTable) {
        HashMap<Integer, String> map = new HashMap<>();
        for (Sym s : elements) {
            map.put(s.st_value.value, stringTable.getString(s.st_name.value));
        }
        return map;
    }


    public String print(StringTable stringTable) {
        StringBuilder sb = new StringBuilder(".symtable\n");
        sb.append("Symbol Value           \tSize Type \t\tBind \tVis   \t\tIndex Name\n");
        for (int i = 0; i < ENTRIES_NUMBER; i++) {
            Sym sym = elements[i];
            sb.append(String.format("[%4d] 0x%-15X %5d %-8s %-8s %-8s %6s %s\n",
                    i,
                    sym.st_value.value,
                    sym.st_size.value,
                    strType(sym),
                    strBind(sym),
                    strVis(sym),
                    strNdx(sym),
                    stringTable.getString(sym.st_name.value)
            ));
        }
        return sb.toString();
    }

    private String strType(Sym sym) {
        switch (sym.getType()) {
            case 0 -> {
                return "NOTYPE";
            }
            case 1 -> {
                return "OBJECT";
            }
            case 2 -> {
                return "FUNC";
            }
            case 3 -> {
                return "SECTION";
            }
            case 4 -> {
                return "FILE";
            }
            case 13 -> {
                return "LOPROC";
            }
            case 15 -> {
                return "HIPROC";
            }
            default -> {
                return "UNDEFINED";
            }
        }
    }

    private String strBind(Sym sym) {
        switch (sym.getBind()) {
            case 0: return "LOCAL";
            case 1: return "GLOBAL";
            case 2: return "WEAK";
            case 13: return "LOPROC";
            case 15: return "HIPROC";
            default: return "UNDEFINED";
        }
    }

    private String strVis(Sym sym) {
        switch ((sym.st_other.value)) {
            case 0: return "DEFAULT";
            default: return "UNDEFINED";
        }
    }

    private String strNdx(Sym sym) {
        switch (sym.st_shndx.value) {
            case 0: return "UND";
            case 0xff00: return "LOPROC";
            case 0xff1f: return "HIPROC";
            case 0xfff1: return "ABS";
            case 0xfff2: return "COMMON";
            case 0xffff: return "HIRESERVE";
            default: return String.valueOf(sym.st_shndx.value);
        }
    }
}
