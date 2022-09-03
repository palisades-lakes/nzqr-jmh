package nzqr.jmh.benchmarks.arithmetic;

import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import nzqr.java.accumulators.Accumulator;
import nzqr.java.accumulators.BigFloatAccumulator;
import nzqr.java.prng.Generator;
import nzqr.java.prng.Generators;
import nzqr.java.test.Common;

/** Benchmark operations on <code>double[]</code>.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-10
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

  Accumulator exact;
  // exact value(s)
  double[] truth;

  @Param({
    //"nzqr.java.accumulators.DoubleAccumulator",
    //"nzqr.java.accumulators.KahanAccumulator0",
    "nzqr.java.accumulators.RationalFloatAccumulator",
    "nzqr.java.accumulators.BigFloatAccumulator",
    //"nzqr.jmh.accumulators.BigFloat0Accumulator",
    //"nzqr.java.accumulators.KahanAccumulator",
    //"nzqr.test.java.accumulators.EFloatAccumulator",
    //"nzqr.test.java.accumulators.ERationalAccumulator",
    //"nzqr.java.accumulators.DistilledAccumulator",
    //"nzqr.java.accumulators.ZhuHayesAccumulator",
    //"nzqr.jmh.accumulators.ZhuHayesGCAccumulator",
    //"nzqr.jmh.accumulators.ZhuHayesGCBranch",
    //"nzqr.jmh.accumulators.ZhuHayesBranch",
    //"nzqr.jmh.accumulators.BigDecimalAccumulator",
    //"nzqr.jmh.accumulators.BigFractionAccumulator",
    //"nzqr.jmh.accumulators.DoubleFmaAccumulator",
    //"nzqr.jmh.accumulators.KahanFmaAccumulator",
    //"nzqr.jmh.accumulators.FloatAccumulator",
    //"nzqr.jmh.accumulators.FloatFmaAccumulator",
    //"nzqr.jmh.accumulators.RatioAccumulator",
  })
  String accumulator;
  Accumulator acc;

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
  int dim;

  double[] x0;
  double[] x1;

  // estimated value(s)
  double[] p;

  //--------------------------------------------------------------
  /** This is what is timed. */

  public abstract double[] operation (final Accumulator ac,
                                      final double[] z0,
                                      final double[] z1);

  //--------------------------------------------------------------

  /** Re-initialize the prngs with the same seeds for each
   * <code>(accumulator,dim)</code> pair.
   */
  @Setup(Level.Trial)
  public final void trialSetup () {
    gen = Generators.make(generator,dim);
    //exact = EFloatAccumulator.make();
    exact = BigFloatAccumulator.make();
    assert exact.isExact();
    acc = Common.makeAccumulator(accumulator); }

  @Setup(Level.Invocation)
  public final void invocationSetup () {
    x0 = (double[]) gen.next();
    x1 = (double[]) gen.next();
    truth = operation(exact,x0,x1); }

  @TearDown(Level.Invocation)
  public final void invocationTeardown () {
    assert
    0.0 == exact.clear().addL1Distance(truth,p).doubleValue(); }

  // not needed while testing exact methods
  //  @TearDown(Level.Trial)
  //  public final void teardownTrial () {
  //    //System.out.println("teardownTrial");
  //    final int n = truth.size();
  //    assert n == est.size();
  //    final String aname = Classes.className(acc);
  //    final String bname =
  //      Classes.className(this).replace("_jmhType","");
  //    final File parent = new File("output/" + bname);
  //    parent.mkdirs();
  //    final File f = new File(parent,
  //      aname + "-" + generator + "-" + dim + "-" + now() + ".csv");
  //    PrintWriter pw = null;
  //    try {
  //      pw = new PrintWriter(f);
  //      pw.println("generator,benchmark,accumulator,dim,truth,est");
  //      for (int i=0;i<n;i++) {
  //        pw.println(
  //          generator + "," + bname + "," + aname + "," + dim + ","
  //            + truth.get(i) + "," + est.get(i)); } }
  //    catch (final FileNotFoundException e) {
  //      throw new RuntimeException(e); }
  //    finally { if (null != pw) { pw.close(); } } }

  @Benchmark
  public final double[] bench () {
    p = operation(acc,x0,x1);
    return p; }

  //--------------------------------------------------------------
  //  /** <pre>
  //   * java -cp target\benchmarks.jar nzqr.jmh.Base
  //   * </pre>
  //   */

  //  public static void main (final String[] args)
  //    throws RunnerException {
  //    System.out.println("args=" + Arrays.toString(args));
  //    final Options opt =
  //      Defaults.options("Generators","TotalDot|TotalL2Norm|TotalSum");
  //    System.out.println(opt.toString());
  //    new Runner(opt).run(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
