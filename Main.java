
public class Main{
    public static void main(String[] args){
        String path = "test.csv";
        FileManager fileManager = FileManager.getInstance();
        Parser parser = Parser.getInstance();
        Process[] processes = parser.parse(fileManager.readFile(path));
        FCFS fcfs = new FCFS();
        Gantt g = new Gantt(processes);

        FileManager.checkOutput();
        FileManager.appendToFile(g.generate(fcfs).toCSV();)
    }
}