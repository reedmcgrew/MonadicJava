package piper;

/**
 * Created by mcgrewbr on 3/27/15.
 */
public class Piper {

    /*
     * Represents the starting point of a pipeline.  The given input can then be "piped" to multiple functions
     * composed together (see PipelineStep).
     *
     * @param input Any input.
     */
    public static <T> PipelineInput<T> pipe(T input){
        return new PipelineInput<>(input);
    }
}
