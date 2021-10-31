package anhtester.com.testcases;

import anhtester.com.common.helpers.Helpers;
import anhtester.com.common.utilities.Props;
import anhtester.com.common.utilities.listeners.ListenersTest;
import okhttp3.Headers;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;

//@Listeners(ListenersTest.class)
public class TestSimpleCode {

    @Test
    public void testGetCurrentDirectory() {
        System.out.println(Helpers.getCurrentDir());
    }

    @Test
    public void testGetPropertiesFile() {
        //Props.setFile("src/test/resources/DataTest.properties");
        Props.loadAllFiles();
        System.out.println(Props.getValue("hongthai"));

        Props.setFile("src/test/resources/DataTest.properties");
        Props.setValue("abc", "AN123");
    }

    @Test
    public void testSplitString() {
        String s1 = "Automation, Testing, Selenium, Java";

        for (String arr : Helpers.splitString(s1, ", ")) {
            System.out.println(arr);
        }
    }

}
