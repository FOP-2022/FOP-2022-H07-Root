package h07;

import h07.person.Person;
import h07.person.PersonFilter;
import h07.person.PersonToIntFunction;
import h07.person.Traits;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;

import static org.junit.jupiter.api.Assertions.*;

class TestUtils {
    static Class<?> getPersonClass(String s) {
        Class<?> personFilter = null;
        try {
            personFilter = Class.forName("h07.person." + s);
        } catch (ClassNotFoundException e) {
            fail("Die Klasse " + s + " existiert nicht");
        }
        return personFilter;
    }

    static Object personGet(Class<?> clazz, Object person, String name) {
        try {
            var getter = Arrays.stream(clazz.getMethods()).filter(me -> me.getName().toLowerCase()
                .contains(name.toLowerCase()) && me.getName().contains("get")).findAny().orElseThrow(NoSuchMethodException::new);
            return getter.invoke(person);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            fail("Attribut " + name + " eines Person-Objekts konnte nicht bestimmt werden  ");
            throw new RuntimeException(e);
        }
    }

    static Object get(Class<?> clazz, Object traits, String name) {
        try {
            var getter = Arrays.stream(clazz.getMethods()).filter(me -> me.getName().toLowerCase()
                .contains(name.toLowerCase())).findAny().orElseThrow(NoSuchMethodException::new);
            return getter.invoke(traits);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            fail("Attribut " + name + " eines Objekts von Typ " + clazz.getSimpleName() + " konnte nicht bestimmt werden  ");
            throw new RuntimeException(e);
        }
    }

    static Method getMethod(Class<?> clazz, String name, Class<?>... parameter) {
        try {
            return clazz.getMethod(name, parameter);
        } catch (NoSuchMethodException e) {
            fail("Methode mit Namen " + name + " konnte in der Klasse " + clazz.getSimpleName() + " nicht gefunden werden.");
            throw new RuntimeException(e);
        }
    }

