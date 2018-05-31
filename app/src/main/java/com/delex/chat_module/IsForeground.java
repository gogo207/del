package com.delex.chat_module;

/**
 * Created by embed on 12/8/16.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Usage:
 *
 * 1. Get the Foreground Singleton, passing a Context or Application object unless you
 * are sure that the Singleton has definitely already been initialised elsewhere.
 *
 * 2.a) Perform a direct, synchronous check: Foreground.isForeground() / .isBackground()
 *
 * or
 *
 * 2.b) Register to be notified (useful in Service or other non-UI components):
 *
 *   Foreground.Listener myListener = new Foreground.Listener(){
 *       public void onBecameForeground(){
 *           // ... whatever you want to do
 *       }
 *       public void onBecameBackground(){
 *           // ... whatever you want to do
 *       }
 *   }
 *
 *   public void on_Create(){
 *      super.on_Create();
 *      Foreground.get(this).addListener(listener);
 *   }
 *
 *   public void onDestroy(){
 *      super.on_Create();
 *      Foreground.get(this).removeListener(listener);
 *   }
 */
public class IsForeground implements Application.ActivityLifecycleCallbacks {

    public static final long CHECK_DELAY = 500;
    public static final String TAG = IsForeground.class.getName();
    private static IsForeground instance;
    private boolean foreground = false, paused = true;
    private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
    private Runnable check;
    private Handler handler = new Handler();

    public interface Listener
    {
        public void onBecameForeground();
        public void onBecameBackground();
    }

    /**
     * Its not strictly necessary to use this method - _usually_ invoking
     * get with a Context gives us a path to retrieve the Application and
     * initialise, but sometimes (e.g. in test harness) the ApplicationContext
     * is != the Application, and the docs make no guarantees.
     *
     * @param application
     * @return an initialised Foreground instance
     */
    public static IsForeground init(Application application)
    {
        if (instance == null)
        {
            instance = new IsForeground();
            application.registerActivityLifecycleCallbacks(instance);   //Registering life cycle for application class.
        }
        return instance;
    }

    /**
     * <h2>get</h2>
     * <p>
     *     to create the single instance of this class
     * </p>
     * @param application: application reference
     * @return: return the singleton instance of this class
     */
    public static IsForeground get(Application application)
    {
        if (instance == null)
        {
            init(application);
        }
        return instance;
    }

    /**
     * <h2>get</h2>
     * <p>
     *     to create the single instance of this class
     * </p>
     * @param context: calling activity reference
     * @return: return the singleton instance of this class
     */
    public static IsForeground get(Context context)
    {
        if (instance == null)
        {
            Context appContext = context.getApplicationContext();
            if (appContext instanceof Application)
            {
                init((Application) appContext);
            }
            throw new IllegalStateException(
                    "Foreground is not initialised and " +
                            "cannot obtain the Application object");
        }
        return instance;
    }

    /**
     * <h2>get</h2>
     * <p>
     *     to create the single instance of this class
     * </p>
     * @return: return the singleton instance of this class
     */
    public static IsForeground get()
    {
        if (instance == null)
        {
            throw new IllegalStateException(
                    "Foreground is not initialised - invoke " +
                            "at least once with parameterised init/get");
        }
        return instance;
    }

    /**
     * <h2>isForeground</h2>
     * <p>
     *     this method will return the state of the app
     *     whether the app is in  foreground or not
     * </p>
     * @return boolean:  whether the app is in  foreground or not
     */
    public boolean isForeground()
    {
        return foreground;
    }

    /**
     * <h2>isBackground</h2>
     * <p>
     *     this method will return the state of the app
     *     whether the app is in background or not
     * </p>
     * @return boolean:  whether the app is in  background or not
     */
    public boolean isBackground()
    {
        return !foreground;
    }

    /**
     * <h2>addListener</h2>
     * <p>
     *     method to add listener to get callback
     * </p>
     */
    public void addListener(Listener listener)
    {
        listeners.add(listener);
    }

    /**
     * <h2>removeListener</h2>
     * <p>
     *     method to remove listener
     * </p>
     */
    public void removeListener(Listener listener)
    {
        listeners.remove(listener);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null)
        {
            handler.removeCallbacks(check);
        }

        if (wasBackground)
        {
            Log.i(TAG, "went foreground");
            for (Listener l : listeners)
            {
                try
                {
                    l.onBecameForeground();
                }
                catch (Exception e)
                {
                    Log.e(TAG, "Listener threw exception!", e);
                }
            }
        }
        else
        {
            Log.i(TAG, "still foreground");
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;

        if (check != null)
        {
            handler.removeCallbacks(check);
        }

        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused)
                {
                    foreground = false;
                    Log.i(TAG, "went background");
                    for (Listener l : listeners)
                    {
                        try {
                            l.onBecameBackground();
                        }
                        catch (Exception e)
                        {
                            Log.e(TAG, "Listener threw exception!", e);
                        }
                    }
                }
                else {
                    Log.i(TAG, "still foreground");
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
