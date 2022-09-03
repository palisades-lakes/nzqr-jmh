package nzqr.jmh.benchmarks.arithmetic;

import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import nzqr.java.numbers.Ringlike;
import nzqr.java.prng.Generator;
import nzqr.java.prng.Generators;
import nzqr.java.test.Common;

/** Benchmark arithmetic operations on various number classes.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2022-09-03
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
public abstract class Base {

  //--------------------------------------------------------------

  public static final void save (final double x,
                                 final List data) {
    data.add(Double.valueOf(x)); }

  //--------------------------------------------------------------

  //@Param({"exponential",})
  //@Param({"finite",})
  //@Param({"gaussian",})
  //@Param({"laplace",})
  @Param({"uniform",})
  //@Param({"exponential","finite","gaussian","laplace","uniform",})
  //@Param({"exponential","laplace","uniform",})
  String generator;
  Generator gen;

 
  @Param({
    "java.math.BigInteger",
    "nzqr.java.numbers.BoundedNatural",
  })
  String numberClass;
  Object number;

  //--------------------------------------------------------------

  @Param({
    //"33554433",
    //"8388609",
    //"4194303",
    //"2097153",
    "1048575",
    //"524289",
    //"131071",
  })
  Object min;
  Object max;

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
//  @Setup(Level.Trial)
//  public final void trialSetup () {
//    gen = Generators.make(generator,min,max); }

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
