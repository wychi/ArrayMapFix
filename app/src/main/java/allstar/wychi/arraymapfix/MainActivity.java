package allstar.wychi.arraymapfix;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;

import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void arrayMapClassCastExceptionDemo(final Map<String, String> criminal, final Map<String, String> victim) {

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

    public void useFixArrayMap(View view) {
        final ArrayMap<String, String> criminal = new ArrayMap<>();
        final ArrayMap<String, String> victim = new ArrayMap<>();

        arrayMapClassCastExceptionDemo(criminal, victim);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void useFrameworkArrayMap(View view) {
        final android.util.ArrayMap<String, String> criminal = new android.util.ArrayMap<>();
        final android.util.ArrayMap<String, String> victim = new android.util.ArrayMap<>();
        arrayMapClassCastExceptionDemo(criminal, victim);
    }

    public void doDebug(View view) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("a", "1");
        map.put("b", "1");
        map.clear();
    }
}
