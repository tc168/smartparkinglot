package humber.thunderbolts;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by n01075212 on 11/15/2016.
 */
public class Theme
{
    private static int sTheme;
    public final static int BlackTheme = 0;
    public final static int BlueTheme = 1;
    public final static int GreenTheme = 2;
    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme) {
            default:
            case BlackTheme:
                activity.setTheme(R.style.BlackTheme);
                break;
            case BlueTheme:
                activity.setTheme(R.style.BlueTheme);
                break;
            case GreenTheme:
                activity.setTheme(R.style.GreenTheme);
                break;
        }
    }
}
