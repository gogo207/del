package com.delex.utility;

import android.content.Context;
import android.graphics.Typeface;

/**
 * <h2>AppTypeface</h2>
 * This class contains several methods that are used for setting and getting methods for typeFace.
 */

public class AppTypeface
{
    private static AppTypeface setTypeface = null;
    private Typeface pro_narMedium;
    private Typeface pro_News;
    private Typeface sans_regular;
    private Typeface sans_semiBold;
    private Typeface sans_light;
    private Typeface dinBlackAlternate;

    /**
     * <h2>AppTypeface</h2>
     * @param context: calling activity reference
     * @return: Single instance of this class
     */
    public static AppTypeface getInstance(Context context)
    {
        if (setTypeface == null)
        {
            setTypeface = new AppTypeface(context.getApplicationContext());
        }
        return setTypeface;
    }

    private AppTypeface(Context context)
    {
        initTypefaces(context);
    }

    /**
     * <h2>initTypefaces</h2>
     * <p>
     *     method to initializes the typefaces of the app
     * </p>
     * @param context Context of the activity from where it is called
     */
    private void initTypefaces(Context context)
    {
        this.pro_narMedium = Typeface.createFromAsset(context.getAssets(),"fonts/ClanPro-NarrMedium.otf");
        this.pro_News = Typeface.createFromAsset(context.getAssets(),"fonts/ClanPro-NarrNews.otf");
        this.sans_regular = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular_0.ttf");
        this.sans_semiBold = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold_0.ttf");
        this.sans_light = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light_0.ttf");
        this.dinBlackAlternate=Typeface.createFromAsset(context.getAssets(),"fonts/DINBlackAlternate.ttf");
    }

    public Typeface getPro_narMedium() {
        return pro_narMedium;
    }

    public Typeface getPro_News() {
        return pro_News;
    }

    public Typeface getSans_regular() {
        return sans_regular;
    }

    public Typeface getSans_semiBold() {
        return sans_semiBold;
    }

    public Typeface getSans_light() {
        return sans_light;
    }

    public Typeface getDinBlackAlternate() {
        return dinBlackAlternate;
    }
}
