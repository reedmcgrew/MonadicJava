import monads.Pipeable;

import java.util.function.Function;

/**
 * Created by mcgrewbr on 3/27/15.
 */
public class PipeableExample {

    public static void main(String[] args){

        Pipeable.of("Stuff")
                .thenTo(str -> str + "_addThis")
                .thenTo(str -> str + "_andThenThis")
                .wrapWith(PipeableExample::wrappy)
                .thenDo(System.out::println);

    }

    public static Function<String,String> wrappy(Function<String,String> f) {
        return (input) -> "WRAP" + f.apply(input) + "WRAP";
    }
}
