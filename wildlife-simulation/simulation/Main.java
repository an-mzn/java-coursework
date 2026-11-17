package simulation;

public class Main {
    public static void main(String[] args) {
        int numSteps = args.length > 0 ? Integer.parseInt(args[0]) : 1000; // read param
        new Simulator().simulate(numSteps);
    }
}