package student;

import reflection.ClassTester;
import reflection.MethodTester;
import reflection.ParameterMatcher;
import tutor.Mocked;

import java.util.List;
import java.util.Objects;

import static h07.Global.SIMILARITY;
import static java.lang.reflect.Modifier.ABSTRACT;
import static java.lang.reflect.Modifier.INTERFACE;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Objects.requireNonNullElseGet;

public abstract class Traverser_STUD implements Mocked {

    private static ClassTester<?> cTraverserO;
    private static MethodTester mGetFirstIndexO;
    private static MethodTester mGetNextIndexO;

    public static ClassTester<?> cTraverser() {
        return cTraverserO = Objects.requireNonNullElseGet(cTraverserO, () -> new ClassTester<>(
            "h06",
            "Traverser",
            SIMILARITY,
            PUBLIC | ABSTRACT | INTERFACE).assureResolved());
    }

    public static MethodTester mGetFirstIndex() {
        return mGetFirstIndexO = requireNonNullElseGet(mGetFirstIndexO, () -> new MethodTester(
            cTraverser(),
            "getFirstIndex",
            SIMILARITY,
            PUBLIC,
            int.class,
            List.of(new ParameterMatcher(double[].class))
        ).assureResolved());
    }

    public static MethodTester mGetNextIndex() {
        return mGetNextIndexO = requireNonNullElseGet(mGetNextIndexO, () -> new MethodTester(
            cTraverser(),
            "getNextIndex",
            SIMILARITY,
            PUBLIC,
            int.class,
            List.of(new ParameterMatcher(int.class))
        ).assureResolved());
    }

    public abstract int getFirstIndex(double[] array);

    public abstract int getNextIndex(int index);
}
