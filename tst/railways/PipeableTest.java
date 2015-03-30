package railways;

import javafx.util.Pair;
import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by mcgrewbr on 3/27/15.
 */
public class PipeableTest {
    @Test
    public void pipeAnInputToAFunction(){
        int output = Pipeable.of("Stringy")
                .thenTo(str -> str.length())
                .get();
        assertThat(output, is(7));
    }

    @Test
    public void decorateAFunction(){
        Function<Integer,String> toYs = num -> {
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < num; i++){
                builder.append("y");
            }
            return builder.toString();
        };

        Wrapper<Integer,String> wrapWithDecs =
                f -> (input) -> "dec" + f.apply(input) + "dec";

        Function<String, String> appendOneZ = (str) -> str + "z";

        String output = Pipeable.of(5)
                .thenTo(toYs)
                .thenTo(appendOneZ)
                //wraps the forward composition of the previous two lines
                .wrapWith(wrapWithDecs)
                .get();

        assertThat(output, is("decyyyyyzdec"));
    }

    @Test
    public void evaluateAPipelineAndConsumeItsResult(){
        Pipeable.of(7L)
                .thenTo(num -> num * 3L)
                .thenDo(result -> assertThat(result, is(21L)));
    }

    @Test
    public void evaluateAPipeline(){
        int output = Pipeable.of(1)
                .thenTo(num -> num + 5)
                .get();

        assertThat(output, is(6));
    }

    @Test
    public void concatenateTwoPipelineChains(){
        Pair<Integer,Integer> output = Pipeable.of(new Pair<>(2, 2))
                .thenTo(pair -> pair.getKey() * pair.getValue())
                .thenTo(num -> num + 2)
                //Evaluation happens here and is wrapped in a new railways.PipelineInput
                .pipe()
                .thenTo(num -> new Pair<>(1, num))
                .thenTo(pair -> new Pair<>(pair.getKey() + 1, pair.getValue() + 1))
                .get();

        assertThat(output.getKey(), is(2));
        assertThat(output.getValue(), is(7));
    }

    @Test
    public void pipeAnInputToAConsumer(){
         Pipeable.of(5)
                .thenDo(num -> assertThat(num, is(5)));
    }
}
