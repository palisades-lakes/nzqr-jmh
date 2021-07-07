package nzqr.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import nzqr.java.accumulators.Accumulator;
import nzqr.java.polynomial.Polynomial;
import nzqr.java.prng.Generator;
import nzqr.java.prng.Generators;
import nzqr.java.test.Common;
import nzqr.java.test.accumulators.EFloatAccumulator;
import nzqr.java.test.polynomial.MonomialEFloat;

/** Benchmark polynomial implementations.
 *
 *<pre>
 * j nzqr.jmh.MonomialBench
 * </pre>
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-11
 */

@SuppressWarnings("unchecked")
@State(Scope.Thread)
public class MonomialBench {

  //--------------------------------------------------------------

  //@Param({"exponential",})
  //@Param({"finite",})
  //@Param({"gaussian",})
  //@Param({"laplace",})
  @Param({"uniform",})
  //@Param({"exponential","finite","gaussian","laplace","uniform",})
  String agenerator;
  Generator agen;

  @Param({
    //"1",
    //"2",
    "3",
  })
  int degree;

  double[] a;

  @Param({
    "nzqr.java.polynomial.MonomialRationalFloat",
    "nzqr.java.polynomial.MonomialBigFloat",
    "nzqr.java.polynomial.MonomialDoubleBF",
    "nzqr.java.polynomial.MonomialDoubleRF",
    //"nzqr.java.polynomial.MonomialDouble",
  })
  String cName;
  Polynomial c;

  // estimated value(s)
  double[] p;

  //--------------------------------------------------------------

  Polynomial e;
  // exact value(s)
  double[] truth;

  //--------------------------------------------------------------

  @Param({
    //"33554433",
    //"8388609",
    //"4194303",
    //"2097153",
    //"1048575",
    //"524289",
    "131071",
    //"32767",
  })
  int dim;

  //@Param({"exponential",})
  //@Param({"finite",})
  @Param({"gaussian",})
  //@Param({"laplace",})
  //@Param({"uniform",})
  //@Param({"exponential","finite","gaussian","laplace","uniform",})
  String xgenerator;
  Generator xgen;
  double[] x;

  //--------------------------------------------------------------
  /** This is what is timed. */

  public static final double[] operation (final Polynomial imp,
                                          final double[] x) {
    return imp.doubleValue(x); }

  //--------------------------------------------------------------

  /** Re-initialize the prngs with the same seeds for each
   * <code>(cName,dim)</code> pair.
   */
  @Setup(Level.Trial)
  public final void trialSetup () {
    agen = Generators.make(xgenerator,degree+1);
    xgen = Generators.make(xgenerator,dim); }

  @Setup(Level.Invocation)
  public final void invocationSetup () {
    a = (double[]) agen.next();
    x = (double[]) xgen.next();
    e = MonomialEFloat.make(a);
    assert e.isExact();
    truth = operation(e,x);
    c = Common.makeMonomial(cName,a); }

  private final Accumulator acc = EFloatAccumulator.make();

  @TearDown(Level.Invocation)
  public final void invocationTeardown () {
    assert 
    0.0 == acc.clear().addL1Distance(truth,p).doubleValue(); }

  @Benchmark
  public final double[] bench () {
    p = operation(c,x);
    return p; }

  //--------------------------------------------------------------

  public static final void main (final String[] args)  {
    Defaults.run("MonomialBench"); } 

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
