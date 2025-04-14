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

        Gantt gantt2 = new Gantt(BCPList);
        Dispatcher dispatcher2 = new Dispatcher(BCPList, gantt2);
        if (dispatcher2.run(sjf)){
            fileManager.appendToFile(gantt2.toCSV("SJF"));
        }
        resetAll(BCPList);

        Gantt gantt3 = new Gantt(BCPList);
        Dispatcher dispatcher3 = new Dispatcher(BCPList, gantt3);
        if (dispatcher3.run(sjfe)){
            fileManager.appendToFile(gantt3.toCSV("SJFE"));
        }
        resetAll(BCPList);

        Gantt gantt4 = new Gantt(BCPList);
        Dispatcher dispatcher4 = new Dispatcher(BCPList, gantt4);
        if (dispatcher4.run(rr)){
            fileManager.appendToFile(gantt4.toCSV("RR"));
        }



    }
    private static void resetAll(LinkedList<BCP> list){
        for(BCP b : list){
            b.reset();
        }
        return;
    }
}
