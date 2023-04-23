import java.util.HashMap;
import java.util.Map;

public class OpCodeGenerator {
    private OpCodeGenerator() {}
    private static Map<Byte, OpCode> getByValue;

    static {
        getByValue = new HashMap<>(Map.of(
                getValue("0110111"),
                OpCode.LUI,
                getValue("0010111"),
                OpCode.AUIPC,
                getValue("1101111"),
                OpCode.JAL,
                getValue("1100111"),
                OpCode.JALR,
                getValue("0000011"),
                OpCode.LOAD,
                getValue("0100011"),
                OpCode.STORE,
                getValue("0010011"),
                OpCode.OP_IMM,
                getValue("0110011"),
                OpCode.OP,
                getValue("0001111"),
                OpCode.MISC_MEM,
                getValue("1110011"),
                OpCode.SYSTEM
        ));
        getByValue.put(getValue("1100011"), OpCode.BRANCH);
    }

    private static Byte getValue(String binary) {
        return Byte.parseByte(binary, 2);
    }

    public static OpCode get(byte value) {
        return getByValue.get(value);
    }

    public static OpCode get(String binary) {
        return getByValue.get(getValue(binary));
    }
}
