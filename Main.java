
public class Main{
    public static void main(String[] args){
        String path = "test.csv";
        FileManager fileManager = FileManager.getInstance();
        Parser parser = Parser.getInstance();


        Process[] processes = parser.parse(fileManager.readFile(path));

        for(Process proc : processes){
            System.out.println(proc.toString());
        }
    }
}