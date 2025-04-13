import java.util.LinkedList;

public class Main{
    public static void main(String[] args){
        String path = "test.csv";
        FileManager fileManager = FileManager.getInstance();
        Parser parser = Parser.getInstance();
        LinkedList<BCP> BCPList = parser.parse(fileManager.readFile(path));

        FCFS fcfs = new FCFS();
        Gantt gantt = new Gantt(BCPList);
        Dispatcher dispatcher = new Dispatcher(BCPList, gantt);

        fileManager.checkOutput();

        // Logic of filtering and Execution of algorithms
        if (dispatcher.run(fcfs)){
            fileManager.appendToFile(gantt.toCSV("FCFS"));
        }

    }
}