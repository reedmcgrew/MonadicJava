package piper;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents an un-evaluated function and its intended input.  PipelineSteps facilitate function composition,
 * including transparent function wrapping (decoration).
 */
public class PipelineStep<I, O> {
    private final Function<I, O> func;
    private final I input;

    /*
     * Create a new pipeline step
     */
    public PipelineStep(Function<I, O> func, I input) {
        this.func = func;
        this.input = input;
    }

    /*
     * Forward-compose the current pipeline step's function with the given function "after".
     *
     * @param after The function to which the the output of the current pipeline step's function will be applied.
     */
    public <OPRIME> PipelineStep<I,OPRIME> thenTo(Function<O, OPRIME> after){
        return new PipelineStep<>(func.andThen(after), input);
    }

    /*
     * Compose the current pipeline step's function with the given function "before".
     *
     *  @param before The function to which the the output of the current pipeline step's function will be applied.
     */

    /*
     * Wrap the current pipeline step's function with the given wrapper function.  Input and output types must
     * be preserved. This is useful for transparently decorating existing functions.  Note that this will wrap
     * the composition of all previous pipeline steps.
     *
     * @param wrapper A higher-order function that will take the current PipelineStep's function as input
     *      and return a function with identical
     */
    public PipelineStep<I,O> wrapWith(Wrapper<I,O> wrapper){
        return new PipelineStep<>(wrapper.wrap(func), input);
    }

    /*
     * Apply the input to the current function, passing the result to the given consumer.
     *
     * @param consumer Any lambda with 1 parameter and a void return type.
     */
    public void thenDo(Consumer<O> consumer){
        consumer.accept(get());
    }

    /*
     * Apply the current input to the current function (often representing the composition of many pipeline steps).
     */
    public O get(){
        return func.apply(input);
    }

    /*
     * Apply the input to the current function, passing
     */
    public PipelineInput<O> pipe() {
        return new PipelineInput(get());
    }
}
