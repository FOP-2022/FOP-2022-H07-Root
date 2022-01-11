package h07;

import h07.person.Person;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The JUnit tests for H2.
 */
@TestForSubmission("h07")
public class TutorTest_H2 {

    @Test
    public void personExist() {
        Class<?> person = TestUtils.getPersonClass("Person");

        assertTrue(Modifier.isPublic(person.getModifiers()),
            "Die Klasse Person soll öffentlich sein");
        assertFalse(Modifier.isInterface(person.getModifiers()),
            "Die Klasse Person soll kein Interface sein");

        Person p = null;
        for (Constructor<?> cons : person.getDeclaredConstructors()) {
            if (cons.getParameterCount() != 5) {
                continue;
            }
            assertEquals(5, cons.getParameterCount());
            try {
                p = (Person) cons.newInstance("l", "f", "s", 1, 2);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                fail("Der Konstruktor konnte nicht mit den Parametern [\"l\", \"f\", \"s\", 1, 2] aufgerufen werden");
            }
        }
        assertNotNull(p, "Der Konstruktor konnte nicht mit den Parametern [\"l\", \"f\", \"s\", 1, 2] aufgerufen werden");
        assertEquals("l", TestUtils.personGet(person, p, "lastName"));
        assertEquals("f", TestUtils.personGet(person, p, "firstName"));
        assertEquals("s", TestUtils.personGet(person, p, "street"));
        assertEquals(1, TestUtils.personGet(person, p, "houseNumber"));
        assertEquals(2, TestUtils.personGet(person, p, "postalCode"));
    }

    @Test
    public void personFilterExist() {
        Class<?> personFilter = TestUtils.getPersonClass("PersonFilter");
        Class<?> person = TestUtils.getPersonClass("Person");

        assertTrue(personFilter.isAnnotationPresent(FunctionalInterface.class),
            "Die Klasse PersonFilter soll ein FunctionalInterface sein");
        assertTrue(Modifier.isPublic(personFilter.getModifiers()),
            "Die Klasse PersonFilter soll öffentlich sein");

        Method method = null;
        try {
            method = personFilter.getMethod("test", person);
        } catch (NoSuchMethodException e) {
            fail("Die Methode PersonFilter.test konnte nicht gefunden werden");
        }
    }

    @Test
    public void personToIntFunctionExist() {
        Class<?> personToIntFunction = TestUtils.getPersonClass("PersonToIntFunction");
        Class<?> person = TestUtils.getPersonClass("Person");

        assertTrue(personToIntFunction.isAnnotationPresent(FunctionalInterface.class),
            "Die Klasse PersonToIntFunction soll ein FunctionalInterface sein");
        assertTrue(Modifier.isPublic(personToIntFunction.getModifiers()),
            "Die Klasse PersonToIntFunction soll öffentlich sein");

        Method method = null;
        try {
            method = personToIntFunction.getMethod("apply", person);
        } catch (NoSuchMethodException e) {
            fail("Die Methode PersonFilter.apply konnte nicht gefunden werden");
        }
    }

    @Test
    public void traitsExist() {
        Class<?> traits = TestUtils.getPersonClass("Traits");

        assertEquals(Object.class, traits.getSuperclass(),
            "Die Klasse Traits soll lediglich von Object erben");
        assertTrue(Modifier.isPublic(traits.getModifiers()),
            "Die Klasse Traits soll öffentlich sein");
        assertFalse(Modifier.isAbstract(traits.getModifiers()),
            "Die Klasse Traits soll nicht abstrakt sein");
    }

