
import com.mysql.cj.jdbc.NonRegisteringDriver;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Driver {
    private Connection connection;
    private java.sql.Driver driver;
    private Statement statement;
    private PreparedStatement pstmt = null;
    private ResultSet res = null;
    private String sql;
    private FileManager fileManager=FileManager.getInstance();

    private static String StudentsTable = Student.class.getSimpleName();

    private static final String WRITE_DATA =
            "INSERT INTO task1 (date, latin, rus, numb, fraction) VALUES(";
    private static final String GET_NUMB_SUM="select SUM(numb) from ey.task1;";
    private static final String GET_STUDENTS_COUNT = MessageFormat.format(
            "SELECT COUNT(id) as size FROM students", StudentsTable);


    public Driver() {
        connect();
    }


    private void connect() {
        try {

            driver = new NonRegisteringDriver();
            DriverManager.registerDriver(driver);
            System.out.println("[dbDriver] Connecting to database...");
            connection = DriverManager.getConnection(Options.DB_URL, Options.DB_USER, Options.DB_PASS);
            statement = connection.createStatement();
           //statement.executeQuery("truncate table task1;");

        } catch (Exception se) {
            System.out.println("[dbDriver] Some problem with test.db connection");
            se.printStackTrace();
            System.exit(1);
        }
    }

    private void writeFromFile(String file){
        try{
        BufferedReader reader=new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            int linesImported=1;
            int linesAll=fileManager.countFileLines(file);
            while (line != null) {
                if(linesImported%10==0){
                    System.out.println(linesImported+" lines imported, "+(linesAll-linesImported)+
                            " lines left");
                }
                String[] data=line.split("-");
                StringBuilder sb=new StringBuilder(
                        "INSERT INTO ey.task1 (date, latin, rus, numb, fraction) VALUES('");
                for(int i=0; i<4; i++){
                    sb.append(data[i]).append("', '");//date
                }
                sb.append(data[4]).append("');");
                pstmt=connection.prepareStatement(sb.toString());
                pstmt.execute();
                linesImported++;
               // statement.executeQuery(sb.toString());
                // считываем остальные строки в цикле
                line = reader.readLine();
            }
        } catch ( IOException|SQLException e){
            e.printStackTrace();
        }
    }

    private String getNumbSum(){
        String sum="not available.";
        try {
            res=statement.executeQuery(GET_NUMB_SUM);
            while (res.next()) {
                sum=res.getString(1);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return sum;
    }




    private void close() {
        try {
            if (statement != null)
                statement.close();
        } catch (SQLException se2) {
            System.out.println(se2.getMessage());
        }

        try {
            if (pstmt != null)
                pstmt.close();
        } catch (SQLException se2) {
            System.out.println(se2.getMessage());
        }

        try {
            if (connection != null)
                connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        System.out.println("[dbDriver] Close test.db connection... Goodbye!");
    }

    public static void main(String[] args) {
       Driver db = new Driver();
        DataGenerator dataGenerator=DataGenerator.getInstance();
        FileManager fileManager=FileManager.getInstance();
        fileManager.generateTxt(100000, 100);
        System.out.println(fileManager.editMerge(" "));//чтобы не удалять ничего, строка " "
        db.writeFromFile(fileManager.getMergeFile());
        System.out.println("Int sum: "+db.getNumbSum());

       // db.getStudents();
        //db.getStudentCount();
       //db.close();
    }
}