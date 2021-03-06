package reflection;

import javax.management.RuntimeErrorException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static tutor.Utils.Messages.wasNotCalledRecursively;
import static tutor.Utils.TestCollection.test;

import org.mockito.invocation.Invocation;
import spoon.Launcher;
import spoon.reflect.code.CtCodeElement;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.filter.TypeFilter;
import tutor.Mocked;

/**
 * A Method Tester
 *
 * @author Ruben Deisenroth
 */
public class MethodTester {
    /**
     * whether to also match super implementations
     */
    public boolean allowSuperClass;
    /**
     * The Method-Identifier
     */
    IdentifierMatcher methodIdentifier;
    /**
     * The resolved Method that will be tested
     */
    Executable theExecutable;
    /**
     * The Expected Access Modifier
     */
    int accessModifier;
    /**
     * The expected return Type
     */
    private Class<?> returnType;
    /**
     * The expected parameters
     */
    private ArrayList<ParameterMatcher> parameters;
    /**
     * A Class Tester (used for invoking)
     */
    private ClassTester<?> classTester;
    /**
     * Whether to allow derived return Types
     */
    private boolean looseReturnTypeChecking;

    /**
     * Generates a new {@link MethodTester}
     *
     * @param classTester    A Class Tester (used for invoking)
     * @param methodName     the expected method name
     * @param similarity     the minimum matching similarity
     * @param accessModifier The Expected Access Modifier
     * @param returnType     The expected return Type
     * @param parameters     The expected parameters
     */
    public MethodTester(ClassTester<?> classTester, String methodName, double similarity, int accessModifier, Class<?> returnType, List<ParameterMatcher> parameters, boolean allowSuperClass) {
        this.classTester = classTester;
        methodIdentifier = new IdentifierMatcher(methodName, similarity);
        this.accessModifier = accessModifier;
        this.returnType = returnType;
        this.parameters = new ArrayList<>(parameters);
        this.allowSuperClass = allowSuperClass;
    }

    /**
     * Generates a new {@link MethodTester}
     *
     * @param classTester    A Class Tester (used for invoking)
     * @param methodName     the expected method name
     * @param similarity     the minimum matching similarity
     * @param accessModifier The Expected Access Modifier
     * @param returnType     The expected return Type
     * @param parameters     The expected parameters
     */
    public MethodTester(ClassTester<?> classTester, String methodName, double similarity, int accessModifier, Class<?> returnType, List<ParameterMatcher> parameters) {
        this(classTester, methodName, similarity, accessModifier, returnType, parameters, false);
    }

    /**
     * Generates a new {@link MethodTester}
     *
     * @param classTester             A Class Tester (used for invoking)
     * @param methodName              the expected method name
     * @param similarity              the minimum matching similarity
     * @param accessModifier          The Expected Access Modifier
     * @param returnType              The expected return Type
     * @param parameters              The expected parameters
     * @param looseReturnTypeChecking whether or not to allow Derived return Types
     */
    public MethodTester(ClassTester<?> classTester, String methodName, double similarity, int accessModifier, Class<?> returnType, List<ParameterMatcher> parameters, boolean allowSuperClass, boolean looseReturnTypeChecking) {
        this(classTester, methodName, similarity, accessModifier, returnType, parameters, allowSuperClass);
        this.looseReturnTypeChecking = looseReturnTypeChecking;
    }

    /**
     * Generates a new {@link MethodTester}
     *
     * @param classTester    A Class Tester (used for invoking)
     * @param methodName     the expected method name
     * @param similarity     the minimum matching similarity
     * @param accessModifier The Expected Access Modifier
     * @param returnType     The expected return Type
     */
    public MethodTester(ClassTester<?> classTester, String methodName, double similarity, int accessModifier, Class<?> returnType) {
        this(classTester, methodName, similarity, accessModifier, returnType, null);
    }

    /**
     * Generates a new {@link MethodTester}
     *
     * @param classTester    A Class Tester (used for invoking)
     * @param methodName     the expected method name
     * @param similarity     the minimum matching similarity
     * @param accessModifier The Expected Access Modifier
     */
    public MethodTester(ClassTester<?> classTester, String methodName, double similarity, int accessModifier) {
        this(classTester, methodName, similarity, accessModifier, null, null);
    }

