//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class MatchMaker {
    private static User_Interface ui = new User_Interface();
    private CSVWriter writer = new CSVWriter();

    public static void main(String[] args) {
    }

    public static void callCSV(String[][] data, String name) {
        CSVWriter.CSVExporter(data, name);
    }

    public static String[] pollUISettings() {
        return ui.pollSettings();
    }

    public static void statusUPD(String msg) {
        ui.statusUpdate(msg);
    }
}
