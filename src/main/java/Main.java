import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        System.out.println(json);
        writeString(json);
    }

    private static void writeString(String json) {
        try (FileWriter jsonWrite = new FileWriter("data.json")) {
            jsonWrite.write(json);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
            staff.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }

    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

}
