public class Individual {

    public static double learnRate;

    private final int dimension;
    private final double[] x;
    private double signum;
    private double fitness;

    Individual(int dimension) {
        this.dimension = dimension;
        this.x = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            x[i] = 5 * Math.random(); // problemspezifisch geraten
            if (Math.random() < 0.5) x[i] = -x[i];
        }

        this.signum = Math.random();
        learnRate = 1.0 / Math.sqrt(dimension); // Schwefel95
        //lernRate = 0.5; // probieren
    }

    private static double snv() {
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

    public void fitness() {
        //Rastigrin-Funktion
        //Minimierung
        //Minimum = 0.0 bei x[i] = 0

        double fit = 10.0 * dimension;
        for (double v : x) {
            fit += (Math.pow(v, 2) - 10 * Math.cos(2 * Math.PI * v));
        }
        this.fitness = fit;
    }

    public void mutate() {
        double zz = learnRate * snv();
        this.signum = this.signum * Math.exp(zz);
        if (signum < 0) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < this.x.length; i++) {
            zz = this.signum * snv();
            this.x[i] = this.x[i] + zz;
        }
    }

    public void recombine(Individual e1, Individual e2) {
        this.signum = (e1.signum + e2.signum) / 2.;
        for (int i = 0; i < this.x.length; i++) {
            double zz = Math.random();
            if (zz < 0.5)
                this.x[i] = e1.x[i];
            else
                this.x[i] = e2.x[i];
        }
    }

    public double[] getX() {
        return x;
    }

    public double getSignum() {
        return signum;
    }

    public double getFitness() {
        return fitness;
    }
}

