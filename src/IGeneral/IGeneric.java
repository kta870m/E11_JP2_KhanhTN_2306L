package IGeneral;

import java.util.List;

public interface IGeneric<T> {
    List<T> readData(String filePath);
    void saveData(String filePath,List<T> list);
}
