package nzqr.jmh.benchmarks.accumulate;

/** <pre>
 * j nzqr.jmh.Totals2
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-09-06
 */

public final class Totals2 {
  public static final void main (final String[] args)  {
    Defaults.run("TotalL2Norm");
    Defaults.run("TotalDot");
    Defaults.run("TotalL2Distance");
  } }
