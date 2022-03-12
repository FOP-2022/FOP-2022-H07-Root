package reflection;

/**
 * A Parameter Matcher based on {@link IdentifierMatcher}
 *
 * @author Ruben Deisenroth
 * @see IdentifierMatcher
 */
public class ParameterMatcher extends IdentifierMatcher {
    /**
     * The expected parameter type
     */
    public Class<?> parameterType;
    /**
     * Whether or not to Allow Parameters derived from {@link #parameterType}
     */
    public boolean allowSubTypes = true;

    /**
     * Generates a new {@link ParameterMatcher}
     *
     * @param identifierName The Name to match
     * @param similarity     The Minimum similarity required
     * @param parameterType  The expected parameter type
     * @param allowSubTypes  Whether or not to Allow Parameters derived from
     *                       {@link #parameterType}
     */
    public ParameterMatcher(String identifierName, double similarity, Class<?> parameterType, boolean allowSubTypes) {
        super(identifierName, similarity);
        this.parameterType = parameterType;
        this.allowSubTypes = allowSubTypes;
    }

    /**
     * Generates a new {@link ParameterMatcher}
     *
     * @param identifierName The Name to match
     * @param similarity     The Minimum similarity required
     * @param parameterType  The expected parameter type
     */
    public ParameterMatcher(String identifierName, double similarity, Class<?> parameterType) {
        super(identifierName, similarity);
        this.parameterType = parameterType;
    }

    /**
     * Generates a new {@link ParameterMatcher}
     *
     * @param parameterType The expected parameter type
     */
    public ParameterMatcher(Class<?> parameterType) {
        this(null, 0, parameterType);
    }

    /**
     * Generates a new {@link ParameterMatcher}
     *
     * @param parameterType The expected parameter type
     * @param allowSubTypes Whether or not to Allow Parameters derived from
     *                      {@link #parameterType}
     */
    public ParameterMatcher(Class<?> parameterType, boolean allowSubTypes) {
        this(null, 0, parameterType, allowSubTypes);
    }
}
