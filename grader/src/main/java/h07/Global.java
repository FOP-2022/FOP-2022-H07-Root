package h07;

import tutor.MyTransformer;
import tutor.Utils.State;

import java.util.Random;

public interface Global {

    Random RANDOM = new Random(42);
    double SIMILARITY = 0.8;
    int T = 10;
    //Logger LOGGER = LogManager.getLogger("Global");
    MyTransformer TRANSFORMER = new MyTransformer();
    State MODE = new State();
}