    /**
     * Generates a new {@link MethodTester}
     *
     * @param classTester A Class Tester (used for invoking)
     * @param methodName  the expected method name
     * @param similarity  the minimum matching similarity
     * @param returnType  The expected return Type
     * @param parameters  The expected parameters
     */
    public MethodTester(ClassTester<?> classTester, String methodName, double similarity, Class<?> returnType, ArrayList<ParameterMatcher> parameters) {
        this(classTester, methodName, similarity, -1, returnType, parameters);
    }

    /**
     * Generates a new {@link MethodTester}
     *
     * @param classTester A Class Tester (used for invoking)
     * @param methodName  the expected method name
     * @param similarity  the minimum matching similarity
     */
    public MethodTester(ClassTester<?> classTester, String methodName, double similarity) {
        this(classTester, methodName, similarity, -1, null);
    }

    /**
     * Generates a new {@link MethodTester}
     *
     * @param classTester A Class Tester (used for invoking)
     * @param methodName  the expected method name
     */
    public MethodTester(ClassTester<?> classTester, String methodName) {
        this(classTester, methodName, 1, -1, null);
    }

    /**
     * Generates a Message for an invalid return type
     *
     * @param methodName the Method name
     * @return the generated Message
     */
    public static String getInvalidReturnTypeMessage(String methodName) {
        return String.format("falscher R??ckgabetyp f??r Methode %s", methodName);
    }

    /**
     * Generates a Should Not Have Parameter Message
     *
     * @param methodName the Method name
     * @return the generated Message
     */
    public static String getShouldNotHaveParameterMessage(String methodName) {
        return String.format("Methode %s sollte keine Parameter haben.", methodName);
    }

    /**
     * Counts the matching Parameters
     *
     * @param expectedParametes the Expected Parameter List
     * @param actualParameters  the Actual Parameter List
     * @param ignoreNames       whether to ignore Parameter Names
     * @return the Amount of matching Parameters
     */
    public static int countMatchingParameters(ArrayList<ParameterMatcher> expectedParametes, ArrayList<Parameter> actualParameters, boolean ignoreNames) {
        int count = 0;
        for (int i = 0; i < expectedParametes.size(); i++) {
            var matcher = expectedParametes.get(i);
            var param = actualParameters.get(i);
            if (param.getType() != matcher.parameterType) {
                continue;
            }
            if (!ignoreNames && matcher.identifierName != null && matcher.similarity > 0) {
                if (TestUtils.similarity(matcher.identifierName, param.getName()) < matcher.similarity) {
                    continue;
                }
            }
            count++;
        }
        return count;
    }

    /**
     * Counts the matching Parameters
     *
     * @param m           The Method to verify
     * @param methodName  The expected Method name
     * @param parameters  the Expected Parameter List
     * @param ignoreNames whether to ignore Parameter Names
     * @return the Amount of matching Parameters
     */
    public static int countMatchingParameters(Executable m, String methodName, ArrayList<ParameterMatcher> parameters, boolean ignoreNames) {
        assertMethodNotNull(m, methodName);
        if (parameters == null || parameters.isEmpty()) {
            return 0;
        }
        return countMatchingParameters(parameters, new ArrayList<>(List.of(m.getParameters())), ignoreNames);
    }

