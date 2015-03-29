package railways;

import java.util.function.Function;

/**
 * Created by mcgrewbr on 3/27/15.
 */
public interface Wrapper<I,O> {
    Function<I,O> wrap(Function<I,O> func);
}
