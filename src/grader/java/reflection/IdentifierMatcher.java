package reflection;

/**
 * An Identifier Matcher
 *
 * @author Ruben Deisenroth
 */
public class IdentifierMatcher {
    /**
     * The Name to match
     */
    public String identifierName;
    /**
     * The package Name
     */
    public String packageName;
    /**
     * The Minimum similarity required
     */
    public double similarity;

    /**
     * Creates a new {@link IdentifierMatcher}
     *
     * @param identifierName The Name to match
     * @param packageName    The package Name
     * @param similarity     The Minimum similarity required
     */
    public IdentifierMatcher(String identifierName, String packageName, double similarity) {
        this.identifierName = identifierName;
        this.packageName = packageName;
        this.similarity = similarity;
    }

    /**
     * Creates a new {@link IdentifierMatcher}
     *
     * @param identifierName The Name to match
     * @param similarity     The Minimum similarity required
     */
    public IdentifierMatcher(String identifierName, double similarity) {
        this(identifierName, null, similarity);
    }
}
