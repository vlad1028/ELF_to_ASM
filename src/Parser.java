import java.util.HashMap;

public class Parser {
    private final int command;
    private final int address;
    private final OpCode opCode;
    private final HashMap<Integer, String> mark;

    public Parser(int command, int address, HashMap<Integer, String> mark) {
        this.command = command;
        this.address = address;
        this.mark = mark;
        this.opCode = OpCodeGenerator.get(getOpCode());
    }

    public String parse() {
        return parseOpCode();
    }

    private String parseOpCode() {
        if (opCode == null) {
            System.out.println(getOpCode());
            return "undefined";
        }
        switch (opCode) {
            case OP: return parseTypeR();
            case MISC_MEM, OP_IMM, JALR, LOAD: return parseTypeI();
            case STORE: return parseTypeS();
            case BRANCH: return parseTypeB();
            case LUI, AUIPC: return parseTypeU();
            case JAL: return parseTypeJ();
            case SYSTEM: return parseTypeSystem();
            default: return "";
        }
    }

    private String getMark(int imm) {
        String m = mark.get(imm);
        if (m == null) {
            m = "L" + mark.get(-1);
            mark.put(imm, m);
            mark.put(-1, Integer.toString(m.charAt(1) - '0' + 1));
        }
        return m;
    }

    private String parseTypeSystem() {
        String func = FuncRepresentation.getRepresentation(opCode, getBits(7, 32));
        return func;
    }

    private String parseTypeJ() {
        String func = FuncRepresentation.getRepresentation(opCode, 0);
        if (func.equals("unknown_instruction")) {
            return func;
        }
        String rd = registerRepresentation(getRd());
        int imm = getImmJ() + address;
        String m = getMark(imm);
        return String.format("%7s\t%s,%h <%s>", func, rd, imm, m);
    }

    private String parseTypeU() {
        String func = FuncRepresentation.getRepresentation(opCode, 0);
        if (func.equals("unknown_instruction")) {
            return func;
        }
        String rd = registerRepresentation(getRd());
        int imm = getImmU();
        return String.format("%7s\t%s,0x%h", func, rd, imm);
    }

    private String parseTypeB() { // BEQ-BGEU
        String func = FuncRepresentation.getRepresentation(opCode, getFunc3());
        if (func.equals("unknown_instruction")) {
            return func;
        }
        String r1 = registerRepresentation(getRs1());
        String r2 = registerRepresentation(getRs2());
        int imm = getImmB() + address;
        return ssh(func, r1, r2, imm);
    }

    private String ssh(String func, String r1, String r2, int imm) {
        String m = getMark(imm);
        return String.format("%7s\t%s,%s,%h <%s>", func, r1, r2, imm, m);
    }

    private String parseTypeS() { // SB-SW
        String func = FuncRepresentation.getRepresentation(opCode, getFunc3());
        if (func.equals("unknown_instruction")) {
            return func;
        }
        int imm = getImmS();
        String r1 = registerRepresentation(getRs1());
        String r2 = registerRepresentation(getRs2());
        return sds(func, r2, imm, r1);
    }

    private String parseTypeI() {
        String func = FuncRepresentation.getRepresentation(opCode, getFunc3());
        if (func.equals("unknown_instruction")) {
            return func;
        }
        int imm = getImmI();
        String r1 = registerRepresentation(getRs1());
        String rd = registerRepresentation(getRd());

        if (func.equals("slli") || func.equals("srli")) {
            return specialTypeI(func, rd, r1, imm);
        }

        switch (opCode) {
            case OP_IMM: return ssd(func, rd, r1, imm);
            case JALR, LOAD: return sds(func, rd, imm, r1);
        }
        return "unknown_instruction";
    }

    private String specialTypeI(String func, String rd, String r1, int imm) {
        int f7 = getFunc7();
        if (f7 > 0) {
            func = "srai";
        }
        imm = getBits(20, 25);
        return String.format("%7s\t%s,%s,0x%h", func, rd, r1, imm);
    }

    private String ssd(String func, String rd, String r1, int imm) {
        return String.format("%7s\t%s,%s,%d", func, rd, r1, imm);
    }
    private String sds(String func, String rd, int imm, String r1) {
        return String.format("%7s\t%s,%d(%s)", func, rd, imm, r1);
    }

    private String parseTypeR() {
        String func = FuncRepresentation.getRepresentation(opCode, (getFunc7() << 3) + getFunc3());
        if (func.equals("unknown_instruction")) {
            return func;
        }
        String rd = registerRepresentation(getRd());
        String r1 = registerRepresentation(getRs1());
        String r2 = registerRepresentation(getRs2());
        return String.format("%7s\t%s,%s,%s", func, rd, r1, r2);
    }

    private String registerRepresentation(int r) {
        if (r == 0) {
            return "zero";
        } else if (r == 1) {
            return "ra";
        } else if (r == 2) {
            return "sp";
        } else if (r == 3) {
            return "gp";
        } else if (r == 4) {
            return "tp";
        } else if (5 <= r && r <= 7) {
            return "t" + (r - 5);
        } else if (8 <= r && r <= 9) {
            return "s" + (r - 8);
        } else if (10 <= r && r <= 17) {
            return "a" + (r - 10);
        } else if (18 <= r && r <= 27) {
            return "s" + (r - 18 + 2);
        } else if (28 <= r && r <= 31) {
            return "t" + (r - 28 + 3);
        }
        return "undef";
    }

    private int getImmI() {
        int val = getBits(20, 31);
        val |= fillSign(11, 32);
        return val;
    }

    private int getImmS() {
        int val = getBits(7, 12) + (getBits(25, 31) << 5);
        val |= fillSign(11, 32);
        return val;
    }

    private int getImmB() {
        int val = (getBits(7, 8) << 11) + (getBits(25, 31) << 5) + (getBits(8, 12) << 1);
        val |= fillSign(12, 32);
        return val;
    }

    private int getImmU() {
        int val = getBits(12, 32);
        return val;
    }

    private int getImmJ() {
        int val = (getBits(12, 20) << 12) + (getBits(20, 21) << 11) + (getBits(21, 31) << 1);
        val |= fillSign(20, 32);
        return val;
    }

    private int fillSign(int start, int end) {
        int sign = getBits(31, 32);
        int val = 0;
        if (sign == 1) {
            for (int i = start; i < end; i++) {
                val |= (1 << i);
            }
        }
        return val;
    }

    private int getRs1() {
        return getBits(15, 20);
    }
    private int getRs2() {
        return getBits(20, 25);
    }
    private int getRd() {
        return getBits(7, 12);
    }
    private int getFunc3() {
        return getBits(12, 15);
    }
    private int getFunc7() {
        return getBits(25, 32);
    }

    private byte getOpCode() {
        return (byte) getBits(0, 7);
    }

    private int getBits(int start, int end) {
        int ans = 0;
        for (int i = start; i < end; i++) {
            if (((1 << i) & command) != 0) {
                ans += (1 << i);
            }
        }
        return ans >>> start;
    }
}
