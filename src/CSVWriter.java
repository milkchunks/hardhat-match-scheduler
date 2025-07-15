//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    public static void CSVExporter(String[][] data, String name) {
        String CSV_DIRECTORY = "C:\\match scheduler csvs\\";
        File dir = new File(CSV_DIRECTORY);
        boolean failed = dir.mkdir();
        if (failed) {
            User_Interface.setNotifText("failed to make CSV directory in CSVWriter.java. maybe it already exists?");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dir.getAbsolutePath() + "\\" + name))) {
            for(int i = 0; i < data.length; ++i) {
                for(int j = 0; j < data[i].length; ++j) {
                    bw.append(data[i][j]);
                    if (j < data[i].length - 1) {
                        bw.append(",");
                    }
                }

                if (i < data.length - 1) {
                    bw.append("\n");
                }
            }
        } catch (IOException e) {
            User_Interface.setNotifText("error exporting " + name + ": " + e.getMessage());
        }

    }
}
