package nzqr.jmh;

/** <pre>
 * java -cp target\benchmarks.jar nzqr.jmh.Totals
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-06-16
 */

public final class Totals {
  public static final void main (final String[] args)  {
    Defaults.run("TotalSum");
    Defaults.run("TotalL1Norm");
    Defaults.run("TotalL2Norm");
    Defaults.run("TotalDot");
    Defaults.run("TotalL2Distance");
    Defaults.run("TotalL1Distance");
  } }
