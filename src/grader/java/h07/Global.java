package h07;

import org.slf4j.Logger;
import org.sourcegrade.jagr.launcher.env.Jagr;
import tutor.MyTransformer;
import tutor.Utils.State;

import java.util.Random;

public interface Global {

    Random RANDOM = new Random(42);
    double SIMILARITY = 0.8;
    int T = 10;
    Logger LOGGER = Jagr.Default.getInjector().getInstance(org.slf4j.Logger.class);
    MyTransformer TRANSFORMER = new MyTransformer();
    State MODE = new State();
}
