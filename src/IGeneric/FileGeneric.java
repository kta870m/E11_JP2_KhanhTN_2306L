package IGeneric;

import java.util.List;

public interface FileGeneric<T> {
    List<T> getData(String filePath);
    List<T> writeData(String filePath);
}
