package student;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public abstract class AbstractTraverser_STUD extends Traverser_STUD {

    public final Object object;

    public AbstractTraverser_STUD() {
        this.object = cTraverser().getNewInstance();
        when(mGetFirstIndex().invoke(object, (Object) any(double[].class))).then(a -> getFirstIndex(a.getArgument(0)));
        when(mGetNextIndex().assureResolved().invoke(object, anyInt())).then(a -> getNextIndex(a.getArgument(0)));
    }

    @Override
    public Object getActualObject() {
        return object;
    }
}
