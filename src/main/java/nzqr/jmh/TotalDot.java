package nzqr.jmh;

import nzqr.java.accumulators.Accumulator;

/** <pre>
 * j nzqr.jmh.TotalDot
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-07-27
 */

public class TotalDot extends Base {

  @Override
  public final double[] operation (final Accumulator ac,
                                   final double[] z0,
                                   final double[] z1) {
    return new double[]
      { ac.clear().addProducts(z0,z1).doubleValue() }; }

  public static final void main (final String[] args)  {
    Defaults.run("TotalDot"); } }
