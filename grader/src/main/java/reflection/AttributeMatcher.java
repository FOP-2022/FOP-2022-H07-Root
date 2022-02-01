package reflection;

/**
 * An Attribute Matcher based on {@link IdentifierMatcher}
 *
 * @author Ruben Deisenroth
 * @see IdentifierMatcher
 */
public class AttributeMatcher extends IdentifierMatcher {
    /**
     * The expected Access Modifier
     */
    public int modifier;
    /**
     * The Expected Attribute Type
     */
    public Class<?> type;
    /**
     * whether to also match super implementations
     */
    public boolean allowSuperClass;

    /**
     * Generates a new {@link AttributeMatcher}
     *
     * @param name            The Name to match
     * @param similarity      The Minimum similarity required
     * @param modifier        The expected Access Modifier
     * @param type            The Expected Attribute Type
     * @param allowSuperClass whether to also match super implementations
     */
    public AttributeMatcher(String name, double similarity, int modifier, Class<?> type, boolean allowSuperClass) {
        super(name, similarity);
        this.modifier = modifier;
        this.type = type;
        this.allowSuperClass = allowSuperClass;
    }

    /**
     * Generates a new {@link AttributeMatcher}
     *
     * @param name       The Name to match
     * @param similarity The Minimum similarity required
     * @param modifier   The expected Access Modifier
     * @param type       The Expected Attribute Type
     */
    public AttributeMatcher(String name, double similarity, int modifier, Class<?> type) {
        this(name, similarity, modifier, type, false);
    }

    /**
     * Generates a new {@link AttributeMatcher}
     *
     * @param name            The Name to match
     * @param similarity      The Minimum similarity required
     * @param type            The Expected Attribute Type
     * @param allowSuperClass whether to also match super implementations
     */
    public AttributeMatcher(String name, double similarity, Class<?> type, boolean allowSuperClass) {
        this(name, similarity, -1, type, allowSuperClass);
    }

    /**
     * Generates a new {@link AttributeMatcher}
     *
     * @param name       The Name to match
     * @param similarity The Minimum similarity required
     * @param type       The Expected Attribute Type
     */
    public AttributeMatcher(String name, double similarity, Class<?> type) {
        this(name, similarity, -1, type);
    }
}