    /**
     * assert that the Method Parameters match
     *
     * @param expectedParameters the expected Parameter List
     * @param actualParamters    the actual Parameter List
     * @param ignoreNames        whether to ignore Parameter Names
     */
    public static void assertParametersMatch(ArrayList<ParameterMatcher> expectedParameters, ArrayList<Parameter> actualParamters, boolean ignoreNames) {

        if (expectedParameters == null || expectedParameters.isEmpty()) {
            assertTrue(actualParamters == null || actualParamters.isEmpty(), "Es sollen keine Parameter vorhanden sein.");
        } else {
            for (int i = 0; i < expectedParameters.size(); i++) {
                var matcher = expectedParameters.get(i);
                assertTrue(i < actualParamters.size(), "Zu wenige Parameter.");
                var param = actualParamters.get(i);
                assertSame(matcher.parameterType, param.getType(), "Falscher Parametertyp an Index " + i + ".");
                if (!ignoreNames && param.isNamePresent() && matcher.identifierName != null && matcher.similarity > 0) {
                    assertTrue(TestUtils.similarity(matcher.identifierName, param.getName()) >= matcher.similarity, "Falscher Parametername. Erwartet: " + matcher.identifierName + ", Erhalten: " + param.getName());
                }
            }
            assertEquals(actualParamters.size(), expectedParameters.size(), "Die folgenden Parameter waren nicht gefrdert:" + actualParamters.subList(expectedParameters.size(), actualParamters.size()));
        }
    }

    /**
     * assert that the Method Parameters match
     *
     * @param m           The Method to verify
     * @param methodName  The expected Method name
     * @param parameters  the Expected Parameter List
     * @param ignoreNames whether to ignore Parameter Names
     */
    public static void assertParametersMatch(Executable m, String methodName, ArrayList<ParameterMatcher> parameters, boolean ignoreNames) {
        assertMethodNotNull(m, methodName);
        assertParametersMatch(parameters, new ArrayList<>(List.of(m.getParameters())), ignoreNames);
    }

    /**
     * Generates a Method not found Message
     *
     * @param methodName the expecteed Method name
     * @return the generated Message
     */
    public static String getMethodNotFoundMessage(String methodName) {
        return String.format("Methode %s existiert nicht.", methodName);
    }

    /**
     * Assert that a given method is not {@code null}
     *
     * @param m    the Method
     * @param name the expected Method name
     */
    public static void assertMethodNotNull(Executable m, String name) {
        assertNotNull(m, getMethodNotFoundMessage(name));
    }

    /**
     * Generates a Class tester null message
     *
     * @param methodName the expected Method name
     * @return the generated message
     */
    public static String getClassTesterNullMessage(String methodName) {
        return String.format("Fehlerhafter Test f??r Methode %s: Kein Klassentester gegeben.", methodName);
    }

    public static String safeArrayToString(Object... array) {
        var paramsString = "[]";
        if (array != null) {
            try {
                paramsString = Arrays.toString(array);
            } catch (Exception e) {

            }
        }
        return paramsString;
    }