    static Object invokeMethod(Method method, Object caller, Object... parameter) {
        try {
            return method.invoke(caller, parameter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Methode " + method.toGenericString() + " konnte nicht erfolgreich ausgeführt werden.", e);
            throw new RuntimeException(e);
        }
    }

    static Field getField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            fail("Attribut " + name + " konnte in der Klasse " + clazz.getSimpleName() + " nicht gefunden werden.");
            throw new RuntimeException(e);
        }
    }

    static <T, R> void forEach(Collection<T> inputT, Collection<R> inputR, BiConsumer<T, R> biConsumer) {
        Iterator<R> rit = inputR.iterator();
        Iterator<T> tit = inputT.iterator();
        while (tit.hasNext() && rit.hasNext()) {
            biConsumer.accept(tit.next(), rit.next());
        }
    }

    static <T, R, S> void forEach(Collection<T> inputT, Collection<R> inputR, Collection<S> inputS,
                                  TriConsumer<T, R, S> triConsumer) {
        Iterator<R> rit = inputR.iterator();
        Iterator<T> tit = inputT.iterator();
        Iterator<S> sit = inputS.iterator();
        while (tit.hasNext() && rit.hasNext() && sit.hasNext()) {
            triConsumer.accept(tit.next(), rit.next(), sit.next());
        }
    }

    static Object makePerson(String lastName, String firstName, String street, int houseNumber, int postalCode) {
        var person = getPersonClass("Person");
        try {
            return person.getConstructor(String.class, String.class, String.class, int.class, int.class)
                .newInstance(lastName, firstName, street, houseNumber, postalCode);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            fail("Ein Person konnnte nicht erstellt werden", e);
            return null;
        }
    }

    static Object people() {
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

    static Object filtered() {
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

    static int[] mapped = {9, 6, 2, 2, 2, 4, 4, 64289, 64289, 6};

    static void assertPeopleEquals(Object[] filtered, Object[] result) {
        assertEquals(filtered.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertPersonEquals(filtered[i], result[i]);
        }
    }

    static void assertPersonEquals(Object p1, Object p2) {
        var person = getPersonClass("Person");
        if (p2 == null) {
            fail("Mindestens eine resultierende Person ist null");
        }
        assertEquals(personGet(person, p1, "houseNumber"), personGet(person, p2, "houseNumber"));
        assertEquals(personGet(person, p1, "postalCode"), personGet(person, p2, "postalCode"));
        assertEquals(personGet(person, p1, "firstName"), personGet(person, p2, "firstName"));
        assertEquals(personGet(person, p1, "lastName"), personGet(person, p2, "lastName"));
        assertEquals(personGet(person, p1, "street"), personGet(person, p2, "street"));
    }

    static Object getTraitsObject(boolean withCombine) {
        var personFilter = personFilter();
        var personToIntFunction = personToIntFunction();
        IntBinaryOperator op = (a, b) -> a + b + 1;
        IntBinaryOperator combine = (a, b) -> a * b / 7;
        int init = 357;
        var traits = TestUtils.getPersonClass("Traits");
        var consOpt = Arrays.stream(traits.getConstructors()).filter(c -> c.getParameterCount() >= (withCombine ? 5 : 4)).findAny();
        if (consOpt.isEmpty()) {
            fail("Traits hat keinen geeigneten Konstruktor zum Testen von" + (withCombine ? "MyFunctionWithAdjacent" : "MyFunctionWithFilterMapAndFold1"));
        }
        return makeTraits(Map.of(
            PersonFilter.class, new ArrayList<>(List.of(personFilter)),
            IntBinaryOperator.class, new ArrayList<>(List.of(op, combine)),
            int.class, new ArrayList<>(List.of(init)),
            PersonToIntFunction.class, new ArrayList<>(List.of(personToIntFunction))
        ));
    }

    static PersonFilter personFilter() {
        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodType methodType = MethodType.methodType(boolean.class, TestUtils.getPersonClass("Person"));
            final CallSite site = LambdaMetafactory.metafactory(lookup,
                "test",
                MethodType.methodType(TestUtils.getPersonClass("PersonFilter")),
                methodType,
                lookup.findStatic(TestUtils.class, "postalCodeEquals1",
                    MethodType.methodType(boolean.class, TestUtils.getPersonClass("Person"))),
                methodType);
            return (PersonFilter) site.getTarget().invokeExact();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException();
        }
    }

    private static boolean postalCodeEquals1(Person p) {
        return p.getPostalCode() == 3;
    }

    static PersonToIntFunction personToIntFunction() {
        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodType methodType = MethodType.methodType(int.class, TestUtils.getPersonClass("Person"));
            final CallSite site = LambdaMetafactory.metafactory(lookup,
                "apply",
                MethodType.methodType(TestUtils.getPersonClass("PersonToIntFunction")),
                methodType,
                lookup.findStatic(TestUtils.class, "personIntProduct",
                    MethodType.methodType(int.class, TestUtils.getPersonClass("Person"))),
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

    static Method getHigherOrderMethod(Class<?> clazz, String name) {
        try {
            return Arrays.stream(clazz.getMethods()).filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .findAny().orElseThrow(() -> new NoSuchMethodException("Keine Klassenmethode mit Namen: " + name));
        } catch (NoSuchMethodException e) {
            fail("Methode mit Namen " + name + " konnte in der Klasse " + clazz.getSimpleName() + " nicht gefunden werden.");
            throw new RuntimeException(e);
        }
    }

    static Object invokeHigherOrderMethod(Method method, Traits traits, Object fct, Object... inputs) {
        try {
            Function<Parameter, Object> lookup = p -> {
                if (p.getType().equals(Traits.class)) {
                    return traits;
                } else {
                    var match = Arrays.stream(inputs).filter(o -> o.getClass().equals(p.getType()) ||
                        (o.getClass().equals(Integer.class) && p.getType().equals(int.class))).findAny();
                    return match.orElse(fct);
                }
            };
            return method.invoke(null, Arrays.stream(method.getParameters()).map(lookup).toArray(Object[]::new));
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Methode " + method.toGenericString() + " konnte nicht erfolgreich ausgeführt werden.");
            throw new RuntimeException(e);
        }
    }

    static Traits makeTraits(Map<Class<?>, List<?>> objects) {
        try {
            var traits = TestUtils.getPersonClass("Traits");
            var consOpt = Arrays.stream(traits.getConstructors()).filter(c -> c.getParameterCount() >= objects.size()).findAny();
            if (consOpt.isEmpty()) {
                throw new NoSuchMethodException();
            }
            Function<Class<?>, Object> lookUpWithUsed = p -> {
                if (objects.containsKey(p)) {
                    var list = objects.get(p);
                    if (!list.isEmpty()) {
                        return list.remove(0);
                    }
                }
                return null;
            };
            IntBinaryOperator acc = (IntBinaryOperator) lookUpWithUsed.apply(IntBinaryOperator.class);
            Integer init = (Integer) lookUpWithUsed.apply(int.class);
            int initInt = init == null ? 0 : init;
            PersonToIntFunction personToIntFunction = (PersonToIntFunction) lookUpWithUsed.apply(PersonToIntFunction.class);
            PersonFilter personFilter = (PersonFilter) lookUpWithUsed.apply(PersonFilter.class);
            IntBinaryOperator combine = (IntBinaryOperator) lookUpWithUsed.apply(IntBinaryOperator.class);
            var p = consOpt.get().getParameters();
            Object[] arguments;
            if (p.length == 5) {
                arguments = new Object[]{
                    p[0].getType().equals(IntBinaryOperator.class) ? acc : p[0].getType().equals(int.class) ? initInt : p[0].getType().equals(PersonToIntFunction.class) ? personToIntFunction : personFilter,
                    p[1].getType().equals(IntBinaryOperator.class) ? acc : p[1].getType().equals(int.class) ? initInt : p[1].getType().equals(PersonToIntFunction.class) ? personToIntFunction : personFilter,
                    p[2].getType().equals(IntBinaryOperator.class) ? acc : p[2].getType().equals(int.class) ? initInt : p[2].getType().equals(PersonToIntFunction.class) ? personToIntFunction : personFilter,
                    p[3].getType().equals(IntBinaryOperator.class) ? acc : p[3].getType().equals(int.class) ? initInt : p[3].getType().equals(PersonToIntFunction.class) ? personToIntFunction : personFilter,
                    p[4].getType().equals(IntBinaryOperator.class) ? combine : p[4].getType().equals(int.class) ? initInt : p[4].getType().equals(PersonToIntFunction.class) ? personToIntFunction : personFilter
                };
            } else {
                arguments = new Object[]{
                    p[0].getType().equals(IntBinaryOperator.class) ? acc : p[0].getType().equals(int.class) ? initInt : p[0].getType().equals(PersonToIntFunction.class) ? personToIntFunction : personFilter,
                    p[1].getType().equals(IntBinaryOperator.class) ? acc : p[1].getType().equals(int.class) ? initInt : p[1].getType().equals(PersonToIntFunction.class) ? personToIntFunction : personFilter,
                    p[2].getType().equals(IntBinaryOperator.class) ? acc : p[2].getType().equals(int.class) ? initInt : p[2].getType().equals(PersonToIntFunction.class) ? personToIntFunction : personFilter,
                    p[3].getType().equals(IntBinaryOperator.class) ? acc : p[3].getType().equals(int.class) ? initInt : p[3].getType().equals(PersonToIntFunction.class) ? personToIntFunction : personFilter
                };
            }
            return (Traits) consOpt.get().newInstance(arguments);


        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Mit den Objekten " + objects.keySet() + " konnte keine Traits Klasse erstellt werden.", e);
            throw new RuntimeException(e);
        }
    }
}
