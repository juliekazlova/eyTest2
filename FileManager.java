import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final FileManager instance = new FileManager();
    private static List<String> files=new ArrayList<>();
    private static String mergeFile;

    private FileManager() {
    }

    public static FileManager getInstance() {
        return instance;
    }

    public static List<String> getFiles() {
        return files;
    }

    public static String getMergeFile() {
        return mergeFile;
    }

    public void generateTxt(int stringNumber, int fileNumber){
        DataGenerator dataGenerator=DataGenerator.getInstance();
        for(int i=0; i<fileNumber; i++){
            try(FileWriter writer = new FileWriter("dataFile"+i+".txt"))
            {
                for(int j=0; j<stringNumber; j++){
                    writer.write(dataGenerator.getGeneratedString());
                    writer.append('\n');
                }
                files.add("dataFile"+i+".txt");//record name of file
                writer.flush();
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }
        }
    }

    public int editMerge(String regex){
        int editCount=0;
        if(regex==null||regex.length()==0) {
            merge();
            return 0;
        }
        try(FileWriter writer = new FileWriter("dataFile"+files.size()+".txt")) {

            for (String file : files) {
                int symbol;
                BufferedReader reader=new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    String newLine=line.replaceAll(regex, "");
                    editCount+=(line.length()-newLine.length())/regex.length();
                    newLine+="\n";
                   writer.write(newLine);
                    // считываем остальные строки в цикле
                    line = reader.readLine();
                }
            }

            mergeFile="dataFile"+files.size()+".txt";
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        return editCount;
    }

    public void merge(){
        try(FileWriter writer = new FileWriter("dataFile"+files.size()+".txt")) {

            for (String file : files) {
                int symbol;
                BufferedReader reader=new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    line+="\n";
                    writer.write(line);
                    // считываем остальные строки в цикле
                    line = reader.readLine();
                }
            }

            files.add("dataFile"+files.size()+".txt");
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public int countFileLines(String file){
        int lineNumber=0;
        try{

            File myFile =new File(file);
            FileReader fileReader = new FileReader(myFile);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
            while (lineNumberReader.readLine() != null){
                lineNumber++;
            }
            lineNumberReader.close();

        }catch(IOException e){
            e.printStackTrace();
        }
        return lineNumber;
    }

}
