package be.nmct.unitycard.di;

import android.app.Application;

import javax.inject.Singleton;

import be.nmct.unitycard.repositories.GeoCodeClient;
import be.nmct.unitycard.repositories.RestClient;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    RestClient provideRestClient() {
        return new RestClient();
    }

    @Provides
    @Singleton
    GeoCodeClient provideGeoCodeClient() { return new GeoCodeClient(); }
}