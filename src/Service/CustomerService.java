package Service;

import Entity.Customer;
import IGeneral.IGeneric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerService implements IGeneric<Customer> {

    @Override
    public List<Customer> readData(String filePath) {
        List<Customer> customers = new ArrayList<>();
        File f = new File(filePath);
        if(f.exists()){
            try{
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while((line=br.readLine())!=null){
                    Customer customer = new Customer();
                    String[] data = line.split(";\\s*");
                    customer.setId(Integer.parseInt(data[0]));
                    customer.setName(data[1]);
                    customer.setPhone(data[2]);
                    customers.add(customer);
                }
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
        return customers;
    }

    @Override
    public void saveData(String filePath, List<Customer> list) {

    }
}
