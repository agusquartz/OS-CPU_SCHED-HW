import java.util.LinkedList;

public class Main{
    public static void main(String[] args){
        String path = "test.csv";
        FileManager fileManager = FileManager.getInstance();
        Parser parser = Parser.getInstance();
        LinkedList<BCP> BCPList = parser.parse(fileManager.readFile(path));

        FCFS fcfs = new FCFS();
        RR rr = new RR(4);
        SJFE sjfe = new SJFE();
        SJF sjf = new SJF();
        Gantt gantt = new Gantt(BCPList);
        Dispatcher dispatcher = new Dispatcher(BCPList, gantt);

        fileManager.checkOutput();

        // Logic of filtering and Execution of algorithms
        if (dispatcher.run(fcfs)){
            fileManager.appendToFile(gantt.toCSV("FCFS"));
        }
        resetAll(BCPList);
        if (dispatcher.run(rr)){
            fileManager.appendToFile(gantt.toCSV("RR"));
        }
        resetAll(BCPList);
        if (dispatcher.run(sjfe)){
            fileManager.appendToFile(gantt.toCSV("SJFE"));
        }
        resetAll(BCPList);
        if (dispatcher.run(sjf)){
            fileManager.appendToFile(gantt.toCSV("SJF"));
        }



    }
    private static void resetAll(LinkedList<BCP> list){
        for(BCP b : list){
            b.reset();
        }
        return;
    }
}