    /**
     * Gets all Fields from a given Class and its superclasses recursively
     *
     * @param methods the fields so far (initially give it new ArrayList<>())
     * @param clazz   the Class to search
     * @return all Fields from a given Class and its superclasses recursively
     */
    private static ArrayList<Executable> getAllMethods(ArrayList<Executable> methods, Class<?> clazz) {
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));

        if (clazz.getSuperclass() != null) {
            getAllMethods(methods, clazz.getSuperclass());
        }

        return methods;
    }

    /**
     * Gets all Fields from a given Class and its superclasses recursively
     *
     * @param clazz the Class to search
     * @return all Fields from a given Class and its superclasses recursively
     */
    public static ArrayList<Executable> getAllMethods(Class<?> clazz) {
        return getAllMethods(new ArrayList<>(), clazz);
    }

    public static boolean isRecursive(List<CtElement> elements, CtMethod<?> methodToCall, int level) {
        if (level <= 0) {
            return false;
        }
        for (var e : elements) {
            if (e instanceof CtInvocation<?> method) {
                if (method.getExecutable().equals(methodToCall.getReference())) {
                    return true;
                }
                if (isRecursive(e.getDirectChildren(), methodToCall, level - 1)) {
                    return true;
                }
            } else if (e instanceof CtExecutableReference<?> exe) {
                if (exe.equals(methodToCall.getReference())) {
                    return true;
                }
                if (isRecursive(e.getDirectChildren(), methodToCall, level - 1)) {
                    return true;
                }
            } else if (isRecursive(e.getDirectChildren(), methodToCall, level)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChildRecursive(List<CtElement> elements, int level, Set<CtMethod<?>> methods) {
        for (var e : elements) {
            if (e instanceof CtInvocation<?> invo) {
                var any = methods.stream().filter(m -> invo.getExecutable().equals(m.getReference())).findAny().orElse(null);
                if (any == null) {
                    continue;
                }
                if (isRecursive(e.getDirectChildren(), any, level)) {
                    return true;
                }
            } else if (isChildRecursive(e.getDirectChildren(), level, methods)) {
                return true;
            }
        }
        return false;
    }

    public void checkDeclaration() {
        getClassTester().assureResolved();
        assureResolved();
        test()
            .add(this::assertParametersMatch)
            .add(this::assertReturnType)
            .add(this::assertAccessModifier)
            .run();
    }

    /**
     * returns the Value of {@link #classTester}
     *
     * @return the Value of {@link #classTester}
     */
    public ClassTester<?> getClassTester() {
        return classTester;
    }

    /**
     * Set {@link #classTester} to the given value
     *
     * @param classTester the new Class Tester
     */
    public void setClassTester(ClassTester<?> classTester) {
        this.classTester = classTester;
    }

    /**
     * returns the Value of {@link #methodIdentifier}
     *
     * @return the Value of {@link #methodIdentifier}
     */
    public IdentifierMatcher getMethodIdentifier() {
        return methodIdentifier;
    }

    /**
     * Set {@link #methodIdentifier} to the given value
     */
    public void setMethodIdentifier(IdentifierMatcher methodIdentifier) {
        this.methodIdentifier = methodIdentifier;
    }

    /**
     * returns the Value of {@link #returnType}
     *
     * @return the Value of {@link #returnType}
     */
    public Class<?> getReturnType() {
        return returnType;
    }

    /**
     * Set {@link #returnType} to the given value
     */
    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    /**
     * returns true if {@link #looseReturnTypeChecking} is true
     *
     * @return true if {@link #looseReturnTypeChecking} is true
     */
    public boolean isLooseReturnTypeChecking() {
        return looseReturnTypeChecking;
    }

    /**
     * Allow or disallow Loose return Type Checking
     *
     * @param looseReturnTypeChecking the new Rule
     */
    public void setLooseReturnTypeChecking(boolean looseReturnTypeChecking) {
        this.looseReturnTypeChecking = looseReturnTypeChecking;
    }

    /**
     * asserts that the Return type matches the expected return Type
     */
    public void assertReturnType() {
        if (!(theExecutable instanceof Method))
            return;
        var theMethod = (Method) theExecutable;
        if (returnType == null) {
            throw new RuntimeErrorException(new Error(), "Faulty Test: Cannot assert return type null");
        }
        assertMethodResolved();
        if (looseReturnTypeChecking) {
            assertInstanceOf(returnType, theMethod.getReturnType(), getInvalidReturnTypeMessage(methodIdentifier.identifierName));
        } else {
            assertSame(returnType, theMethod.getReturnType(), getInvalidReturnTypeMessage(methodIdentifier.identifierName));
        }
    }

    /**
     * Verifies that the Method was declared correctly
     *
     * @returns this
     */
    public MethodTester verify() {
        if (!resolved()) {
            resolveMethod();
        }
        if (accessModifier >= 0) {
            assertAccessModifier();
        }
        assertParametersMatch();
        assertReturnType();
        return this;
    }

    /**
     * returns the Value of {@link #parameters}
     *
     * @return the Value of {@link #parameters}
     */
    public ArrayList<ParameterMatcher> getParameters() {
        return parameters;
    }

    /**
     * Set {@link #parameters} to the given value
     */
    public void setParameters(ArrayList<ParameterMatcher> parameters) {
        this.parameters = parameters;
    }

    /**
     * returns the Value of {@link #theExecutable}
     *
     * @return the Value of {@link #theExecutable}
     */
    public Executable getTheExecutable() {
        return theExecutable;
    }

    /**
     * Set {@link #theExecutable} to the given value
     */
    public void setTheExecutable(Method theExecutable) {
        this.theExecutable = theExecutable;
    }

    /**
     * Adds expected Parameter Matchers to {@link #parameters}
     *
     * @param interfaceMatcher the Interface Metchers to add
     */
    public void addParameter(ParameterMatcher... interfaceMatcher) {
        if (parameters == null) {
            parameters = new ArrayList<>();
        }
        parameters.addAll(Arrays.asList(interfaceMatcher));
    }

    /**
     * Adds expected Parameter Matchers to {@link #parameters}
     *
     * @param type       The expected parameter type
     * @param name       The Name to match
     * @param similarity The Minimum similarity required
     */
    public void addParameter(Class<?> type, String name, double similarity) {
        addParameter(new ParameterMatcher(name, similarity, type));
    }

    /**
     * Adds expected Parameter Matchers to {@link #parameters}
     *
     * @param type The expected parameter type
     */
    public void addParameter(Class<?> type) {
        addParameter(new ParameterMatcher(null, 1, type));
    }

    /**
     * assert that the Method Parameters match with {@link #parameters}
     */
    public void assertParametersMatch() {
        assertParametersMatch(theExecutable, methodIdentifier.identifierName, parameters, false);
    }

    /**
     * Generates a Method not found Message
     *
     * @return the generated Message
     */
    public String getMethodNotFoundMessage() {
        return getMethodNotFoundMessage(methodIdentifier.identifierName);
    }

    /**
     * returns {@code true} if {@link #theExecutable} is not {@code null}
     *
     * @return {@code true} if {@link #theExecutable} is not {@code null}
     */
    public boolean resolved() {
        return theExecutable != null;
    }

    /**
     * Assert that the method is resolved
     */
    public void assertMethodResolved() {
        assertTrue(resolved(), getMethodNotFoundMessage());
    }

    /**
     * Assert that {@link #classTester} is not {@code null}
     */
    public void assertClassTesterNotNull() {
        assertNotNull(classTester, getClassTesterNullMessage(methodIdentifier.identifierName));
    }

    /**
     * returns {@code true} if {@link #classTester} is not
     * {@code null} and {@link ClassTester#class_resolved} returns true
     *
     * @return {@code true} if {@link #classTester} is not {@code null}
     */
    public boolean classResolved() {
        return classTester != null && classTester.class_resolved();
    }

    /**
     * Asserts that {@link ClassTester#classInstance} is not {@code null}
     */
    public void assertClassResolved() {
        assertClassTesterNotNull();
        classTester.assertClassResolved();
    }

    /**
     * returns {@code true}, if the Method is invokable.
     *
     * <br>
     * </br>
     * To be exact: returns {@code true} if
     * {@link #classTester} {@link #theExecutable} and
     * {@link ClassTester#classInstance} are
     * resolved
     *
     * @return returns {@code true}, if the Method is invokable.
     */
    public boolean invokeable() {
        return classResolved() && classTester.classInstanceResolved() && resolved() && classTester.classInstanceResolved();
    }

    /**
     * Asserts that the Method is invokable.
     * <p>
     * To be exact: asserts that {@link #classTester} {@link #theExecutable} and
     * {@link ClassTester#classInstance} are resolved
     */
    public void assertInvokeable() {
        assertClassResolved();
        classTester.assertClassInstanceResolved();
        assertMethodResolved();
    }

    public <T> T invokeIns(Object... params) {
        return invoke(getClassTester().instantiate(), params);
    }

    /**
     * Invokes {@link #theExecutable} using {@link #classTester}
     *
     * @param params the Parameters used for invoking
     * @return the Returned Value of the Method
     */
    public <T> T invoke(Object instance, Object... params) {
        if (instance instanceof Mocked) {
            instance = ((Mocked) instance).getActualObject();
        }
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof Mocked) {
                params[i] = ((Mocked) params[i]).getActualObject();
            }
        }

        if (instance != null) {
            assertInvokeable();
        }
        assertDoesNotThrow(() -> theExecutable.setAccessible(true), "method could not be invoked");
        Object returnValue = null;
        try {
            if (theExecutable instanceof Method)
                returnValue = ((Method) theExecutable).invoke(instance, params);
            else if (theExecutable instanceof Constructor<?>) {
                returnValue = ((Constructor<?>) theExecutable).newInstance(params);
                getClassTester().setClassInstance(returnValue);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            if (e instanceof InvocationTargetException && ((InvocationTargetException) e).getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) ((InvocationTargetException) e).getTargetException();
            }
            fail(instance + ": " + methodIdentifier.identifierName + "(" +
                Arrays.stream(params).map(Objects::toString).collect(Collectors.joining(",")) + ") could not be invoked: " +
                e.getMessage(), e);
        }

        //noinspection unchecked
        return (T) returnValue;
    }

    public <T> T invokeStatic(Object... params) {
        return invoke(null, params);
    }

    /**
     * Gets the Invocations of the Method
     *
     * @return the Invocations of the Method
     */
    public List<Invocation> getInvocations() {
        assertMethodResolved();
        classTester.assertSpied();
        return classTester.getMockingDetails().getInvocations().stream().filter(x -> x.getMethod().getName().equals(getTheExecutable().getName())).collect(Collectors.toList());
    }

    public List<Invocation> getInvocations(Object instance) {
        return getInvocations().stream().filter(x -> x.getMock() == instance).collect(Collectors.toList());
    }

    /**
     * Gets the Invocation Count (How often Has the Method been invoked?)
     *
     * @return the Invocation Count
     */
    public int getInvocationCount() {
        return getInvocations().size();
    }

    public void assertInvoked() {
        if (getInvocationCount() == 0) {
            fail(wasNotCalledRecursively(getMethodIdentifier().identifierName));
        }
    }

    /**
     * Gets random Valid Parameter Values
     *
     * @return the Random Parameters
     */
    public Object[] getRandomParams() {
        return Arrays.stream(getTheExecutable().getParameters()).map(x -> ClassTester.getRandomValue(x.getType())).toArray();
    }

    /**
     * {@link #theExecutable} using {@link #classTester} with Random parameters
     *
     * @return the Returned Value of the Method
     */
    public Object invokeWithRandomParams() {
        assertMethodResolved();
        return invoke(getRandomParams());
    }

    /**
     * Asserts the Return Value of an invokation with the given parameters
     *
     * @param expected          the expected Return value
     * @param additionalMessage an Additional Message Text
     * @param params            the Parameters used for invokation
     */
    public void assertReturnValueEquals(Object expected, String additionalMessage, Object... params) {
        assertEquals(expected, invoke(params), "Falsche R??ckgabe bei Methode" + getMethodIdentifier().identifierName + (params.length > 0 ? "mit Parameter(n):" + safeArrayToString(params) : "") + additionalMessage);
    }

    /**
     * Asserts that none of the Blacklisted Constructs were Used
     *
     * @param disallowedConstructs the Disallowed Constructs
     */
    public void assertConstructsNotUsed(List<Class<? extends CtCodeElement>> disallowedConstructs) {
        var method = assertCtMethodExists();
        var test = test();
        for (var construct : disallowedConstructs) {
            if (!method.getElements(new TypeFilter<>(construct)).isEmpty()) {
                test.add(() -> fail(String.format("<%s> was used unexpectedly", construct.getSimpleName().substring(2))));
            }
        }
        test.run();
    }

    public void assertConstructsUsed(List<Class<? extends CtCodeElement>> disallowedConstructs) {
        var method = assertCtMethodExists();
        var test = test();
        for (var construct : disallowedConstructs) {
            if (method.getElements(new TypeFilter<>(construct)).isEmpty()) {
                test.add(() -> fail(String.format("<%s> was not used unexpectedly", construct.getSimpleName().substring(2))));
            }
        }
        test.run();
    }

    public CtMethod<?> assertCtMethodExists() {
        assureResolved();
        Launcher spoon = assertDoesNotThrow(() -> getClassTester().assureSpoonLauncherModelsBuild().getSpoon(), "Could not Create Spoon Launcher");
        CtType<?> type = assertDoesNotThrow(() -> spoon.getModel().getAllTypes().stream().filter(CtType::isTopLevel).findFirst().orElseThrow(),
            "Could not resolve Class Source for Class " + classTester.getClassIdentifier().identifierName + "." + "available Class Sources:" + spoon.getModel().getAllTypes().toString());
        return assertDoesNotThrow(() -> type.getMethodsByName(getMethodIdentifier().identifierName).stream().findFirst().orElseThrow(), "Could not resolve Method Source for Method " + getTheExecutable().getName());
    }

    public Set<CtMethod<?>> getCtMethods() {
        assureResolved();
        Launcher spoon = assertDoesNotThrow(() -> getClassTester().assureSpoonLauncherModelsBuild().getSpoon(), "Could not Create Spoon Launcher");
        CtType<?> type = assertDoesNotThrow(() -> spoon.getModel().getAllTypes().stream().filter(CtType::isTopLevel).findFirst().orElseThrow(),
            "Could not resolve Class Source for Class " + classTester.getClassIdentifier().identifierName + "." + "available Class Sources:" + spoon.getModel().getAllTypes().toString());
        return assertDoesNotThrow(type::getAllMethods, "Could not resolve Method Sources for Class " + classTester.getClassIdentifier().identifierName);
    }

    public void assertDirectlyRecursive() {
        assertRecursive(1);
    }

    public void assertRecursive(int level) {
        var m = assertCtMethodExists();
        if (!isRecursive(m.getDirectChildren(), m, level)) {
            fail(String.format("method <%s> is not recursive", getMethodIdentifier().identifierName));
        }
    }

    public void assertChildRecursive(int level) {
        var m = assertCtMethodExists();
        if (!isChildRecursive(m.getDirectChildren(), level, getCtMethods())) {
            fail(String.format("method <%s> is not recursive", getMethodIdentifier().identifierName));
        }
    }

    public void assertNotDirectlyRecursive() {
        assertNotRecursive(1);
    }

    public void assertNotRecursive(int level) {
        var m = assertCtMethodExists();
        if (isRecursive(m.getDirectChildren(), m, level)) {
            fail(String.format("method <%s> is recursive", getMethodIdentifier().identifierName));
        }
    }

    /**
     * Asserts the Return Value of an invokation with the given parameters
     *
     * @param expected the expected Return value
     * @param params   the Parameters used for invokation
     */
    public void assertReturnValueEquals(Object expected, Object... params) {
        assertReturnValueEquals(expected, "", params);
    }

    /**
     * Returns the Value of {@link #accessModifier}
     *
     * @return the Value of {@link #accessModifier}
     */
    public int getAccessModifier() {
        return accessModifier;
    }

    /**
     * Sets {@link #accessModifier} to the given Value
     *
     * @param accessModifier the new Access Modifier
     */
    public void setAccessModifier(int accessModifier) {
        this.accessModifier = accessModifier;
    }

    /**
     * Asserts the actual access Modifier matches {@link #accessModifier}
     */
    public void assertAccessModifier() {
//        disabled for this submission
    }

    /**
     * Resolve the Method with tolerances
     *
     * <br>
     * </br>
     * The Method is first searched by name using using
     * {@link TestUtils#similarity(String, String)}. If Multiple overloads are found
     * then the function with the most matching parameters according to
     * {@link #countMatchingParameters(Executable, String, ArrayList, boolean)} is
     * chosen.
     *
     * @param theClass        The Class to search in
     * @param methodName      The expected Method name
     * @param similarity      The minimum required similarity
     * @param parameters      The expected Parameters
     * @param allowSuperClass whether to search in Super classes as well
     * @return the resolved Method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Executable, String, ArrayList, boolean)
     */
    public Executable resolveExecutable(Class<?> theClass, String methodName, double similarity, ArrayList<ParameterMatcher> parameters, boolean allowSuperClass) {
        similarity = Math.max(0, Math.min(similarity, 1));
        ClassTester.assertClassNotNull(theClass, "zu Methode " + methodName);
        ArrayList<Executable> methods = allowSuperClass ? getAllMethods(theClass) : new ArrayList<>(Arrays.asList(theClass.getDeclaredMethods()));
        methods.addAll(List.of(theClass.getConstructors()));
        var bestMatch = methods.stream().min((x, y) -> Double.compare(TestUtils.similarity(methodName, y.getName()), TestUtils.similarity(methodName, x.getName()))).orElse(null);
        assertMethodNotNull(bestMatch, methodName);
        var sim = TestUtils.similarity(bestMatch.getName(), methodName);
        assertTrue(sim >= similarity, getMethodNotFoundMessage() + "??hnlichster Methodenname:" + bestMatch.getName() + " with " + sim + " similarity.");
        if (parameters != null) {
            // Account for overloads
            var matches = methods.stream().filter(x -> TestUtils.similarity(methodName, x.getName()) == sim).collect(Collectors.toCollection(ArrayList::new));
            if (matches.size() > 1) {
                // Find Best match according to parameter options
                bestMatch = matches.stream().min((x, y) -> Integer.compare(countMatchingParameters(y, methodName, parameters, true), countMatchingParameters(x, methodName, parameters, true))).orElse(null);
            }
        }

        return theExecutable = bestMatch;
    }

    /**
     * Resolve the Method with tolerances
     *
     * <br>
     * </br>
     * The Method is first searched by name using using
     * {@link TestUtils#similarity(String, String)}. If Multiple overloads are found
     * then the function with the most matching parameters according to
     * {@link #countMatchingParameters(Executable, String, ArrayList, boolean)} is
     * chosen.
     *
     * @param theClass   The Class to search in
     * @param methodName The expected Method name
     * @param similarity The minimum required similarity
     * @param parameters The expected Parameters
     * @return the resolved Method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Executable, String, ArrayList, boolean)
     */
    public Executable resolveExecutable(Class<?> theClass, String methodName, double similarity, ArrayList<ParameterMatcher> parameters) {
        return resolveExecutable(theClass, methodName, similarity, parameters, false);
    }

    /**
     * Resolve the Method with tolerances
     *
     * <br>
     * </br>
     * The Method is first searched by name using using
     * {@link TestUtils#similarity(String, String)}. If Multiple overloads are found
     * then the function with the most matching parameters according to
     * {@link #countMatchingParameters(Executable, String, ArrayList, boolean)} is
     * chosen.
     *
     * @return the resolved Method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Executable, String, ArrayList, boolean)
     */
    public Method resolveMethod() {
        var executable = resolve();
        if (executable instanceof Method)
            return (Method) executable;
        return fail("executable is not a method");
    }

    public Executable resolve() {
        getClassTester().assureResolved();
        return resolveExecutable(classTester.theClass, methodIdentifier.identifierName, methodIdentifier.similarity, parameters, allowSuperClass);
    }

    /**
     * Assures that the Method has been resolved
     *
     * @return the Method
     */
    public MethodTester assureResolved() {
        if (!resolved()) {
            resolve();
        }
        return this;
    }

    /**
     * Resolve the Method with tolerances
     *
     * <br>
     * </br>
     * The Method is first searched by name using using
     * {@link TestUtils#similarity(String, String)}. If Multiple overloads are found
     * then the function with the most matching parameters according to
     * {@link #countMatchingParameters(Executable, String, ArrayList, boolean)} is
     * chosen.
     *
     * @param similarity The minimum required similarity
     * @return the resolved Method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Executable, String, ArrayList, boolean)
     */
    public Executable resolveExecutable(double similarity) {
        return resolveExecutable(classTester.theClass, methodIdentifier.identifierName, similarity, parameters);
    }

    public void assertNoInvocation() {
        var m = assertCtMethodExists();
        if (isChildRecursive(m.getDirectChildren(), 1, getCtMethods())) {
            fail(String.format("method <%s> contains recursive calls, but should not.", getMethodIdentifier().identifierName));
        }
    }

    public MethodTester forClass(ClassTester<?> classTester) {
        return new MethodTester(
            classTester,
            methodIdentifier.identifierName,
            methodIdentifier.similarity,
            accessModifier,
            returnType,
            parameters,
            allowSuperClass,
            looseReturnTypeChecking).assureResolved();
    }
}
