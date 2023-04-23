import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Too few program arguments. Expected 3, found " + args.length);
            System.out.println("Expected [executable program] [input file] [output file]");
        }
        String filePath = args[1];
        String outFile = args[2];
        DataSource source;
        try {
            source = new DataSource(filePath);
        } catch (IOException e) {
            System.out.println("Can't open file ('" + filePath + "')");
            return;
        }
        if (!checkELF(source)) {
            System.out.println("Wrong file ('" + filePath + "') format. Should be ELF");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outFile),
                "UTF-8"
        ))) {
            fileELF file = new fileELF(source);
            writer.write(file.parse());
            writer.write("\n");
            writer.write(file.symTable.print(file.strTable));
        } catch (FileNotFoundException e) {
            System.out.println("Can't open file '" + outFile + "'");
        } catch (IOException e) {
            System.out.println("Problems with writing to file '" + outFile + "'");
        }
    }

    private static boolean checkELF(final DataSource source) {
        StringBuilder sb = new StringBuilder();
        sb.append((char) source.getValue(1, 1));
        sb.append((char) source.getValue(2, 1));
        sb.append((char) source.getValue(3, 1));
        return sb.toString().equals("ELF");
    }
}
