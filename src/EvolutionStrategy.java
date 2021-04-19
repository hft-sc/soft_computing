public class EvolutionStrategy {

    private final int dimension;
    /**
     * mue in formula
     */
    private final int populationSize;
    /**
     * lambda in formula
     */
    private final int childrenSize;
    private final int maxIter;

    /**
     * parents are part of candidates for the next generation
     */
    private final boolean elitist;
    private final boolean maximize;

    private Individual[] population;
    private final Individual[] children;

    public EvolutionStrategy(int dimension, int populationSize, int childrenSize, int maxIter, boolean elitist, boolean maximize) {

        this.dimension = dimension;
        this.populationSize = populationSize;
        this.childrenSize = childrenSize;
        this.maxIter = maxIter;
        this.elitist = elitist;
        this.maximize = maximize;

        population = new Individual[populationSize];
        children = new Individual[childrenSize];

        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual(dimension);
            individual.calculateFitness();
            //System.out.println(i + " parent-> " + individual.fitness);
            population[i] = individual;
        }
        if (elitist) sort(population);
    }

    public static void main(String[] args) {
        EvolutionStrategy strategy = new EvolutionStrategy(50, 100, 700, 3000, false, false);
        strategy.run();
    }

    private void run() {
        for (int i = 0; i < maxIter; i++) {

            System.out.println(i + " -> " + population[0].fitness + " " + population[0].x[0] + " " + population[0].signum + " " + Individual.learnRate);

            for (int k = 0; k < childrenSize; k++) {
                int p1 = (int) (population.length * Math.random());
                int p2 = (int) (population.length * Math.random());
                Individual individual = new Individual(dimension);

                individual.recombine(population[p1], population[p2]);
                individual.mutate();

                individual.calculateFitness();

                children[k] = individual;
            }

            if (elitist) replacementElitist();
            else replacement();
        }
    }

    private void replacement() {
        sort(children);
        if (populationSize >= 0) System.arraycopy(children, 0, population, 0, populationSize);
    }

    private void replacementElitist() {
        sort(children);
        Individual[] popNeu = new Individual[populationSize];
        int i = 0;
        int j = 0;
        for (int k = 0; k < populationSize; k++) {
            if (maximize) {
                if (children[j].fitness >= population[i].fitness) {//maximize
                    popNeu[k] = children[j];
                    j++;
                } else {
                    popNeu[k] = population[i];
                    i++;
                }
            } else {
                if (children[j].fitness <= population[i].fitness) {//maximize
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

    private void sort(Individual[] list) {
        mergesort(list, 0, list.length - 1);
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
                if ((k > re) || ((j < mi) && (s[j].fitness < s[k].fitness))) {
                    temp[i] = s[j];
                    j++;
                } else {
                    temp[i] = s[k];
                    k++;
                }
            } else {
                if ((k > re) || ((j < mi) && (s[j].fitness > s[k].fitness))) {
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
