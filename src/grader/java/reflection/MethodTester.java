package reflection;

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

import javax.management.RuntimeErrorException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static tutor.Utils.Messages.wasNotCalledRecursively;
import static tutor.Utils.TestCollection.test;

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
    Method theMethod;
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
        this.methodIdentifier = new IdentifierMatcher(methodName, similarity);
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
    public MethodTester(ClassTester<?> classTester, String methodName, double similarity, int accessModifier, Class<?> returnType, ArrayList<ParameterMatcher> parameters, boolean allowSuperClass, boolean looseReturnTypeChecking) {
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
        return String.format("falscher Rückgabetyp für Methode %s", methodName);
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
    public static int countMatchingParameters(Method m, String methodName, ArrayList<ParameterMatcher> parameters, boolean ignoreNames) {
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
                // TODO fix assertions for sub-types
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
    public static void assertParametersMatch(Method m, String methodName, ArrayList<ParameterMatcher> parameters, boolean ignoreNames) {
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
    public static void assertMethodNotNull(Method m, String name) {
        assertNotNull(m, getMethodNotFoundMessage(name));

    }

    /**
     * Generates a Class tester null message
     *
     * @param methodName the expected Method name
     * @return the generated message
     */
    public static String getClassTesterNullMessage(String methodName) {
        return String.format("Fehlerhafter Test für Methode %s: Kein Klassentester gegeben.", methodName);
    }

    public static String safeArrayToString(Object... array) {
        var paramsString = "[]";
        if (array != null) {
            try {
                paramsString = Arrays.toString(array);
            } catch (Exception e) {
                Arrays.stream(array).map(x -> x.getClass().getName() + "@" + Integer.toHexString(x.hashCode())).collect(Collectors.joining(", ", "[", "]"));
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
    private static ArrayList<Method> getAllMethods(ArrayList<Method> methods, Class<?> clazz) {
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
    public static ArrayList<Method> getAllMethods(Class<?> clazz) {
        return getAllMethods(new ArrayList<>(), clazz);
    }

    public static boolean isRecursive(List<CtElement> elements, CtMethod<?> methodToCall, int level) {
        if (level <= 0) {
            return false;
        }
        for (var e : elements) {
            if (e instanceof CtInvocation<?>) {
                var method = (CtInvocation<?>) e;
                if (method.getExecutable().equals(methodToCall.getReference())) {
                    return true;
                }
                if (isRecursive(e.getDirectChildren(), methodToCall, level - 1)) {
                    return true;
                }
            } else if (e instanceof CtExecutableReference<?>) {
                var exe = (CtExecutableReference<?>) e;
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
            if (e instanceof CtInvocation<?>) {
                var invo = (CtInvocation<?>) e;
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

    public void assertCorrectDeclaration() {
        assertMethodResolved();
        test().add(() -> assertParametersMatch()).add(() -> assertReturnType()).add(() -> assertAccessModifier()).run();
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
        if (!methodResolved()) {
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
     * returns the Value of {@link #theMethod}
     *
     * @return the Value of {@link #theMethod}
     */
    public Method getTheMethod() {
        return theMethod;
    }

    /**
     * Set {@link #theMethod} to the given value
     */
    public void setTheMethod(Method theMethod) {
        this.theMethod = theMethod;
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
        assertParametersMatch(theMethod, methodIdentifier.identifierName, parameters, false);
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
     * returns {@code true} if {@link #theMethod} is not {@code null}
     *
     * @return {@code true} if {@link #theMethod} is not {@code null}
     */
    public boolean methodResolved() {
        return theMethod != null;
    }

    /**
     * Assert that the method is resolved
     */
    public void assertMethodResolved() {
        assertTrue(methodResolved(), getMethodNotFoundMessage());
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
     * {@link #classTester} {@link #theMethod} and
     * {@link ClassTester#classInstance} are
     * resolved
     *
     * @return returns {@code true}, if the Method is invokable.
     */
    public boolean invokeable() {
        return classResolved() && classTester.classInstanceResolved() && methodResolved() && classTester.classInstanceResolved();
    }

    /**
     * Asserts that the Method is invokable.
     * <p>
     * To be exact: asserts that {@link #classTester} {@link #theMethod} and
     * {@link ClassTester#classInstance} are resolved
     */
    public void assertInvokeable() {
        assertClassResolved();
        classTester.assertClassInstanceResolved();
        assertMethodResolved();
    }

    /**
     * Invokes {@link #theMethod} using {@link #classTester}
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
        assertDoesNotThrow(() -> theMethod.setAccessible(true), "method could not be invoked");
        Object returnValue = null;
        try {
            returnValue = theMethod.invoke(instance, params);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            if (e instanceof InvocationTargetException && ((InvocationTargetException) e).getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) ((InvocationTargetException) e).getTargetException();
            }
//            Arrays.stream(e.getStackTrace()).forEach(x -> Global.LOGGER.log(Level.WARN, x));
            fail("method could not be invoked" + e.getMessage(), e);

        }
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
        return classTester.getMockingDetails().getInvocations().stream().filter(x -> x.getMethod().getName().equals(getTheMethod().getName())).collect(Collectors.toList());
    }

    // public boolean needsJavadoc() {
    // assertMethodResolved();
    // // theMethod.
    // }

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
        return Arrays.stream(getTheMethod().getParameters()).map(x -> ClassTester.getRandomValue(x.getType())).toArray();
    }

    /**
     * {@link #theMethod} using {@link #classTester} with Random parameters
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
        assertEquals(expected, invoke(params), "Falsche Rückgabe bei Methode" + getMethodIdentifier().identifierName + (params.length > 0 ? "mit Parameter(n):" + safeArrayToString(params) : "") + additionalMessage);
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
        CtMethod<?> method = assertDoesNotThrow(() -> type.getMethodsByName(getMethodIdentifier().identifierName).stream().findFirst().orElseThrow(), "Could not resolve Method Source for Method " + getTheMethod().getName());
        return method;
    }

    public Set<CtMethod<?>> getCtMethods() {
        assureResolved();
        Launcher spoon = assertDoesNotThrow(() -> getClassTester().assureSpoonLauncherModelsBuild().getSpoon(), "Could not Create Spoon Launcher");
        CtType<?> type = assertDoesNotThrow(() -> spoon.getModel().getAllTypes().stream().filter(CtType::isTopLevel).findFirst().orElseThrow(),
            "Could not resolve Class Source for Class " + classTester.getClassIdentifier().identifierName + "." + "available Class Sources:" + spoon.getModel().getAllTypes().toString());
        Set<CtMethod<?>> methods = assertDoesNotThrow(type::getAllMethods, "Could not resolve Method Sources for Class " + classTester.getClassIdentifier().identifierName);
        return methods;
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
//        disabled for this submbission
//        if (accessModifier >= 0) {
//            TestUtils.assertModifier(accessModifier, theMethod);
//        }
    }

    /**
     * Resolve the Method with tolerances
     *
     * <br>
     * </br>
     * The Method is first searched by name using using
     * {@link TestUtils#similarity(String, String)}. If Multiple overloads are found
     * then the function with the most matching parameters according to
     * {@link #countMatchingParameters(Method, String, ArrayList, boolean)} is
     * chosen.
     *
     * @param theClass        The Class to search in
     * @param methodName      The expected Method name
     * @param similarity      The minimum required similarity
     * @param parameters      The expected Parameters
     * @param allowSuperClass whether to search in Super classes as well
     * @return the resolved Method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Method, String, ArrayList, boolean)
     */
    public Method resolveMethod(Class<?> theClass, String methodName, double similarity, ArrayList<ParameterMatcher> parameters, boolean allowSuperClass) {
        similarity = Math.max(0, Math.min(similarity, 1));
        ClassTester.assertClassNotNull(theClass, "zu Methode " + methodName);
        ArrayList<Method> methods = allowSuperClass ? getAllMethods(theClass) : new ArrayList<>(Arrays.asList(theClass.getDeclaredMethods()));
        var bestMatch = methods.stream().sorted((x, y) -> Double.valueOf(TestUtils.similarity(methodName, y.getName())).compareTo(TestUtils.similarity(methodName, x.getName()))).findFirst().orElse(null);
        assertMethodNotNull(bestMatch, methodName);
        var sim = TestUtils.similarity(bestMatch.getName(), methodName);
        assertTrue(sim >= similarity, getMethodNotFoundMessage() + "Ähnlichster Methodenname:" + bestMatch.getName() + " with " + sim + " similarity.");
        if (parameters != null) {
            // Account for overloads
            var matches = methods.stream().filter(x -> TestUtils.similarity(methodName, x.getName()) == sim).collect(Collectors.toCollection(ArrayList::new));
            if (matches.size() > 1) {
                // Find Best match according to parameter options
                bestMatch = matches.stream().sorted((x, y) -> Integer.valueOf(countMatchingParameters(y, methodName, parameters, true)).compareTo(countMatchingParameters(x, methodName, parameters, true))).findFirst().orElse(null);
            }
        }

        return theMethod = bestMatch;
    }

    /**
     * Resolve the Method with tolerances
     *
     * <br>
     * </br>
     * The Method is first searched by name using using
     * {@link TestUtils#similarity(String, String)}. If Multiple overloads are found
     * then the function with the most matching parameters according to
     * {@link #countMatchingParameters(Method, String, ArrayList, boolean)} is
     * chosen.
     *
     * @param theClass   The Class to search in
     * @param methodName The expected Method name
     * @param similarity The minimum required similarity
     * @param parameters The expected Parameters
     * @return the resolved Method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Method, String, ArrayList, boolean)
     */
    public Method resolveMethod(Class<?> theClass, String methodName, double similarity, ArrayList<ParameterMatcher> parameters) {
        return resolveMethod(theClass, methodName, similarity, parameters, false);
    }

    /**
     * Resolve the Method with tolerances
     *
     * <br>
     * </br>
     * The Method is first searched by name using using
     * {@link TestUtils#similarity(String, String)}. If Multiple overloads are found
     * then the function with the most matching parameters according to
     * {@link #countMatchingParameters(Method, String, ArrayList, boolean)} is
     * chosen.
     *
     * @return the resolved Method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Method, String, ArrayList, boolean)
     */
    public Method resolveMethod() {
        assertClassTesterNotNull();
        if (!classResolved()) {
            classTester.resolveClass();
        }
        return resolveMethod(classTester.theClass, methodIdentifier.identifierName, methodIdentifier.similarity, parameters, allowSuperClass);
    }

    /**
     * Assures that the Method has been resolved
     *
     * @return the Method
     */
    public MethodTester assureResolved() {
        if (!methodResolved()) {
            resolveMethod();
        }
        return this;
    }

    /**
     * Gets Method Documentation for JavaDoc
     *
     * @param d the Source Documentation
     * @return the Method Documentation
     */
//    public MethodDocumentation getMethodDocumentation(SourceDocumentation d) {
//        try {
//            classTester.assureClassResolved();
//            var resolvedMethod = assureMethodResolved().getTheMethod();
//            return d.forTopLevelType(classTester.getTheClass().getName()).forMethod(
//                resolvedMethod.getName(), resolvedMethod.getParameterTypes());
//        } catch (Throwable e) {
//            return d.forTopLevelType("").forMethod("");
//        }
//    }

    /**
     * Resolve the Method with tolerances
     *
     * <br>
     * </br>
     * The Method is first searched by name using using
     * {@link TestUtils#similarity(String, String)}. If Multiple overloads are found
     * then the function with the most matching parameters according to
     * {@link #countMatchingParameters(Method, String, ArrayList, boolean)} is
     * chosen.
     *
     * @param similarity The minimum required similarity
     * @return the resolved Method
     * @see TestUtils#similarity(String, String)
     * @see #countMatchingParameters(Method, String, ArrayList, boolean)
     */
    public Method resolveMethod(double similarity) {
        return resolveMethod(classTester.theClass, methodIdentifier.identifierName, similarity, parameters);
    }

    public void assertNoInvocation() {
        var m = assertCtMethodExists();
        if (isChildRecursive(m.getDirectChildren(), 1, getCtMethods())) {
            fail(String.format("method <%s> contains recursive calls, but should not.", getMethodIdentifier().identifierName));
        }
    }
}
