package monads;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents an un-evaluated function and its intended input.  Pipeables facilitate function composition,
 * including transparent function wrapping (decoration).
 * 
 * The intent behind this class is to implement OCaml-style piping and facilitate a bit cleaner function
 * wrapping (for use as an alternative to aspects).
 */
public class Pipeable<I, O> {
    private final Function<I, O> func;
    private final I input;

    /*
     * Create a new pipeline step
     */
    public Pipeable(Function<I, O> func, I input) {
        this.func = func;
        this.input = input;
    }

    /*
     * Represents the starting point of a pipeline.  The given input can then be "piped" to multiple functions
     * composed together (see PipelineStep).
     *
     * @param input Any input.
     */
    public static <T> Pipeable<T,T> of(T input){
        return new Pipeable<>(Function.identity(), input);
    }

    /*
     * Forward-compose the current pipeline step's function with the given function "after".
     *
     * @param after The function to which the the output of the current pipeline step's function will be applied.
     */
    public <OPRIME> Pipeable<I,OPRIME> thenTo(Function<O, OPRIME> after){
        return new Pipeable<>(func.andThen(after), input);
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
    public Pipeable<I,O> wrapWith(Wrapper<I,O> wrapper){
        return new Pipeable<>(wrapper.wrap(func), input);
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
    public Pipeable<O, O> pipe() {
        return new Pipeable<>(Function.identity(), get());
    }
}
