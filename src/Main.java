
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import core.*;
import scheduling.*;
import mm.*;

public class Main{
    public static LinkedList<BCP> BCPList;

    public static void main(String[] args){
        String path = "test.csv";
        FileManager fileManager = FileManager.getInstance();
        Parser parser = Parser.getInstance();
        Main.BCPList = parser.parse(fileManager.readFile(path));

        fileManager.checkOutput();
        ViewPaging view = ViewPaging.getInstance(Main.BCPList);

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
        //executeMultiLevel(multiLevelEnumAlgos,quantumByLevel);
    }

    public static void executeMultiLevel(View.EnumAlgo[] multiLevelEnumAlgos, int[] quantumByLevel){
        // Process multi-level queue algorithms if the array is not null and not empty

        String algoName;
        LinkedList<BCP> filteredBCPs;

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

    public CPU initializeSystemComponents(List<BCP> bcpList, PageAlgorithm pageAlgorithm) {
        // Initialize the system components
        Memory ram = new Memory(20);
        Memory virtMem = new Memory(200);
        PageTable pageTable = new PageTable();
        PFH pageFaultHandler = new PFH();
        LogManager logManager = new LogManager();

        int virtMemInitialIndex = 0;
        for (BCP bcp : bcpList) {
            int framesNeeded = bcp.getProcess().getPages();

            // Initialize all pages in virtual memory
            for (int i = 0; i < framesNeeded; i++) {
                pageTable.updateBit(bcp.getId(), (i + 1), 0); // Page initially in virtual memory
                Page page = new Page(i + 1, bcp.getId());
                virtMem.putPage(page, virtMemInitialIndex);
                virtMemInitialIndex++;
            }

            // Load up to 3 random pages into RAM initially
            int initialPagesInRam = 3;
            int ramInitialIndex = 0;
            Set<Integer> loadedPages = new HashSet<>();

            while (loadedPages.size() < initialPagesInRam && loadedPages.size() < framesNeeded) {
                int randomPage = (int) (Math.random() * framesNeeded) + 1;

                if (!loadedPages.contains(randomPage)) {
                    loadedPages.add(randomPage);

                    pageTable.updateBit(bcp.getId(), randomPage, 1); // Mark page as in RAM

                    Page page = virtMem.removePage(bcp.getId(), randomPage);
                    ram.putPage(page, ramInitialIndex);
                    ramInitialIndex++;
                }
            }
        }

        // Create and return the CPU with initialized components
        return new CPU(ram, virtMem, pageFaultHandler, pageTable, pageAlgorithm, logManager);
    }


    public static void startJob(LinkedList<BCP> _BCPList, String algoName, Algorithm algo){
        Gantt gantt = new Gantt(_BCPList);
        FileManager fileManager = FileManager.getInstance();

        CPU core1 = initializeSystemComponents(_BCPList);
        CPU core2 = initializeSystemComponents(_BCPList);

        Dispatcher dispatcher = new Dispatcher(_BCPList, gantt, core1, core2);

        if (dispatcher.run(algo)){
            fileManager.appendToFile(gantt.toCSV(algoName));
            fileManager.appendToFile(core1.getLogManager().toCSV("FIFO 2nd Change"));
            fileManager.appendToFile(core2.getLogManager().toCSV("LRU"));
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
