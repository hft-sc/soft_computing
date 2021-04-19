public class EvolutionStrategy {

    private final int dimension;
    private final int mue;
    private final int lambda;
    private final int maxIter;

    /**
     * parents are part of candidates for the next generation
     */
    private final boolean elitist;
    private final boolean maximize;

    private Individual[] population;
    private final Individual[] children;

    public EvolutionStrategy(int dimension, int mue, int lambda, int maxIter, boolean elitist, boolean maximize) {

        this.dimension = dimension;
        this.mue = mue;
        this.lambda = lambda;
        this.maxIter = maxIter;
        this.elitist = elitist;
        this.maximize = maximize;

        population = new Individual[mue];
        children = new Individual[lambda];

        for (int i = 0; i < mue; i++) {
            Individual indi = new Individual(dimension);
            indi.fitness();
            //System.out.println(i + " elter-> " + indi.fitness);
            population[i] = indi;
        }
        if (elitist) sort(population);
    }

    public static void main(String[] args) {
        EvolutionStrategy strategy = new EvolutionStrategy(50, 100, 700, 3000, false, false);
        strategy.run();
    }

    private void run() {

        for (int i = 0; i < maxIter; i++) {

            System.out.println(i + " -> " + population[0].getFitness() + " " + population[0].getX()[0] + " " + population[0].getSignum() + " " + Individual.learnRate);

            for (int k = 0; k < lambda; k++) {
                int p1 = (int) (population.length * Math.random());
                int p2 = (int) (population.length * Math.random());
                Individual indi = new Individual(dimension);

                indi.recombine(population[p1], population[p2]);
                indi.mutate();

                indi.fitness();

                children[k] = indi;
            }

            if (elitist) replacementElitaer();
            else replacement();
        }
    }

    private void replacement() {
        sort(children);
        if (mue >= 0) System.arraycopy(children, 0, population, 0, mue);
    }

    private void replacementElitaer() {
        sort(children);
        Individual[] popNeu = new Individual[mue];
        int i = 0;
        int j = 0;
        for (int k = 0; k < mue; k++) {
            if (maximize) {
                if (children[j].getFitness() >= population[i].getFitness()) {//maximierung
                    popNeu[k] = children[j];
                    j++;
                } else {
                    popNeu[k] = population[i];
                    i++;
                }
            } else {
                if (children[j].getFitness() <= population[i].getFitness()) {//maximierung
                    popNeu[k] = children[j];
                    j++;
                } else {
                    popNeu[k] = population[i];
                    i++;
                }
            }
        }
        population = popNeu;
    }

    private void sort(Individual[] liste) {
        mergesort(liste, 0, liste.length - 1);
    }

    private void mergesort(Individual[] s, int l, int r) {
        if (l < r) {
            int m = (l + r + 1) / 2;
            mergesort(s, l, m - 1);
            mergesort(s, m, r);
            merge(s, l, m, r);
        }
    }

    private void merge(Individual[] s, int li, int mi, int re) {
        Individual[] temp = new Individual[re - li + 1];
        for (int i = 0, j = li, k = mi; i < temp.length; i++) {
            if (!maximize) {
                if ((k > re) || ((j < mi) && (s[j].getFitness() < s[k].getFitness()))) {
                    temp[i] = s[j];
                    j++;
                } else {
                    temp[i] = s[k];
                    k++;
                }
            } else {
                if ((k > re) || ((j < mi) && (s[j].getFitness() > s[k].getFitness()))) {
                    temp[i] = s[j];
                    j++;
                } else {
                    temp[i] = s[k];
                    k++;
                }
            }
        }
        System.arraycopy(temp, 0, s, li, temp.length);
    }
}
