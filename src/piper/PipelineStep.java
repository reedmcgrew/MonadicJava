package piper;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by mcgrewbr on 3/27/15.
 */
public class PipelineStep<I, O> {
    private final Function<I, O> func;
    private final I input;

    public PipelineStep(Function<I, O> func, I input) {
        this.func = func;
        this.input = input;
    }

    public <OPRIME> PipelineStep<I,OPRIME> andThen(Function<O,OPRIME> after){
        return new PipelineStep<>(func.andThen(after), input);
    }

    public PipelineStep<I,O> wrapWith(Wrapper<I,O> wrapper){
        return new PipelineStep<>(wrapper.wrap(func), input);
    }

    public void thenDo(Consumer<O> consumer){
        consumer.accept(get());
    }

    public O get(){
        return func.apply(input);
    }

    public PipelineInput<O> pipe() {
        return new PipelineInput(get());
    }
}
