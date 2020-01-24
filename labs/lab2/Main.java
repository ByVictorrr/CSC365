import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Set;

public class Main {

    // usage: ./InsertHelper fileName tableName
    public static void main(String[] args) {
        String fileName = args[0];
        String tableName = args[1];
        InsertBuilder ib = InsertBuilder.getInstance();
        ib.formatPrint(fileName,tableName);
    }

}

class InsertBuilder{
    private static InsertBuilder instance = null;
    private static String schemaStatement;

    public static InsertBuilder getInstance() {
        if (instance==null){
            instance=new InsertBuilder();
        }
        return instance;
    }
    public void formatPrint(String fileName, String tableName) {
        BufferedReader csvReader;
        String row;
        boolean isFirstRow = true;
        try {
            csvReader = new BufferedReader(new FileReader(fileName));
            while ((row = csvReader.readLine()) != null) {
                String[] values = row.split(",");
                if (isFirstRow) {
                    setSchemaStatement(tableName, values);
                    isFirstRow = false;
                } else {
                    String literalStatement = formatLiterals(values);
                    System.out.println(schemaStatement +" "+ literalStatement + ";");
                }
            }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        private void setSchemaStatement(String tableName, String[] schema){
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ")
                    .append(tableName)
                    .append(" ( ");
            for(int i = 0; i < schema.length; i++){
                sb.append(schema[i].trim());
                if(!(i==schema.length-1)){
                    sb.append(", ");
                }else{
                    sb.append(" )");
                }
            }
            schemaStatement= sb.toString();
        }
        private String formatLiterals(String [] values){
            StringBuilder sb = new StringBuilder("VALUES ")
                                    .append("(");
            boolean isLast = false;
            for(int i = 0; i < values.length; i++){
               sb.append("'")
                       .append(values[i].trim())
                       .append("'");
                       if(!(i==values.length-1)){
                           sb.append(",");
                       }else{
                           sb.append(")");
                       }
            }
            return sb.toString();
        }

}

