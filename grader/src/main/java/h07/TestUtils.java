package h07;

import h07.person.Person;
import h07.person.PersonFilter;
import h07.person.PersonToIntFunction;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.IntBinaryOperator;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtils {
    @NotNull
    public static Class<?> getPersonClass(String s) {
        Class<?> personFilter = null;
        try {
            personFilter = Class.forName("H07.person." + s);
        } catch (ClassNotFoundException e) {
            fail("Die Klasse " + s + " existiert nicht");
        }
        return personFilter;
    }

    public static Object personGet(Class<?> clazz, Object person, String name) {
        try {
            var field = clazz.getField(name);
            return field.get(person);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Attribut " + name + " eines Person-Objekts konnte nicht bestimmt werden  ");
            throw new RuntimeException(e);
        }
    }

    public static Object get(Class<?> clazz, Object traits, String name) {
        try {
            var getter = Arrays.stream(clazz.getMethods()).filter(me -> me.getName().toLowerCase()
                .contains(name.toLowerCase())).findAny().orElseThrow();
            return getter.invoke(traits);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Attribut " + name + " eines Objekts von Typ " + clazz.getSimpleName() + " konnte nicht bestimmt werden  ");
            throw new RuntimeException(e);
        }
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameter) {
        try {
            return clazz.getMethod(name, parameter);
        } catch (NoSuchMethodException e) {
            fail("Methode mit Namen " + name + " konnte in der Klasse " + clazz.getSimpleName() + " nicht gefunden werden.");
            throw new RuntimeException(e);
        }
    }

    public static Object invokeMethod(Method method, Object caller, Object... parameter) {
        try {
            return method.invoke(caller, parameter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Methode " + method.toGenericString() + " konnte nicht erfolgreich ausgeführt werden.");
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            fail("Attribut " + name + " konnte in der Klasse " + clazz.getSimpleName() + " nicht gefunden werden.");
            throw new RuntimeException(e);
        }
    }

    public static <T, R> void forEach(Collection<T> inputT, Collection<R> inputR, BiConsumer<T, R> biConsumer) {
        Iterator<R> rit = inputR.iterator();
        Iterator<T> tit = inputT.iterator();
        while (tit.hasNext() && rit.hasNext()) {
            biConsumer.accept(tit.next(), rit.next());
        }
    }

    public static <T, R, S> void forEach(Collection<T> inputT, Collection<R> inputR, Collection<S> inputS,
                                         TriConsumer<T, R, S> triConsumer) {
        Iterator<R> rit = inputR.iterator();
        Iterator<T> tit = inputT.iterator();
        Iterator<S> sit = inputS.iterator();
        while (tit.hasNext() && rit.hasNext() && sit.hasNext()) {
            triConsumer.accept(tit.next(), rit.next(), sit.next());
        }
    }

    public static Object makePerson(String lastName, String firstName, String street, int houseNumber, int postalCode) {
        var person = getPersonClass("Person");
        try {
            return person.getConstructor(String.class, String.class, String.class, int.class, int.class)
                .newInstance(lastName, firstName, street, houseNumber, postalCode);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            fail("Ein Person konnnte nicht erstellt werden", e);
            return null;
        }
    }

    public static Object people() {
        var init = new Object[]{
            makePerson("a", "a", "a", 3, 3),
            makePerson("a", "a", "a", 2, 3),
            makePerson("a", "a", "a", 1, 2),
            makePerson("b", "a", "a", 1, 2),
            makePerson("a", "a", "a", 1, 2),
            makePerson("a", "a", "a", 2, 2),
            makePerson("a", "a", "a", 2, 2),
            makePerson("c", "a", "a", 1, 64289),
            makePerson("c", "a", "a", 1, 64289),
            makePerson("c", "a", "a", 2, 3),
        };
        var result = Array.newInstance(getPersonClass("Person"), init.length);
        for (var i = 0; i < init.length; i++) {
            Array.set(result, i, init[i]);
        }
        return result;
    }

    public static Object filtered() {
        var init = new Object[]{
            makePerson("a", "a", "a", 3, 3),
            makePerson("a", "a", "a", 2, 3),
            makePerson("c", "a", "a", 2, 3),
        };
        var result = Array.newInstance(getPersonClass("Person"), init.length);
        for (var i = 0; i < init.length; i++) {
            Array.set(result, i, init[i]);
        }
        return result;
    }

    public static int[] mapped = {9, 6, 2, 2, 2, 4, 4, 64289, 64289, 6};

    static void assertPeopleEquals(Object[] filtered, Object[] result) {
        assertEquals(filtered.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertPersonEquals(filtered[i], result[i]);
        }
    }

    static void assertPersonEquals(Object p1, Object p2) {
        var person = getPersonClass("Person");
        assertEquals(personGet(person, p1, "houseNumber"), personGet(person, p2, "houseNumber"));
        assertEquals(personGet(person, p1, "postalCode"), personGet(person, p2, "postalCode"));
        assertEquals(personGet(person, p1, "firstName"), personGet(person, p2, "firstName"));
        assertEquals(personGet(person, p1, "lastName"), personGet(person, p2, "lastName"));
        assertEquals(personGet(person, p1, "street"), personGet(person, p2, "street"));
    }

    public static Object getTraitsObject(boolean withCombine) {
        var personFilter = personFilter();
        var personToIntFunction = personToIntFunction();
        var traits = TestUtils.getPersonClass("Traits");
        var consOpt = Arrays.stream(traits.getConstructors()).filter(c -> c.getParameterCount() >= 4).findAny();
        if (consOpt.isEmpty()) {
            fail("Traits hat keinen geeigneten Konstruktor zum Testen von MyFunctionWithFilterMapAndFold1");
        }
        Object traitsObj = null;
        try {
            var cons = consOpt.get();
            IntBinaryOperator op = (a, b) -> a + b + 1;
            if (withCombine) {
                IntBinaryOperator combine = (a, b) -> a * b / 7;
                traitsObj = cons.newInstance(op, 357, personToIntFunction, personFilter, combine);
            } else {
                if (cons.getParameterCount() == 5) {
                    traitsObj = cons.newInstance(op, 357, personToIntFunction, personFilter, null);
                } else {
                    traitsObj = cons.newInstance(op, 357, personToIntFunction, personFilter);
                }
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            fail("Der Konstruktor des Traits-Objekt (das zur Erstellung der " +
                "MyFunctionWithFilterMapAndFold1 nötig ist) konnte nicht erfolgreich aufgerufen werden.", e);
        }
        return traitsObj;
    }

    public static Object personFilter() {
        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodType methodType = MethodType.methodType(boolean.class, TestUtils.getPersonClass("Person"));
            final CallSite site = LambdaMetafactory.metafactory(lookup,
                "test",
                MethodType.methodType(TestUtils.getPersonClass("PersonFilter")),
                methodType,
                lookup.findStatic(TestUtils.class, "postalCodeEquals3",
                    MethodType.methodType(boolean.class, TestUtils.getPersonClass("Person"))),
                methodType);
            return (PersonFilter) site.getTarget().invokeExact();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException();
        }
    }

    private static boolean postalCodeEquals3(Person p) {
        return p.getPostalCode() == 3;
    }

    public static Object personToIntFunction() {
        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodType methodType = MethodType.methodType(int.class, TestUtils.getPersonClass("Person"));
            final CallSite site = LambdaMetafactory.metafactory(lookup,
                "apply",
                MethodType.methodType(TestUtils.getPersonClass("PersonToIntFunction")),
                methodType,
                lookup.findStatic(TestUtils.class, "personIntProduct", MethodType.methodType(int.class, TestUtils.getPersonClass("Person"))),
                methodType);
            return (PersonToIntFunction) site.getTarget().invokeExact();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException();
        }
    }

    private static int personIntProduct(Person p) {
        return p.getPostalCode() * p.getHouseNumber();
    }
}
