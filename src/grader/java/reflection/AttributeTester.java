package reflection;

import java.lang.reflect.Field;
import java.util.Objects;

import static java.lang.reflect.Modifier.isStatic;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static reflection.TestUtils.assertModifier;
import static tutor.Utils.TestCollection.Mode.SHOW_ALL;
import static tutor.Utils.TestCollection.test;

public class AttributeTester {

    private ClassTester<?> classTester;
    private AttributeMatcher matcher;
    private Field field;

    public AttributeTester setMatcher(AttributeMatcher matcher) {
        this.matcher = matcher;
        return this;
    }

    public AttributeTester setClassTester(ClassTester<?> classTester) {
        this.classTester = classTester;
        return this;
    }

    public AttributeTester assureExists() {
        Objects.requireNonNull(classTester, "no class tester defined");
        Objects.requireNonNull(matcher, "no matcher defined");
        field = classTester.resolveAttribute(matcher);
        return this;
    }

    public AttributeTester assertModifiers() {
        if (matcher.modifier >= 0) {
            assertModifier(matcher.modifier, field);
        }
        return this;
    }

    public AttributeTester assertDeclaration() {
        test()
            .add(this::assertModifiers)
            .run(SHOW_ALL);
        return this;
    }

    private void assureAccessible(Object object) {
        if (!field.canAccess(isStatic(field.getModifiers()) ? null : object)) {
            assertDoesNotThrow(() -> field.setAccessible(true));
        }
    }

    public <T> T getValue() {
        return getValue(null);
    }

    public <T> T getValue(Object object) {
        assureAccessible(object);
        return (T) assertDoesNotThrow(() -> field.get(object));
    }

    public <T> void setValue(Object object, T value) {
        assureAccessible(object);
        assertDoesNotThrow(() -> field.set(object, value));
    }
}
