package piper;

/**
 * Created by mcgrewbr on 3/27/15.
 */
public class Piper {

    public static <T> PipelineInput<T> pipe(T input){
        return new PipelineInput<>(input);
    }
}
