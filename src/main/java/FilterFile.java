import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FilterFile {
    public static void main(String[] args) {
        String inputFile = "C:\\Users\\RICHARDSONLG20\\IdeaProjects\\mancala-ai\\src\\main\\java\\results.txt";
        String outputFile = "output2.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            String line = reader.readLine();
            boolean keep = false;
            ArrayList<String> old = new ArrayList<>();
            while (line != null) {
                old.add(line);
                if (line.matches(".*--- matchup H\\d+ vs H\\d+ ---.*")) {
                    keep = true;
                } else if (line.contains("Won")) {
                    keep = true;
                        for (int i = old.size()-4; i > old.size()-9; i--) {
                            String prevLine = old.get(i);
                            if (prevLine != null) {
                                writer.write(prevLine + "\n");
                            } else {
                                break;
                            }
                        }

                } else {
                    keep = false;
                }
                if (keep) {
                    writer.write(line + "\n");
                }
                line = reader.readLine();
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
