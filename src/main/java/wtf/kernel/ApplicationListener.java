package wtf.kernel;

import com.google.inject.Module;

public interface ApplicationListener extends Module {

    void onStart();
}