    @Test
    public void traitsCorrect() {
        Class<?> traits = TestUtils.getPersonClass("Traits");

        assertEquals(Object.class, traits.getSuperclass(),
            "Die Klasse Traits soll lediglich von Object erben");
        assertTrue(Modifier.isPublic(traits.getModifiers()),
            "Die Klasse Traits soll öffentlich sein");
        assertFalse(Modifier.isAbstract(traits.getModifiers()),
            "Die Klasse Traits soll nicht abstrakt sein");
        var expectedFields = List.of("op", "init", "fct", "pred");
        var expectedFieldClasses = List.of(IntBinaryOperator.class, int.class,
            TestUtils.getPersonClass("PersonToIntFunction"), TestUtils.getPersonClass("PersonFilter"));
        var actualFields = Arrays.stream(traits.getDeclaredFields())
            .map(Field::getName).filter(fn -> !fn.equals("combine"))
            .collect(Collectors.toList());
        assertTrue(actualFields.containsAll(expectedFields), "Die Attribute [op, init, fct, pred] "
            + "sollten alle in Traits vorhanden sein.");
        TestUtils.forEach(expectedFields, expectedFieldClasses, (f, c) -> fieldAndGetterCorrect(traits, f, c));

        var consOpt = Arrays.stream(traits.getConstructors()).filter(c -> c.getParameterCount() >= 5).findAny();
        if (consOpt.isEmpty()) {
            fail("Traits hat keinen geeigneten Konstruktor");
        }
        Object traitsObj;
        try {
            var cons = consOpt.get();
            IntBinaryOperator op = (a, b) -> a + b + 1;
            var personToIntFunction = TestUtils.personToIntFunction();
            var personFilter = TestUtils.personFilter();
            if (cons.getParameterCount() == 5) {
                traitsObj = cons.newInstance(op, 357, personToIntFunction, personFilter, null);
            } else {
                traitsObj = cons.newInstance(op, 357, personToIntFunction, personFilter);
            }
            assertEquals(op, TestUtils.get(traits, traitsObj, "op"), "Op-Getter falsch in Traits");
            assertEquals(personToIntFunction, TestUtils.get(traits, traitsObj, "fct"), "Fct-Getter falsch in Traits");
            assertEquals(personFilter, TestUtils.get(traits, traitsObj, "pred"), "Pred-Getter falsch in Traits");
            assertEquals(357, TestUtils.get(traits, traitsObj, "init"), "Init-Getter falsch in Traits");
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            fail("Traits-Objekt konnte nicht richtig erstellt oder verwendet werden", e);
        }
    }

    // Only for non-boolean getters
    private void fieldAndGetterCorrect(Class<?> clazz, String field, Class<?> expectedFieldClass) {
        var f = TestUtils.getField(clazz, field);
        assertTrue(Modifier.isFinal(f.getModifiers()),
            "Field " + field + " sollte final sein.");
        assertTrue(Modifier.isPrivate(f.getModifiers()),
            "Field " + field + " sollte private sein.");
        assertEquals(expectedFieldClass, f.getType(),
            "Field " + field + " sollte den Typ " + expectedFieldClass.getSimpleName() + " haben.");
        var getter = TestUtils.getMethod(clazz,
            "get" + field.substring(0, 1).toUpperCase() + field.substring(1));
        assertTrue(Modifier.isPublic(getter.getModifiers()),
            "Getter von " + field + " sollte public sein.");
        assertEquals(expectedFieldClass, getter.getReturnType(),
            "Getter von " + field + " Objekt von Typ " + expectedFieldClass.getSimpleName() + " zurückgeben.");
    }

