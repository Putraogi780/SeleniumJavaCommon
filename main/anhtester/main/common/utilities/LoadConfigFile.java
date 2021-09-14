package anhtester.main.common.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class LoadConfigFile {

    private static String urlPropertiesFile = "src/test/resources/ConfigData.properties";

    public static String getValueProp(String key) {
        String keyval = null;
        File file = new File(urlPropertiesFile);

        //Creating properties object
        Properties prop = new Properties();
        //Creating InputStream object to read data
        FileInputStream objInput = null;
        try {
            objInput = new FileInputStream(file);
            //Reading properties key/values in file
            prop.load(objInput);
            keyval = prop.getProperty(key);
            objInput.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return keyval;
    }

    public static void setValueProp(String key, String keyValue) {
        File file = new File(urlPropertiesFile);
        Properties prop = new Properties();
        FileInputStream objInput = null;
        try {
            objInput = new FileInputStream(file);
            prop.load(objInput);
            objInput.close();

            //Ghi vào cùng file Prop với file lấy ra
            FileOutputStream out = new FileOutputStream(urlPropertiesFile);
            prop.setProperty(key, keyValue);
            prop.store(out, null);
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
