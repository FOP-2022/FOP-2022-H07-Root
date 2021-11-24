package H07.person;

public abstract class FunctionWithBucketing {
  protected final PersonEquivalency eq;

  /**
   * Defines the required argument of {@link PersonEquivalency} for all subclasses. To be used with super.
   *
   * @param eq a {@link PersonEquivalency}
   */
  public FunctionWithBucketing(PersonEquivalency eq) {
    this.eq = eq;
  }

  /**
   * Buckets the people of the input array according to the {@link PersonEquivalency} this object is configured with
   *
   * @param people an array of people
   * @return a (ragged) array as such that among each subarray all elements are equivalent with some {@link PersonEquivalency}
   */
  public abstract Person[][] apply(Person[] people);
}
