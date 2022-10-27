package nzqr.jmh.benchmarks.arithmetic;

import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import nzqr.java.prng.Generator;
import nzqr.java.prng.Generators;
import nzqr.java.prng.PRNG;

/** Benchmark arithmetic operations on various number classes.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2022-10-27
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
public abstract class Base {

  //--------------------------------------------------------------

  public static final void save (final double x,
                                 final List data) {
    data.add(Double.valueOf(x)); }

  //--------------------------------------------------------------

  Generator gen;

 
  @Param({
    "java.math.BigInteger",
    "nzqr.java.numbers.BoundedNatural",
  })
  String numberClass;

  //--------------------------------------------------------------


  Object x0;
  Object x1;

  // value
  Object p;

  //--------------------------------------------------------------
  /** This is what is timed. */

  public abstract Object operation (final Object z0,
                                    final Object z1);

  //--------------------------------------------------------------

  /** Re-initialize the prngs with the same seeds for each
   * <code>(accumulator,dim)</code> pair.
   */
  @Setup(Level.Trial)
  public final void trialSetup () {
    gen = Generators.positiveBigIntegerGenerator(
      PRNG.well44497b("seeds/Well44497b-2019-01-07.txt")); }

  @Setup(Level.Invocation)
  public final void invocationSetup () {
    x0 = gen.next();
    x1 = gen.next(); }

  @Benchmark
  public final Object bench () {
    p = operation(x0,x1);
    return p; }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
