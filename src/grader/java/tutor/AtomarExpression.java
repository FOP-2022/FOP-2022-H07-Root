package tutor;

import student.ReturnData_STUD;

import java.util.List;
import java.util.stream.Stream;

import static tutor.Utils.Messages.indexNotInRange;

public class AtomarExpression extends AbstractExpression {

    private final char c;

    public AtomarExpression(int startIndex, char c) {
        super(startIndex);
        this.c = c;
    }

    @Override
    public int getValue() {
        return Character.getNumericValue(c);
    }

    @Override
    public Stream<AbstractExpression> stream() {
        return Stream.of(this);
    }

    @Override
    public List<Character> getCharacterList() {
        return List.of(c);
    }

    @Override
    public boolean isValidIndex(int index) {
        return startIndex == index;
    }

    @Override
    public ReturnData_STUD evaluate(int index) {
        if (index != startIndex) {
            throw new RuntimeException(indexNotInRange());
        }
        var d = new ReturnData_STUD();
        d.setNextIndex(index + 1);
        d.setResult(Character.getNumericValue(c));
        return d;
    }
}
