package mainpkg;

import java.util.ArrayList;
import java.util.Random;

public class Solver {
    private int itemCount;
    private int[] values;
    private int[] weights;
    private int maxSize;
    private int candidateCount;
    private int maxGenerations;
    private double crossover = 0.5;
    private double mutation;
    private ArrayList<String> CandidateSolutions;
    private ArrayList<String> StarterPopulation;
    private int bestSolution;


    public Solver(int itemCount, int[] values, int[] weights, int maxSize,
                  int population, int generations, double mutation) {
        this.itemCount = itemCount;
        this.values = values;
        this.weights = weights;
        this.maxSize = maxSize;
        this.candidateCount = population;
        this.maxGenerations = generations;
        this.mutation = mutation;

        CandidateSolutions = new ArrayList<>();
        StarterPopulation = new ArrayList<>();

        //створюємо candidateCount початкових
        generateSolutions(candidateCount);
        StarterPopulation.addAll(CandidateSolutions);

        System.out.println();

        /* //перевірка
        int count=0;
        for(String s: StarterPopulation) {
            System.out.println("Starter: "+count+" | "+s+" | value: "+calcSolution(s) +" | weight : " + calcSolutionWeight(s));
            count++;
        }
         */


        for (int k = 0; k < maxGenerations; k++) {
            /* //вивід усіх поколінь
            System.out.print("Generation: " + (k+1));
            System.out.print(" " + "Best Solution:" + getBestSolution());
            System.out.print(" " + "Weight:" + calcSolutionWeight(getBestSolution()));
            System.out.println(" " + "Value:" + bestSolution);
            */
            newGen();
        }
        System.out.print(" " + "Best Solution:" + getBestSolution());
        System.out.print(" " + "Weight:" + calcSolutionWeight(getBestSolution()));
        System.out.println(" " + "Value:" + bestSolution);
    }

    //створюємо candidateCount початкових
    private void generateSolutions(int amount) {
        String solution;

        int count = 0;

        //1 - предмет №і є, 0 - немає
        for (int i = 0; i < amount; i++) {
            solution = "";
            for (int j = 0; j < itemCount; j++) {
                if (j == count)
                    solution += 1;
                else
                    solution += 0;
            }

            //додаємо у список кандидатів
            CandidateSolutions.add(solution);

            count++;
        }
    }

    private void setBestSolution(int bestSolution) {
        this.bestSolution = bestSolution;
    }

    private int calcSolution(String solution) {
        int value = 0;
        int weight = 0;

        //проходимо по стрічці, якщо символ №і = 49 (юнікод "1"),
        //враховуємо предмет №і, додаючи його вагу і цінність до загальної
        for (int i = 0; i < solution.length(); i++) {
            if (solution.charAt(i) == 49) {//49=1
                weight += weights[i];
                value += values[i];
            }
        }

        //повертаємо, якщо загальна вага не перевищує допустимої
        if (weight <= maxSize && weight != 0)
            return value;
        else
            return -1;
    }

    //обчислення ваги за ^таким же принципом; просто для виводу
    private int calcSolutionWeight(String solution) {
        int weight = 0;

        for (int i = 0; i < solution.length(); i++)
            if (solution.charAt(i) == 49)//49=1
                weight += weights[i];

        return weight;
    }

    //схрещення
    private String crossOver(String cand1, String cand2) {
        StringBuilder newCand = new StringBuilder();


        //проходимо по стрічці №1, у кожного символа №і є crossover ймовірність
        //буди обміняним з відповідним символом стрічки №2
        for (int i = 0; i < cand1.length(); i++) {
            if (Math.random() >= crossover)
                newCand.append(cand1.charAt(i));
            else
                newCand.append(cand2.charAt(i));
        }

        return newCand.toString();
    }

    //мутація
    private String mutate(String cand) {
        //якщо випадкове число 0.00-1.00 менше за mutation
        //відбувається мутація
        /*
        if(Math.random()<=mutation)
            cand = swapGenes(cand);
        */


        for(int i = 0; i < cand.length(); i++){
                cand = swapGenes(cand);
        }

        return cand;
    }

    //випадковий вибір номеру гену
    private int randomizeGeneIdx(String cand) {
        Random rand = new Random();
        return rand.nextInt(cand.length());
    }

    //обмін генів (для мутації, умова завдання: зміна двох випадкових місцями)
    private String swapGenes(String cand) {
        StringBuilder sb= new StringBuilder(cand);
        int idx1=0, idx2=0, gene1=0, gene2=0;

        //коли (idx1==idx2)&&(gene1==gene2) досягнуто, отримані параметри
        //використовуються для проведення мутації
        while((idx1==idx2)&&(gene1==gene2)){
            idx1=randomizeGeneIdx(cand);
            idx2=randomizeGeneIdx(cand);
            if(idx1==idx2)
                continue;
            gene1=sb.charAt(idx1);
            gene2=sb.charAt(idx2);
            if((gene1!=gene2))
                break;
        }

        //зміна генів місцями
        sb.setCharAt(idx1, (char) gene2);
        sb.setCharAt(idx2, (char) gene1);
        return sb.toString();
    }

    //пошук найкращого рішення з усіх кандидатів
    private String getBestSolution() {
        int bestValue = -1;
        String bestSolution = null;

        for (String cand : CandidateSolutions) {
            int newValue = calcSolution(cand);
            if (newValue != -1) {
                if (newValue >= bestValue) {
                    bestSolution = cand;
                    bestValue = newValue;
                }
            }
        }
        setBestSolution(bestValue);

        return bestSolution;
    }

    //початок алгоритму
    private void newGen() {
        //знайти найкраще рішення
        String cand1 = getBestSolution();
        CandidateSolutions.remove(cand1);
        //знайти друге найкраще рішення
        String cand2 = getBestSolution();
        //новий список кандидатів
        CandidateSolutions = new ArrayList<>();
        CandidateSolutions.add(cand1);

        //заповнення списку кандидатів рішеннями,
        //які є результатом схрещення й мутації
        String cand;
        for (int i = 1; i < candidateCount; i++) {
            cand = mutate(crossOver(cand1, cand2));
            CandidateSolutions.add(cand);
        }
    }
}