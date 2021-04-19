public class Individual {

    public final double learnRate;
    /**
     * Result vector. For example [1.5 , 2] when dimension is 2.
     */
    final double[] x;
    private final int dimension;
    /**
     * Increment size. The size this individual changes in a single generation. signum in formula
     */
    double increment;
    double fitness;

    Individual(int dimension, double learnRate) {
        this.dimension = dimension;
        this.learnRate = learnRate;

        //initialize with random values between -5 and 5. Range is problem dependent
        this.x = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            x[i] = 5 * Math.random();
            if (Math.random() < 0.5) x[i] = -x[i];
        }

        this.increment = Math.random();
    }

    public void calculateFitness() {
        //Rastigrin-Funktion
        //Minimierung
        //Minimum = 0.0 bei x[i] = 0

        double fit = 10.0 * dimension;
        for (double value : x) {
            fit += (Math.pow(value, 2) - 10 * Math.cos(2 * Math.PI * value));
        }
        this.fitness = fit;
    }

    public void mutate() {
        double randomNumber = learnRate * standardNormalDistribution();

        this.increment = this.increment * Math.exp(randomNumber);

        for (int i = 0; i < this.x.length; i++) {
            randomNumber = this.increment * standardNormalDistribution();
            this.x[i] = this.x[i] + randomNumber;
        }
    }

    public void recombine(Individual e1, Individual e2) {
        this.increment = (e1.increment + e2.increment) / 2.;
        for (int i = 0; i < this.x.length; i++) {
            double zz = Math.random();
            if (zz < 0.5)
                this.x[i] = e1.x[i];
            else
                this.x[i] = e2.x[i];
        }
    }

    private static double standardNormalDistribution() {
        // Methode von BoxMuller
        double z1 = Math.random();
        double z2 = Math.random();
        final var sqrt = Math.sqrt(-2 * Math.log(z2));
        double x1 = Math.cos(z1 * 2 * Math.PI) * sqrt;
        double x2 = Math.sin(z1 * 2 * Math.PI) * sqrt;
        if (Math.random() < 0.5)
            return x1;
        else
            return x2;
    }

}

