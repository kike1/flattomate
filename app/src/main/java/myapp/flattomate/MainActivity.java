package myapp.flattomate;

import android.app.Application;

import myapp.myapp.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by kike on 10/8/16.
 */
public class MainActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arimo-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
