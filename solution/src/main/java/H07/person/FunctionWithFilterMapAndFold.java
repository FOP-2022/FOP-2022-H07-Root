package H07.person;

public abstract class FunctionWithFilterMapAndFold {
  protected final Traits traits;

  public FunctionWithFilterMapAndFold(Traits traits) {
    this.traits = traits;
  }

  public abstract int apply(Person[] people);
}
