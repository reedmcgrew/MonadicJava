import piper.Piper;
import piper.Wrapper;
import javafx.util.Pair;
import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by mcgrewbr on 3/27/15.
 */
public class PipelineTest {
    @Test
    public void pipeAnInputToAFunction(){
        int output = Piper.pipe("Stringy")
                .to(str -> str.length())
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

        String output = Piper.pipe(5)
                .to(toYs)
                .andThen(appendOneZ)
                //wraps the forward composition of the previous two lines
                .wrapWith(wrapWithDecs)
                .get();

        assertThat(output, is("decyyyyyzdec"));
    }

    @Test
    public void evaluateAPipelineAndConsumeItsResult(){
        Piper.pipe(7L)
                .to(num -> num*3L)
                .thenDo(result -> assertThat(result, is(21L)));
    }

    @Test
    public void evaluateAPipeline(){
        int output = Piper.pipe(1)
                .to(num -> num + 5)
                .get();

        assertThat(output, is(6));
    }

    @Test
    public void concatenateTwoPipelineChains(){
        Pair<Integer,Integer> output = Piper.pipe(new Pair<>(2, 2))
                .to(pair -> pair.getKey()*pair.getValue())
                .andThen(num -> num + 2)
                //Evaluation happens here and is wrapped in a new piper.PipelineInput
                .pipe()
                .to(num -> new Pair<>(1, num))
                .andThen(pair -> new Pair<>(pair.getKey() + 1, pair.getValue() + 1))
                .get();

        assertThat(output.getKey(), is(2));
        assertThat(output.getValue(), is(7));
    }

    @Test
    public void pipeAnInputToAConsumer(){
        Piper.pipe(5)
                .thenDo(num -> assertThat(num, is(5)));
    }
}
