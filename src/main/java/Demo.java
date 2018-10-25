import com.cjburkey.corestore.Store;
import com.cjburkey.corestore.io.StoreIO;
import java.io.File;

public final class Demo {
    
    private static final File exampleFile = new File(System.getProperty("user.home"), "/Desktop/test.bs");
    
    public static void main(String[] args) {
        System.out.println("Demo CoreStore program running.");
        System.out.println("Demo file: " + exampleFile.getAbsolutePath());
        
        createExample();
        loadExample();
    }
    
    private static void createExample() {
        Store store = new Store();
        
        Store jim = new Store();
        jim.set("name", "Jim");
        jim.set("age", 32.5f);
        jim.set("children", 3498758734L);
        
        store.set("person_jim", jim);
        
        StoreIO.write(store, exampleFile);
    }
    
    private static void loadExample() {
        Store found = StoreIO.read(exampleFile, null, false, true);
        if (found != null) {
            for (String key : found.getKeys()) {
                System.out.print(key);
                Store storeAt;
                if ((storeAt = found.get(key, Store.class, null)) != null) {
                    System.out.print(" - ");
                    for (String keyy : storeAt.getKeys()) {
                        System.out.print(keyy);
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        } else {
            System.err.println("No store found in file");
        }
    }
    
}
