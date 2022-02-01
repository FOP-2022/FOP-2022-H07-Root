package student;

import org.jetbrains.annotations.NotNull;
import reflection.AttributeMatcher;
import reflection.AttributeTester;
import reflection.ClassTester;
import tutor.Mocked;

import java.lang.reflect.Modifier;
import java.util.Objects;

import static h07.Global.SIMILARITY;
import static java.util.Objects.requireNonNull;

public class ReturnData_STUD implements Mocked {

    private static ClassTester<?> cReturnDataO;

    public static ClassTester<?> cReturnData() {
        return cReturnDataO = Objects.requireNonNullElseGet(cReturnDataO, () -> new ClassTester<>(
            "h06",
            "ReturnData",
            SIMILARITY).assureResolved());
    }

    private static AttributeTester aResultO;

    public static AttributeTester aResult() {
        return aResultO = Objects.requireNonNullElseGet(aResultO, () ->
            new AttributeTester()
                .setClassTester(cReturnData())
                .setMatcher(
                    new AttributeMatcher(
                        "result",
                        SIMILARITY,
                        Modifier.PUBLIC,
                        int.class)).assureExists());
    }

    private static AttributeTester aNextIndexO;

    public static AttributeTester aNextIndex() {
        return aNextIndexO = Objects.requireNonNullElseGet(aNextIndexO, () -> new AttributeTester()
            .setClassTester(cReturnData())
            .setMatcher(
                new AttributeMatcher(
                    "nextIndex",
                    SIMILARITY,
                    Modifier.PUBLIC,
                    int.class)
            ).assureExists());
    }


    private final Object object;

    public ReturnData_STUD(Object object) {
        this.object = requireNonNull(object);
    }

    public ReturnData_STUD() {
        this(cReturnData().getNewInstance());
    }

    public ReturnData_STUD(int result, int nextIndex) {
        this(cReturnData().getNewInstance());
        setResult(result);
        setNextIndex(nextIndex);
    }

    public void setResult(int result) {
        aResult().setValue(object, result);
    }

    public void setNextIndex(int result) {
        aNextIndex().setValue(object, result);
    }

    public int getResult() {
        return aResult().getValue(object);
    }

    public int getNextIndex() {
        return aNextIndex().getValue(object);
    }

    public @NotNull
    Object getActualObject() {
        return object;
    }
}
