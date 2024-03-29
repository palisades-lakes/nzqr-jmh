package nzqr.jmh.benchmarks.accumulate;

import nzqr.java.accumulators.Accumulator;

/** <pre>
 * java -cp target\benchmarks.jar nzqr.jmh.PartialL2Distances
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-07-31
 */

@SuppressWarnings("unchecked")
public class PartialL2Distances extends Base {

  @Override
  public final double[] operation (final Accumulator ac,
                                   final double[] z0,
                                   final double[] z1) {
    return ac.clear().partialL2Distances(z0,z1); }

  public static final void main (final String[] args)  {
    Defaults.run("PartialL2Distances"); } }
