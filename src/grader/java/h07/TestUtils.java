package h07;

//import h07.person.Person;
//import h07.person.PersonFilter;
//import h07.person.PersonToIntFunction;
//import h07.person.Traits;

import reflection.MethodTester;
import reflection.ParameterMatcher;
import student.PersonFilter;
import student.PersonToIntFunction;
import student.Person_STUD;
import student.Traits;
import tutor.Mocked;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.*;

public class TestUtils {
    static int[] mapped = {9, 6, 2, 2, 2, 4, 4, 64289, 64289, 6};

    static Object personGet(Class<?> clazz, Object person, String name) {
        try {
            var getter = stream(clazz.getMethods()).filter(me -> me.getName().toLowerCase()
                .contains(name.toLowerCase()) && me.getName().contains("get")).findAny().orElseThrow(NoSuchMethodException::new);
            return getter.invoke(person);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            fail("Attribut " + name + " eines Person-Objekts konnte nicht bestimmt werden  ");
            throw new RuntimeException(e);
        }
    }

    static Object get(Class<?> clazz, Object traits, String name) {
        try {
            var getter = stream(clazz.getMethods()).filter(me -> me.getName().toLowerCase()
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
        return new Person_STUD(lastName, firstName, street, houseNumber, postalCode).getActualObject();
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
        var result = Array.newInstance(Person_STUD.cPerson().getActualClass(), init.length);
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
        var result = Array.newInstance(Person_STUD.cPerson().getActualClass(), init.length);
        for (var i = 0; i < init.length; i++) {
            Array.set(result, i, init[i]);
        }
        return result;
    }

    static void assertPeopleEquals(Object[] filtered, Object[] result) {
        assertEquals(filtered.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertPersonEquals(filtered[i], result[i]);
        }
    }

    static void assertPersonEquals(Object p1, Object p2) {
        var person1 = new Person_STUD(p1);
        var person2 = new Person_STUD(p1);
        assertEquals(person1.getHouseNumber(), person2.getHouseNumber());
        assertEquals(person1.getPostalCode(), person2.getPostalCode());
        assertEquals(person1.getFirstName(), person2.getFirstName());
        assertEquals(person1.getLastName(), person2.getLastName());
        assertEquals(person1.getStreet(), person2.getStreet());
    }

    static Traits.Mock getTraitsObject(boolean withCombine) {
        var personFilter = personFilter();
        var personToIntFunction = personToIntFunction();
        IntBinaryOperator op = (a, b) -> a + b + 1;
        IntBinaryOperator combine = (a, b) -> a * b / 7;
        int init = 357;
        return makeTraits(Map.of(
            PersonFilter.class, new ArrayList<>(List.of(personFilter)),
            IntBinaryOperator.class, new ArrayList<>(List.of(op, combine)),
            int.class, new ArrayList<>(List.of(init)),
            PersonToIntFunction.class, new ArrayList<>(List.of(personToIntFunction))
        ));
    }

    static PersonFilter.Mock personFilter() {
        return new PersonFilter.Mock((Person_STUD s) -> s.getPostalCode() == 3);
    }

    private static boolean postalCodeEquals3(Person_STUD p) {
        return p.getPostalCode() == 3;
    }

    static PersonToIntFunction.Mock personToIntFunction() {
        return new PersonToIntFunction.Mock(s -> s.getPostalCode() * s.getHouseNumber());
    }

    private static int personIntProduct(Person_STUD p) {
        return p.getPostalCode() * p.getHouseNumber();
    }

    static Method getHigherOrderMethod(Class<?> clazz, String name) {
        try {
            return stream(clazz.getMethods()).filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .findAny().orElseThrow(() -> new NoSuchMethodException("Keine Klassenmethode mit Namen: " + name));
        } catch (NoSuchMethodException e) {
            fail("Methode mit Namen " + name + " konnte in der Klasse " + clazz.getSimpleName() + " nicht gefunden werden.");
            throw new RuntimeException(e);
        }
    }

    static Object invokeHigherOrderMethod(MethodTester method, Traits traits, Object fct, Object... inputs) {

        Function<ParameterMatcher, Object> lookup = p -> {
            if (p.parameterType.equals(Traits.Student.c().getActualClass())) {
                return traits;
            } else {
                var match = stream(inputs).map(Mocked::getActualObject).filter(o -> o.getClass().equals(p.parameterType) ||
                    (o.getClass().equals(Integer.class) && p.parameterType.equals(int.class))).findAny();
                return match.orElse(fct);
            }
        };
        return method.invoke(method.getClassTester().instantiate(), method.getParameters().stream().map(lookup).toArray(Object[]::new));
    }

    static Traits.Mock makeTraits(Map<Class<?>, List<?>> objects) {

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
        Integer initPre = (Integer) lookUpWithUsed.apply(int.class);
        int init = initPre == null ? 0 : initPre;
        PersonToIntFunction.Mock fct = (PersonToIntFunction.Mock) lookUpWithUsed.apply(PersonToIntFunction.class);
        PersonFilter.Mock pred = (PersonFilter.Mock) lookUpWithUsed.apply(PersonFilter.class);
        IntBinaryOperator combine = (IntBinaryOperator) lookUpWithUsed.apply(IntBinaryOperator.class);
        return new Traits.Mock(acc, init, fct, pred, combine);
    }

    public static Class<?> getArrayClass(Class<?> clazz, int dimensions) {
        return Array.newInstance(Person_STUD.cPerson().getActualClass(), new int[dimensions]).getClass();
    }
}
