package H07.person;

public abstract class FunctionWithBucketing {
  protected final PersonEquivalency eq;

  public FunctionWithBucketing(PersonEquivalency eq) {
    this.eq = eq;
  }

  public abstract Person[][] apply(Person[] people);
}
