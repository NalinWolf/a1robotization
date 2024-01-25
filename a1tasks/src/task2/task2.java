package task2;

public class task2 {

    public static double calculateUn(int n) {
        double sum = 0;

        for (int i = 1; i <= n; i++) {
            double iFactorial = factorial(i);
            sum += iFactorial;
        }

        double nFactorial = factorial(n);
        return 1.0 / nFactorial * sum;
    }

    private static double factorial(int n) {
        double result = 1;

        for (int i = 1; i <= n; i++) {
            result *= i;
        }

        return result;
    }

    public static void main(String[] args) {
        int n = 4; // Пример значения n
        double result = calculateUn(n);
        System.out.println("u_" + n + " = " + result);
    }
}
