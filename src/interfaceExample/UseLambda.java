package interfaceExample;

import java.util.function.DoubleFunction;

/**
 *
 * @author tadaki
 */
public class UseLambda {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //ラムダ式の利用
        DoubleFunction<Double> function = x -> x * x;
        System.out.println(function.apply(2.));
    }

}
