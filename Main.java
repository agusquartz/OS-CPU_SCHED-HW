import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main{
    public static LinkedList<BCP> BCPList;

    public static void main(String[] args){
        String path = "test.csv";
        FileManager fileManager = FileManager.getInstance();
        Parser parser = Parser.getInstance();
        Main.BCPList = parser.parse(fileManager.readFile(path));

        fileManager.checkOutput();
        View view = View.getInstance();

        /*
        FCFS fcfs = new FCFS();
        RR rr = new RR(4);
        SJFE sjfe = new SJFE();
        SJF sjf = new SJF();
        Gantt gantt = new Gantt(BCPList);
        Dispatcher dispatcher = new Dispatcher(BCPList, gantt);

        

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
        }*/



    }
    public static void executeSerial(List<View.EnumAlgo> normalEnumAlgo, int quantum){
        String algoName;
        // Process normal algorithms only if the list is not null and not empty
        if (normalEnumAlgo != null && !normalEnumAlgo.isEmpty()) {
            for (View.EnumAlgo algo : normalEnumAlgo) {
                switch (algo) {
                    case FCFS:
                        algoName = "FCFS";
                        startJob(Main.BCPList, algoName, new FCFS());
                        break;
                    case SJF_EXPULSIVO:
                        algoName = "SJF_Expulsivo";
                        startJob(Main.BCPList, algoName, new SJFE());
                        break;
                    case SJF_NO_EXPULSIVO:
                        algoName = "SJF_NoExpulsivo";
                        startJob(Main.BCPList, algoName, new SJF());
                        break;
                    case PRIORIDAD:
                        algoName = "Priority";
                        startJob(Main.BCPList, algoName, new Priority());
                        break;
                    case ROUND_ROBIN:
                        algoName = "RR (" + quantum + ")";
                        startJob(Main.BCPList, algoName, new RR(quantum));
                        break;
                    case HRRN:
                        algoName = "HRRN";
                        startJob(Main.BCPList, algoName, new HRRN());
                        break;
                    default:
                        // No action for NONE or unexpected value
                        break;
                }
            }
        }
    }


    public static void programLogic( List<View.EnumAlgo> normalEnumAlgo, View.EnumAlgo[] multiLevelEnumAlgos,int quantum, int[] quantumByLevel){
        executeSerial(normalEnumAlgo,quantum);
        executeMultiLevel(multiLevelEnumAlgos,quantumByLevel);
    }

    public static void executeMultiLevel(View.EnumAlgo[] multiLevelEnumAlgos, int[] quantumByLevel){
        // Process multi-level queue algorithms if the array is not null and not empty

        String algoName;
        LinkedList<BCP> filteredBCPs;
        View view = View.getInstance();

        for(View.EnumAlgo e : multiLevelEnumAlgos){
            System.out.println(e);
        }
        if (multiLevelEnumAlgos != null && multiLevelEnumAlgos.length > 0) {
            for (int i = 0; i < 3; i++) {
                filteredBCPs = filterListByPrio(Main.BCPList, i + 1);
                if (multiLevelEnumAlgos[i] == View.EnumAlgo.FCFS) {
                    //    filteredBCPs = filterListByPrio(Main.BCPList, i + 1);
                    algoName = "Queue LvL " + (i+1) + ": FCFS";
                    startJob(filteredBCPs, algoName, new FCFS());
                } else if (multiLevelEnumAlgos[i] == View.EnumAlgo.SJF_EXPULSIVO) {
                    //   filteredBCPs = filterListByPrio(Main.BCPList, i + 1);
                    algoName = "Queue LvL " + (i+1) + ": SJF_Expulsivo";
                    startJob(filteredBCPs, algoName, new SJFE());
                } else if (multiLevelEnumAlgos[i] == View.EnumAlgo.SJF_NO_EXPULSIVO) {
                    //filteredBCPs = filterListByPrio(Main.BCPList, i + 1);
                    algoName = "Queue LvL " + (i+1) + ": SJF_NoExpulsivo";
                    startJob(filteredBCPs, algoName, new SJF());
                } else if (multiLevelEnumAlgos[i] == View.EnumAlgo.PRIORIDAD) {
                    //filteredBCPs = filterListByPrio(Main.BCPList, i + 1);
                    algoName = "Queue LvL " + (i+1) + ": Priority";
                    startJob(filteredBCPs, algoName, new Priority());
                } else if (multiLevelEnumAlgos[i] == View.EnumAlgo.ROUND_ROBIN) {
                    //filteredBCPs = filterListByPrio(Main.BCPList, i + 1);
                    algoName = "Queue LvL " + (i+1) + ": RR (" + quantumByLevel[i] + ")";
                    startJob(filteredBCPs, algoName, new RR(quantumByLevel[i]));
                } else if (multiLevelEnumAlgos[i] == View.EnumAlgo.HRRN) {
                    //filteredBCPs = filterListByPrio(Main.BCPList, i + 1);
                    algoName = "Queue LvL " + (i+1) + ": HRRN";
                    startJob(filteredBCPs, algoName, new HRRN());
                } else {
                    // Do nothing for NONE or unexpected value
                }
            }

        }
    }

    public static void startJob(LinkedList<BCP> _BCPList, String algoName, Algorithm algo){
        Gantt gantt = new Gantt(_BCPList);
        Dispatcher dispatcher = new Dispatcher(_BCPList, gantt);
        FileManager fileManager = FileManager.getInstance();

        if (dispatcher.run(algo)){
            fileManager.appendToFile(gantt.toCSV(algoName));
        }
        resetAll(Main.BCPList);
    }

    private static LinkedList<BCP> filterListByPrio(LinkedList<BCP> bcps, int num){
        return bcps.stream().filter(b -> b.getPriority() == num).collect(Collectors.toCollection(LinkedList::new));
    }

    private static void resetAll(LinkedList<BCP> list){
        for(BCP b : list){
            b.reset();
        }
        return;
    }
}
