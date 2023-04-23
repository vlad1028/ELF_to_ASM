import java.util.HashMap;
import java.util.Map;

public class FuncRepresentation {

    private static Map<OpCode, Map<Integer, String>> functions;

    static {
        functions = new HashMap<>(Map.of(
                OpCode.JAL,
                Map.of(0, "jal"),
                OpCode.LUI,
                Map.of(0, "lui"),
                OpCode.AUIPC,
                Map.of(0, "auipc"),
                OpCode.JALR,
                Map.of(0, "jalr"),
                OpCode.MISC_MEM,
                Map.of(0, "fence")
        ));
        functions.put(OpCode.OP, new HashMap<>(Map.of(
                toInt("0000000" + "000"),
                "add",
                toInt("0100000" + "000"),
                "sub",
                toInt("0000000" + "001"),
                "sll",
                toInt("0000000" + "010"),
                "slt",
                toInt("0000000" + "011"),
                "sltu",
                toInt("0000000" + "100"),
                "xor",
                toInt("0000000" + "101"),
                "srl",
                toInt("0100000" + "101"),
                "sra",
                toInt("0000000" + "110"),
                "or",
                toInt("0000000" + "111"),
                "and"
        )));
        functions.get(OpCode.OP).put(toInt("0000001" + "000"), "mul");
        functions.get(OpCode.OP).put(toInt("0000001" + "001"), "mulh");
        functions.get(OpCode.OP).put(toInt("0000001" + "010"), "mulhsu");
        functions.get(OpCode.OP).put(toInt("0000001" + "011"), "mulhu");
        functions.get(OpCode.OP).put(toInt("0000001" + "100"), "div");
        functions.get(OpCode.OP).put(toInt("0000001" + "101"), "divu");
        functions.get(OpCode.OP).put(toInt("0000001" + "110"), "rem");
        functions.get(OpCode.OP).put(toInt("0000001" + "111"), "remu");

        functions.put(OpCode.OP_IMM, Map.of(
                toInt("001"),
                "slli",
                toInt("101"),
                "srli",
                toInt("0100000" + "101"),
                "srai",

                toInt("000"),
                "addi",
                toInt("010"),
                "slti",
                toInt("011"),
                "sltiu",
                toInt("100"),
                "xori",
                toInt("110"),
                "ori",
                toInt("111"),
                "andi"
        ));
        functions.put(OpCode.BRANCH, Map.of(
                0,
                "beq",
                1,
                "bne",
                4,
                "blt",
                5,
                "bge",
                6,
                "bltu",
                7,
                "bgeu"
        ));
        functions.put(OpCode.LOAD, Map.of(
                toInt("000"),
                "lb",
                toInt("001"),
                "lh",
                toInt("010"),
                "lw",
                toInt("100"),
                "lbu",
                toInt("101"),
                "lhu"
        ));
        functions.put(OpCode.STORE, Map.of(
                toInt("000"),
                "sb",
                toInt("001"),
                "sh",
                toInt("010"),
                "sw"
        ));
        functions.put(OpCode.SYSTEM, Map.of(
                toInt("000000000000" + "00000" + "000" + "00000"),
                "ecall",
                toInt("000000000001" + "00000" + "000" + "00000"),
                "ebreak"
        ));
    }

    private FuncRepresentation() {}

    private static int toInt(String binary) {
        return Integer.parseInt(binary, 2);
    }

    public static String getRepresentation(OpCode code, String func) {
        Map<Integer, String> map = functions.get(code);
        if (map != null) {
            String ans = map.get(toInt(func));
            if (ans != null) {
                return ans;
            }
        }
        return "unknown_instruction";
    }
    public static String getRepresentation(OpCode code, int func) {
        Map<Integer, String> map = functions.get(code);
        if (map != null) {
            String ans = map.get(func);
            if (ans != null) {
                return ans;
            }
        }
        return "unknown_instruction";
    }
}
