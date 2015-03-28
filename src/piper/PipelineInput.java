package piper;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by mcgrewbr on 3/27/15.
 */
public class PipelineInput<I> {
    I input;
    public PipelineInput(I input) {
        this.input = input;
    }

    public <O> PipelineStep<I,O> to(Function<I,O> func){
        return new PipelineStep<>(func, input);
    }

    public void thenDo(Consumer<I> consumer){
        consumer.accept(input);
    }
}
