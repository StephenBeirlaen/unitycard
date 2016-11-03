package be.nmct.unitycard;

import android.app.Application;

import com.facebook.stetho.Stetho;

import be.nmct.unitycard.di.ApplicationComponent;
import be.nmct.unitycard.di.ApplicationModule;
import be.nmct.unitycard.di.DaggerApplicationComponent;

/**
 * Created by Stephen on 1/11/2016.
 */

public class UnityCardApplication extends Application {
    // We erven van de klasse application om via dagger dependency injection te doen.
    // Deze klasse wordt dan ingevuld in de manifest in de plaats van de default klasse
    ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger Dependency Injection
        component = DaggerApplicationComponent.builder()
                // list of modules that are part of this component need to be created here too
                // This also corresponds to the name of your module: %component_name%Module
                .applicationModule(new ApplicationModule(this))
                .build();

        // ---- Stetho debugging tool init ----
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
