package mainpkg;

import java.util.Random;

public class Main {
    public static void main(String[] args){
        int[] values=new int[100];
        int[] weights=new int[100];
        Random rand=new Random();
        for(int i=0; i<100; i++){
            values[i]= (rand.nextInt(9)+2);
            weights[i]= (rand.nextInt(5)+1);
            System.out.print(values[i]+";"+weights[i]+" | ");
        }
        Solver solver = new Solver(100, values, weights, 150, 100, 1000, 0.05);
    }
}
