package nzqr.jmh.benchmarks.accumulate;

/** <pre>
 * mvn install & java -cp target\benchmarks.jar nzqr.jmh.benchmarks.accumulate.Totals
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2026-05-04
 */

public final class Totals {
  @SuppressWarnings("unused")
  public static final void main (final String[] args)  {
//    Defaults.run("TotalSum");
//    Defaults.run("TotalL1Norm");
//    Defaults.run("TotalL2Norm");
    Defaults.run("TotalDot");
//    Defaults.run("TotalL2Distance");
//    Defaults.run("TotalL1Distance");
  } }
