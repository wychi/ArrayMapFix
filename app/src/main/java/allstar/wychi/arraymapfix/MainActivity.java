package allstar.wychi.arraymapfix;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        arrayMapClassCastExceptionDemo();
//        arrayMapClassCastExceptionDemo();
//        arrayMapClassCastExceptionDemo();

        for(int i=0;i<10000; i++) {
            {
                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("a", "1");
                map.put("b", "1");
                map.put("c", "1");
                map.put("d", "1");
                map.put("e", "1");

                map.clear();
            }
            {
                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("a", "1");
                map.put("b", "1");

                map.clear();
            }
        }

    }


    private void arrayMapClassCastExceptionDemo() {
        final ArrayMap<String, String> criminal = new ArrayMap<>();
        final ArrayMap<String, String> victim = new ArrayMap<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        if (criminal.size() >= 8) {
                            // trigger freeArrays
                            criminal.clear();
                        }
                    } catch (IndexOutOfBoundsException ingnore) {
                        // multi thread issue
                        continue;
                    }
                }
            }
        }, "criminal-thread-0").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // may corrupt ArrayMap
                while (true) {
                    try {
                        // may crash here
                        criminal.put("" + System.currentTimeMillis(), "");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // ignore
                    }
                }
            }
        }, "criminal-thread-1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // may corrupt ArrayMap
                while (true) {
                    // poll array from memory pool
                    victim.put("x", ""); // may crash here
                    victim.clear();
                }
            }
        }, "victim").start();
    }
}