    @Test
    public void functionWithFilterMapAndFoldExists() {
        final Class<?> fwfmf = TestUtils.getPersonClass("FunctionWithFilterMapAndFold");
        final Class<?> traits = TestUtils.getPersonClass("Traits");

        assertEquals(Object.class, fwfmf.getSuperclass(),
            "Die Klasse FunctionWithFilterMapAndFold soll lediglich von Object erben");
        assertTrue(Modifier.isPublic(fwfmf.getModifiers()),
            "Die Klasse FunctionWithFilterMapAndFold soll öffentlich sein");
        assertTrue(Modifier.isAbstract(fwfmf.getModifiers()),
            "Die Klasse FunctionWithFilterMapAndFold soll abstrakt sein");
        final var f = TestUtils.getField(fwfmf, "traits");
        assertTrue(Modifier.isFinal(f.getModifiers()));
        assertTrue(Modifier.isProtected(f.getModifiers()));
        assertEquals(traits, f.getType());

        final var cons = Arrays.stream(fwfmf.getConstructors())
            .filter(c -> c.getParameterCount() == 1 && c.getParameters()[0].getType().equals(traits))
            .findAny().orElse(null);
        if (cons == null) {
            fail("Kein geeigneter Konstuktor von FunctionWithFilterMapAndFold konnte gefunden werden.");
        }
        assertTrue(Modifier.isPublic(cons.getModifiers()));

        final var apply = TestUtils.getMethod(fwfmf, "apply", Person[].class);
        assertTrue(Modifier.isPublic(apply.getModifiers()));
        assertTrue(Modifier.isAbstract(apply.getModifiers()));
        assertEquals(int.class, apply.getReturnType());

        final Class<?> fwfmf1 = TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold1");

        assertEquals(fwfmf, fwfmf1.getSuperclass(),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll von FunctionWithFilterMapAndFold erben");
        assertTrue(Modifier.isPublic(fwfmf1.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll öffentlich sein");
        assertFalse(Modifier.isAbstract(fwfmf1.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll nicht abstrakt sein");

        final Class<?> fwfmf2 = TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold2");

        assertEquals(fwfmf, fwfmf2.getSuperclass(),
            "Die Klasse MyFunctionWithFilterMapAndFold2 soll von FunctionWithFilterMapAndFold erben");
        assertTrue(Modifier.isPublic(fwfmf2.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold2 soll öffentlich sein");
        assertFalse(Modifier.isAbstract(fwfmf2.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold2 soll nicht abstrakt sein");
    }

    @Test
    public void myFunctionWithFilterMapAndFold1Partly() {

        var erroneous = determineWrongFilterMapFold();

        assertTrue(erroneous.size() <= 1, "Bei MyFunctionWithFilterMapAndFold1"
            + " schlugen folgende Funktionen fehl: " + erroneous);
    }

    private List<String> determineWrongFilterMapFold() {
        List<String> erroneous = new ArrayList<>();

        var classUT = TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold1");
        assertTrue(Modifier.isPublic(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll öffentlich sein");
        assertFalse(Modifier.isAbstract(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll nicht abstrakt sein");
        try {
            var filter = TestUtils.getMethod(classUT, "filter",
                TestUtils.getPersonClass("PersonFilter"),
                Array.newInstance(TestUtils.getPersonClass("Person"), 0).getClass());
            var personFilter = TestUtils.personFilter();
            var actual = TestUtils.invokeMethod(filter, null, personFilter, TestUtils.people());
            TestUtils.assertPeopleEquals((Object[]) TestUtils.filtered(), (Object[]) actual);
        } catch (Throwable t) {
            erroneous.add("Filter[" + t.getMessage() + "]");
        }

        try {
            var map = TestUtils.getMethod(classUT, "map",
                TestUtils.getPersonClass("PersonToIntFunction"),
                Array.newInstance(TestUtils.getPersonClass("Person"), 0).getClass());
            var personToIntFunction = TestUtils.personToIntFunction();
            var actual = TestUtils.invokeMethod(map, null, personToIntFunction, TestUtils.people());
            assertArrayEquals(TestUtils.mapped, (int[]) actual);
        } catch (Throwable t) {
            erroneous.add("Map[" + t.getMessage() + "]");
        }

        try {
            var foldl = TestUtils.getMethod(classUT, "foldl",
                IntBinaryOperator.class,
                int.class,
                int[].class);
            List<IntBinaryOperator> functions = List.of(Integer::sum, Integer::sum, Math::max);
            List<Integer> inits = List.of(0, 5, 5);
            List<Integer> expected = List.of(128613, 128618, 64289);
            TestUtils.forEach(functions, inits, expected, (f, init, result) -> {
                var actual = TestUtils.invokeMethod(foldl, null, f, init, TestUtils.mapped);
                assertEquals(result, actual,
                    String.format("Für Eingabe %s, Init %d und %s folgt bei foldl nicht der erwartete Wert %d sondern %s .",
                        Arrays.toString(TestUtils.mapped),
                        init,
                        result < 0 ? "Funktion: Math::max" : "Addition",
                        result,
                        actual
                    )
                );
            });
        } catch (Throwable t) {
            erroneous.add("Foldl[" + t.getMessage() + "]");
        }
        return erroneous;
    }

    @Test
    public void myFunctionWithFilterMapAndFold1All() {
        var erroneous = determineWrongFilterMapFold();
        assertEquals(0, erroneous.size(), "Bei MyFunctionWithFilterMapAndFold1"
            + " schlugen folgende Funktionen fehl: " + erroneous);
        var classUT = TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold1");
        assertTrue(Modifier.isPublic(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll öffentlich sein");
        assertFalse(Modifier.isAbstract(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll nicht abstrakt sein");
        var apply = TestUtils.getMethod(classUT, "apply",
            Array.newInstance(TestUtils.getPersonClass("Person"), 0).getClass());
        var traitsObj = TestUtils.getTraitsObject(false);
        Object uut;
        try {
            uut = classUT.getConstructor(TestUtils.getPersonClass("Traits")).newInstance(traitsObj);
            var actual = TestUtils.invokeMethod(apply, uut, TestUtils.filtered());
            assertEquals(381, actual);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Der Konstruktor von MyFunctionWithFilterMapAndFold1 konnte nicht mit einem Traits Objekt aufgerufen werden");
        }
    }

    @Test
    public void myFunctionWithFilterMapAndFoldCorrect2() {
        var classUT = TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold2");
        assertTrue(Modifier.isPublic(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold2 soll öffentlich sein");
        assertFalse(Modifier.isAbstract(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold2 soll nicht abstrakt sein");
        var traitsObj = TestUtils.getTraitsObject(false);
        Object uut;
        var apply = TestUtils.getMethod(classUT, "apply",
            Array.newInstance(TestUtils.getPersonClass("Person"), 0).getClass());
        try {
            uut = classUT.getConstructor(TestUtils.getPersonClass("Traits")).newInstance(traitsObj);
            var actual = TestUtils.invokeMethod(apply, uut, TestUtils.filtered());
            assertEquals(381, actual);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Der Konstruktor von MyFunctionWithFilterMapAndFold2 konnte nicht mit einem Traits Objekt aufgerufen werden");
        }
    }

    @Test
    public void functionCreatorExistsWithActive() {
        var classUT = TestUtils.getPersonClass("PersonFunctionFactory");
        try {
            var field = classUT.getDeclaredField("firstImplementationActive");
            assertTrue(Modifier.isPrivate(field.getModifiers()), "firstImplementationActive sollte private sein");
            assertTrue(Modifier.isStatic(field.getModifiers()), "firstImplementationActive sollte static sein");
            assertFalse(Modifier.isFinal(field.getModifiers()), "firstImplementationActive sollte nicht final sein");
        } catch (NoSuchFieldException e) {
            fail("Attribut firstImplementationActive von PersonFunctionFactory konnte nicht gefunden werden.");
        }
        try {
            var getter = classUT.getDeclaredMethod("isFirstImplementationActive");
            var setter = classUT.getDeclaredMethod("setFirstImplementationActive", boolean.class);
            assertTrue(Modifier.isPublic(getter.getModifiers()), "isFirstImplementationActive sollte public sein");
            assertTrue(Modifier.isStatic(getter.getModifiers()), "isFirstImplementationActive sollte static sein");
            assertTrue(Modifier.isPublic(setter.getModifiers()), "setFirstImplementationActive sollte public sein");
            assertTrue(Modifier.isStatic(setter.getModifiers()), "setFirstImplementationActive sollte static sein");
            setter.invoke(null, true);
            assertTrue((Boolean) getter.invoke(null), "Getter isFirstImplementationActive sollte true zurückgeben,"
                + " nachdem setFirstImplementationActive(true) aufgerufen wurde.");
            setter.invoke(null, false);
            assertFalse((Boolean) getter.invoke(null), "Getter isFirstImplementationActive sollte false zurückgeben,"
                + " nachdem setFirstImplementationActive(false) aufgerufen wurde.");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Getter oder Setter von firstImplementationActive konnte nicht erfolgreich verwendet werden.", e);
        }
    }

    @Test
    public void createFunctionWithFilterMapAndFoldCreatorCorrect() {
        var classUT = TestUtils.getPersonClass("PersonFunctionFactory");
        var traits = TestUtils.getPersonClass("Traits");
        try {
            var setter = classUT.getDeclaredMethod("setFirstImplementationActive", boolean.class);
            var create = classUT.getDeclaredMethod("createFunctionWithFilterMapAndFold", traits);
            var traitsObj = TestUtils.getTraitsObject(false);
            setter.invoke(null, true);
            var fct = create.invoke(null, traitsObj);
            assertEquals(fct.getClass(), TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold1"));
            var field = fct.getClass().getSuperclass().getDeclaredField("traits");
            field.setAccessible(true);
            assertSame(traitsObj, field.get(fct),
                "Traits vom MyFunctionWithFilterMapAndFold1-Objekt ist nicht das Gleiche wie das im Konstruktor übergebene.");
            setter.invoke(null, false);
            var fct2 = create.invoke(null, traitsObj);
            assertEquals(fct2.getClass(), TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold2"));
            var field2 = fct2.getClass().getSuperclass().getDeclaredField("traits");
            field2.setAccessible(true);
            assertSame(traitsObj, field2.get(fct2),
                "Traits vom MyFunctionWithFilterMapAndFold2-Objekt ist nicht das Gleiche wie das im Konstruktor übergebene.");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            fail("Beim Erstellen einer Funktion mit createFunctionWithFilterMapAndFoldCreator"
                + "konnte nicht erfolgreich verwendet werden.", e);
        }
    }

    @Test
    public void createStrangeFunctionCorrect() {
        final var classUT = TestUtils.getPersonClass("PersonFunctionFactory");
        final var traits = TestUtils.getPersonClass("Traits");
        final var personFilterClass = TestUtils.getPersonClass("PersonFilter");
        final var personToIntFunctionClass = TestUtils.getPersonClass("PersonToIntFunction");
        final var personClass = TestUtils.getPersonClass("Person");
        try {
            final var createCreateStrangeFunction = classUT.getDeclaredMethod("createStrangeFunction", String.class);
            final var fct = createCreateStrangeFunction.invoke(null, "cake");
            final var field = fct.getClass().getSuperclass().getDeclaredField("traits");
            field.setAccessible(true);
            final var traitsObj = field.get(fct);
            assertEquals(357, TestUtils.get(traits, traitsObj, "init"), "createStrangeFunctions Init sollte 357 sein");
            final var personFilter = TestUtils.get(traits, traitsObj, "pred");
            final var intOperator = (IntBinaryOperator) TestUtils.get(traits, traitsObj, "op");
            final var personToIntFunction = TestUtils.get(traits, traitsObj, "fct");
            assertEquals(5, intOperator.applyAsInt(1, 3), "Op sollte  a + b + 1  entsprechen");
            assertEquals(-3, intOperator.applyAsInt(3, -7), "Op sollte  a + b + 1  entsprechen");
            final var person1 = TestUtils.makePerson("cake", "vincent", "baker street", 2, 234);
            final var person2 = TestUtils.makePerson("muffin", "alex", "baker street", 56, 120);
            assertTrue((Boolean) personFilterClass.getDeclaredMethod("test", personClass).invoke(personFilter, person1),
                "Pred sollte  p -> p.lastName.equals(<parameter>)  entsprechen");
            assertFalse((Boolean) personFilterClass.getDeclaredMethod("test", personClass).invoke(personFilter, person2),
                "Pred sollte  p -> p.lastName.equals(<parameter>)  entsprechen");
            assertEquals(234,
                personToIntFunctionClass.getDeclaredMethod("apply", personClass).invoke(personToIntFunction, person1),
                "Fct sollte  p -> p.postalCode  entsprechen");
            assertEquals(120,
                personToIntFunctionClass.getDeclaredMethod("apply", personClass).invoke(personToIntFunction, person2),
                "Fct sollte  p -> p.postalCode  entsprechen");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            fail("Beim Testen von createStrangeFunction konnte eine notwendige Operation nicht durchgeführt werden", e);
        }
    }

    @Test
    public void myFunctionWithAdjacentExistsAndCombineInTraits() {
        final Class<?> classUT = TestUtils.getPersonClass("MyFunctionWithAdjacent");
        final Class<?> traits = TestUtils.getPersonClass("Traits");
        final Class<?> fwfmf = TestUtils.getPersonClass("FunctionWithFilterMapAndFold");

        assertEquals(fwfmf, classUT.getSuperclass(),
            "Die Klasse MyFunctionWithAdjacent soll von FunctionWithFilterMapAndFold erben");
        assertTrue(Modifier.isPublic(classUT.getModifiers()),
            "Die Klasse MyFunctionWithAdjacent soll öffentlich sein");
        assertFalse(Modifier.isAbstract(classUT.getModifiers()),
            "Die Klasse MyFunctionWithAdjacent soll nicht abstrakt sein");
        var cons = Arrays.stream(classUT.getConstructors())
            .filter(c -> c.getParameterCount() == 1 && c.getParameters()[0].getType().equals(traits))
            .findAny().orElse(null);
        if (cons == null) {
            fail("Kein geeigneter Konstuktor von FunctionWithFilterMapAndFold konnte gefunden werden.");
        }
        assertTrue(Modifier.isPublic(cons.getModifiers()));

        var apply = TestUtils.getMethod(classUT, "apply", Person[].class);
        assertTrue(Modifier.isPublic(apply.getModifiers()));
        assertFalse(Modifier.isAbstract(apply.getModifiers()));
        assertEquals(int.class, apply.getReturnType());

        var consOpt = Arrays.stream(traits.getConstructors()).filter(c -> c.getParameterCount() >= 5).findAny();
        if (consOpt.isEmpty()) {
            fail("Traits hat keinen geeigneten Konstruktor mit 5 Parametern");
        }
        Object traitsObj;
        try {
            var traitsCons = consOpt.get();
            IntBinaryOperator op = (a, b) -> a + b + 1;
            var personToIntFunction = TestUtils.personToIntFunction();
            var personFilter = TestUtils.personFilter();
            IntBinaryOperator combine = (a, b) -> a * b / 79;
            traitsObj = traitsCons.newInstance(op, 357, personToIntFunction, personFilter, combine);
            assertEquals(op, TestUtils.get(traits, traitsObj, "op"), "Op-Getter falsch in Traits");
            assertEquals(personToIntFunction, TestUtils.get(traits, traitsObj, "fct"), "Fct-Getter falsch in Traits");
            assertEquals(personFilter, TestUtils.get(traits, traitsObj, "pred"), "Pred-Getter falsch in Traits");
            assertEquals(357, TestUtils.get(traits, traitsObj, "init"), "Init-Getter falsch in Traits");
            assertEquals(combine, TestUtils.get(traits, traitsObj, "combine"), "Combine-Getter falsch in Traits");
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            fail("Traits-Objekt konnte nicht richtig erstellt oder verwendet werden", e);
        }
    }

    @Test
    public void myFunctionWithAdjacentCorrect() {
        var classUT = TestUtils.getPersonClass("MyFunctionWithAdjacent");
        assertTrue(Modifier.isPublic(classUT.getModifiers()),
            "Die Klasse MyFunctionWithAdjacent soll öffentlich sein");
        assertFalse(Modifier.isAbstract(classUT.getModifiers()),
            "Die Klasse MyFunctionWithAdjacent soll nicht abstrakt sein");
        var traitsObj = TestUtils.getTraitsObject(true);
        Object uut;
        var apply = TestUtils.getMethod(classUT, "apply",
            Array.newInstance(TestUtils.getPersonClass("Person"), 0).getClass());
        try {
            uut = classUT.getConstructor(TestUtils.getPersonClass("Traits")).newInstance(traitsObj);
            var actual = TestUtils.invokeMethod(apply, uut, TestUtils.people());
            assertEquals(371, actual);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Der Konstruktor von MyFunctionWithFilterMapAndFold1 konnte nicht mit einem Traits Objekt aufgerufen werden");
        }
    }

    @Test
    public void distanceCorrect() {
        final var classUT = TestUtils.getPersonClass("PersonFunctionFactory");
        final var fctClass = TestUtils.getPersonClass("MyFunctionWithAdjacent");
        final var traits = TestUtils.getPersonClass("Traits");
        final var personFilterClass = TestUtils.getPersonClass("PersonFilter");
        final var personToIntFunctionClass = TestUtils.getPersonClass("PersonToIntFunction");
        final var personClass = TestUtils.getPersonClass("Person");
        try {
            final var createCreateStrangeFunction = classUT.getDeclaredMethod("distance");
            final var fct = createCreateStrangeFunction.invoke(null);
            assertEquals(fctClass, fct.getClass());
            final var field = fct.getClass().getSuperclass().getDeclaredField("traits");
            field.setAccessible(true);
            final var traitsObj = field.get(fct);
            assertEquals(0, TestUtils.get(traits, traitsObj, "init"), "distance: Init sollte 357 sein");
            final var personFilter = TestUtils.get(traits, traitsObj, "pred");
            final var intOperator = (IntBinaryOperator) TestUtils.get(traits, traitsObj, "op");
            final var combine = (IntBinaryOperator) TestUtils.get(traits, traitsObj, "combine");
            final var personToIntFunction = TestUtils.get(traits, traitsObj, "fct");
            assertEquals(4, intOperator.applyAsInt(1, 3), "Op sollte  a + b  entsprechen");
            assertEquals(-4, intOperator.applyAsInt(3, -7), "Op sollte  a + b  entsprechen");
            final var person1 = TestUtils.makePerson("cake", "vincent", "baker street", 2, 234);
            final var person2 = TestUtils.makePerson("muffin", "alex", "baker street", 56, 64289);
            assertTrue((Boolean) personFilterClass.getDeclaredMethod("test", personClass).invoke(personFilter, person1),
                "Pred sollte  p -> p.postalCode != 64289  entsprechen");
            assertFalse((Boolean) personFilterClass.getDeclaredMethod("test", personClass).invoke(personFilter, person2),
                "Pred sollte  p -> p.postalCode != 64289  entsprechen");
            assertEquals(234,
                personToIntFunctionClass.getDeclaredMethod("apply", personClass).invoke(personToIntFunction, person1),
                "Fct sollte  p -> p.postalCode  entsprechen");
            assertEquals(64289,
                personToIntFunctionClass.getDeclaredMethod("apply", personClass).invoke(personToIntFunction, person2),
                "Fct sollte  p -> p.postalCode  entsprechen");
            assertEquals(20,
                combine.applyAsInt(20, 40),
                "Combine sollte  (a, b) -> Math.abs(a - b)  entsprechen");
            assertEquals(45,
                combine.applyAsInt(34, -11),
                "Combine sollte  (a, b) -> Math.abs(a - b)  entsprechen");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            fail("Beim Testen von createStrangeFunction konnte eine notwendige Operation nicht durchgeführt werden", e);
        }
    }
}
