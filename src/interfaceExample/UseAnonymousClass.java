package interfaceExample;

import java.util.function.DoubleFunction;

/**
 *
 * @author tadaki
 */
public class UseAnonymousClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Using anonymous class
        DoubleFunction<Double> function = new DoubleFunction<Double>(){
            @Override
            public Double apply(double v){
                return v*v;
            }
        };
        System.out.println(function.apply(2.));
    }
    
}
