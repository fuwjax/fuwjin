package org.fuwjax.test;

import java.util.Random;

import org.fuwjax.brand.Brand;
import org.junit.Assert;
import org.junit.Test;

public class BrandTest {
	@Test
	public void testExample() {
		double[] p = new double[] { 0.01, 0.1, 0.02, 0.03, 0.2, 0.04, 0.15, 0.25, 0.05, 0.15 };
		Brand brand = new Brand(p);
		System.out.println(brand);
		assertCorrectDistribution(brand, p, 1000, 21.67); // Chi-squared, 9 degrees of freedom, 99th percentile
	}

	@Test
	public void testLargeArray() {
		double[] p = normalize(random(new double[101]));
		Brand brand = new Brand(p);
		System.out.println(brand);
		assertCorrectDistribution(brand, p, 10000, 136); // Chi-squared, 100 degrees of freedom, 99th percentile
	}

	@Test
	public void testInverseArray() {
		double[] p = normalize(inverse(new double[100]));
		Brand brand = new Brand(p);
		System.out.println(brand);
		assertCorrectDistribution(brand, p, 10000, 136); // Chi-squared, 100 degrees of freedom, 99th percentile
	}

	@Test
	public void testInverseSquareArray() {
		double[] p = normalize(inverseSquare(new double[99]));
		Brand brand = new Brand(p);
		System.out.println(brand);
		assertCorrectDistribution(brand, p, 10000, 136); // Chi-squared, 100 degrees of freedom, 99th percentile
	}

	@Test
	public void testExponentArray() {
		double[] p = normalize(exponent(new double[102]));
		Brand brand = new Brand(p);
		System.out.println(brand);
		assertCorrectDistribution(brand, p, 10000, 136); // Chi-squared, 100 degrees of freedom, 99th percentile
	}

	@Test
	public void testReallyLargeArray() {
		double[] p = normalize(random(new double[1000]));
		Brand brand = new Brand(p);
		System.out.println(brand);
		assertCorrectDistribution(brand, p, 1000000, 5000); // no idea what the Chi-squared, 999 degrees of freedom, 99th percentile is...
	}
	
	@Test
	public void testRidiculouslyLargeArray() {
		double[] p = normalize(random(new double[100000]));
		Brand brand = new Brand(p);
		System.out.println(brand);
		assertCorrectDistribution(brand, p, 10000000, 500000); // no idea what the Chi-squared, 999 degrees of freedom, 99th percentile is...
	}
	
	@Test
	public void testAbsurdlyLargeArray() {
		double[] p = normalize(random(new double[1000000]));
		Brand brand = new Brand(p);
		System.out.println(brand);
		assertCorrectDistribution(brand, p, 10000000, 5000000); // no idea what the Chi-squared, 999 degrees of freedom, 99th percentile is...
	}
	
	private double[] random(double[] p) {
		Random random = new Random();
		for(int i = 0;i<p.length;i++){
			p[i] = random.nextDouble();
		}
		return p;
	}

	private double[] inverse(double[] p) {
		for(int i = 0;i<p.length;i++){
			p[i] = 1.0/(1+i);
		}
		return p;
	}

	private double[] exponent(double[] p) {
		for(int i = 0;i<p.length;i++){
			p[i] = Math.pow(.5, i);
		}
		return p;
	}

	private double[] inverseSquare(double[] p) {
		for(int i = 0;i<p.length;i++){
			p[i] = 1.0/(1+i)/(1+i);
		}
		return p;
	}

	private double[] normalize(double[] p) {
		double sum = 0;
		for(int i = 0;i<p.length;i++){
			sum += p[i];
		}
		for(int i = 0;i<p.length;i++){
			p[i] = p[i]/sum;
		}
		return p;
	}
	
	private void assertCorrectDistribution(Brand brand, double[] p, int reps, double chiSquaredTest) {
		int[] c = new int[p.length];
		for (int i = 0; i < reps; i++) {
			c[brand.nextInteger()]++;
		}
		double X = 0;
		for (int i = 0; i < p.length; i++) {
			double diff = c[i] - p[i] * reps;
			X += diff * diff / p[i] / reps;
		}
		Assert.assertTrue("random sample is unlikely to be correctly generated", X < chiSquaredTest);
	}
}
