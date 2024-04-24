package eu.veldsoft.one.max;

import org.apache.commons.math3.genetics.BinaryChromosome;
import org.apache.commons.math3.genetics.BinaryMutation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.CrossoverPolicy;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.MutationPolicy;
import org.apache.commons.math3.genetics.RandomKeyMutation;
import org.apache.commons.math3.genetics.TournamentSelection;
import org.apache.commons.math3.genetics.UniformCrossover;
import org.apache.commons.math3.genetics.FixedGenerationCount;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Comparator;

public class App {
	public static void main(String[] args) {
		int length = 100;
		int generations = 100;
		int population = 100;

		GeneticAlgorithm ga = new GeneticAlgorithm(new UniformCrossover<Integer>(0.5), 0.9, new BinaryMutation(), 0.01,
				new TournamentSelection(3));

		class IntegerChromosome extends BinaryChromosome {
			public IntegerChromosome(List<Integer> representation) {
				super(representation);
			}

			@Override
			public BinaryChromosome newFixedLengthChromosome(List<Integer> representation) {
				representation = new ArrayList<>(representation);
				Collections.shuffle(representation);
				return new IntegerChromosome(representation);
			}

			@Override
			public double fitness() {
				return Collections.frequency(getRepresentation(), 1);
			}
		}

		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
		for (int i = 0; i < population; i++) {
			chromosomes
					.add(new IntegerChromosome(Stream
							.concat(Collections.nCopies(length / 5, 0).stream(),
									Collections.nCopies(length - length / 5, 1).stream())
							.collect(Collectors.toList())));
		}

		Chromosome fittest = ga.evolve(new ElitisticListPopulation(chromosomes, chromosomes.size() * 2, 0.05),
				new FixedGenerationCount(generations)).getFittestChromosome();
		;

		System.out.println("Fittest Individual: " + fittest);
	}
}
