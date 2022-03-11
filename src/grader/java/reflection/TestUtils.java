package reflection;

import com.google.common.reflect.ClassPath;
import org.checkerframework.checker.units.qual.A;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Utilities by Ruben
 *
 * @author Ruben Deisenroth
 */
public class TestUtils {

    public static final int BRIDGE = 0x00000040;
    public static final int VARARGS = 0x00000080;
    public static final int SYNTHETIC = 0x00001000;
    public static final int ANNOTATION = 0x00002000;
    public static final int ENUM = 0x00004000;
    public static final int MANDATED = 0x00008000;

    /**
     * Asserts matching Modifiers
     *
     * @param expected Erwarteter Wert
     * @param actual   Eigentlicher Wert
     * @param name     Feld Name
     */
    public static void assertModifier(int expected, int actual, String name) {
        if (expected < 0) {
            return;
        }
        assertEquals(expected, actual, String.format("incorrect modifiers for %s", name));
    }

    /**
     * Asserts matching Modifiers
     *
     * @param expected Erwarteter Wert
     * @param clazz    Klasse mit Modifier
     */
    public static void assertModifier(int expected, Class<?> clazz) {
        assertModifier(expected, clazz.getModifiers(), "class <" + clazz.getName() + ">");
    }

    /**
     * Asserts matching Modifiers
     *
     * @param expected Erwarteter Wert
     * @param method   Methode mit Modifier
     */
    public static void assertModifier(int expected, Method method) {
        assertModifier(expected, method.getModifiers(),
            "method <" + method.getDeclaringClass() + "." + method.getName() + ">");
    }

    /**
     * Asserts matching Modifiers
     *
     * @param expected    Erwarteter Wert
     * @param constructor Konstruktor mit Modifier
     */
    public static void assertModifier(int expected, Constructor<?> constructor) {
        assertModifier(expected, constructor.getModifiers(),
            "constructor <" + constructor.getDeclaringClass() + "." + constructor.getName() + ">");
    }

    /**
     * Asserts matching Modifiers
     *
     * @param expected Erwarteter Wert
     * @param attribut Attribut mit Modifier
     */
    public static void assertModifier(int expected, Field attribut) {
        assertModifier(expected, attribut.getModifiers(),
            "attribute <" + attribut.getDeclaringClass() + "." + attribut.getName() + ">");
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     *
     * @param s1 String 1
     * @param s2 String 2
     * @return the similarity
     */
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
            /* both strings are zero length */
        }
        /*
         * // If you have Apache Commons Text, you can use it to calculate the edit
         * distance: LevenshteinDistance levenshteinDistance = new
         * LevenshteinDistance(); return (longerLength -
         * levenshteinDistance.apply(longer, shorter)) / (double) longerLength;
         */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     *
     * @param s1 string 1
     * @param s2 string 2
     * @return the calculated similarity (a number within 0 and 1) between two
     * strings.
     * @see http://rosettacode.org/wiki/Levenshtein_distance#Java
     */
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    /**
     * Scans all classes accessible from the context class loader which belong to
     * the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws IOException            if an IO Exception occurs
     */
    public static Class<?>[] getClasses(String packageName) throws IOException {
        @SuppressWarnings("UnstableApiUsage") var cycle = TestCycleResolver.getTestCycle();
        if (cycle != null) {
            // Autograder Run
            //noinspection UnstableApiUsage
            return cycle.getSubmission().getClassNames().stream()
                .map(x -> assertDoesNotThrow(() -> cycle.getClassLoader().loadClass(x))).toArray(Class<?>[]::new);
        } else {
            // Regular Junit Run
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            //noinspection UnstableApiUsage,Convert2MethodRef
            return ClassPath.from(loader).getTopLevelClasses(packageName).stream().map(x -> x.load())
                .toArray(Class<?>[]::new);
        }
    }

    /**
     * Returns {@code true} if {@link A#getTestCycle()}
     * does not return {@code null}
     *
     * @return {@code true} if {@link A#getTestCycle()}
     * does not return {@code null}
     */
    public static boolean isAutograderRun() {
        //noinspection UnstableApiUsage
        return TestCycleResolver.getTestCycle() != null;
    }
}
