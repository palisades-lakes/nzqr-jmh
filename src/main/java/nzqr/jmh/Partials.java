package nzqr.jmh;

/** <pre>
 * java -cp target\benchmarks.jar nzqr.jmh.Partials
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-09-08
 */


public final class Partials {
  public static final void main (final String[] args)  {
    Defaults.run("PartialDots");
    Defaults.run("PartialL2s");
    //Defaults.run("PartialL2Distances");
    Defaults.run("PartialSums");
    Defaults.run("PartialL1s");
    Defaults.run("PartialL1Distances");
  } }
