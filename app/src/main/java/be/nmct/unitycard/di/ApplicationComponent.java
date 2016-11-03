package be.nmct.unitycard.di;

import javax.inject.Singleton;

import be.nmct.unitycard.repositories.AuthRepository;
import be.nmct.unitycard.repositories.RetailerRepository;
import dagger.Component;

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    // to update the fields in your activities
    // the activities, services, or fragments that will can be added should be declared in this class with individual inject() methods:
    void inject(AuthRepository authRepository);
    void inject(RetailerRepository retailerRepository);
}
