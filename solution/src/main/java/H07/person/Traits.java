package H07.person;

import java.util.function.IntBinaryOperator;

public class Traits {
  private final IntBinaryOperator op;
  private final int init;
  private final PersonToIntFunction fct;
  private final PersonFilter pred;
  private final IntBinaryOperator combine;

  public Traits(IntBinaryOperator op, int init, PersonToIntFunction fct, PersonFilter pred, IntBinaryOperator combine) {
    this.op = op;
    this.init = init;
    this.fct = fct;
    this.pred = pred;
    this.combine = combine;
  }

  public IntBinaryOperator getOp() {
    return op;
  }

  public int getInit() {
    return init;
  }

  public PersonToIntFunction getFct() {
    return fct;
  }

  public PersonFilter getPred() {
    return pred;
  }

  public IntBinaryOperator getCombine() {
    return combine;
  }
}
