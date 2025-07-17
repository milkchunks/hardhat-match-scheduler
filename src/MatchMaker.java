//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class MatchMaker {
    public static void main(String[] args) {
        User_Interface.start();
    }

    public static void callCSV(String[][] data, String name) {
        CSVWriter.exportCSV(data, name);
    }

    public static String[] pollUISettings() {
        return User_Interface.pollSettings();
    }

    public static void statusUPD(String msg) {
        User_Interface.statusUpdate(msg);
    }
}
