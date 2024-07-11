package Repository;

import Entity.Customer;
import IGeneric.FileGeneric;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CustomerRepo implements FileGeneric<Customer> {
    public static String rootPath;
    public static List<Customer> customers;


    @Override
    public List<Customer> getData(String filePath) {
        filePath = rootPath.replace("\\","/") + "/data/Customer.txt";
        try{
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            while((line = br.readLine()) != null){
                Customer c = new Customer();
                String[] data = line.split("; ");
                c.setId(Integer.parseInt(data[0]));
                c.setName(data[1]);
                c.setPhone(data[2]);
                customers.add(c);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        return customers;
    }

    @Override
    public List<Customer> writeData(String filePath) {
        return List.of();
    }
}
