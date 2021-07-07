package nzqr.jmh.accumulators;

import nzqr.java.accumulators.Accumulator;

/** Naive sum of <code>double</code> values, using fma.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-09
 */
strictfp
public final class StrictDoubleFmaAccumulator
implements Accumulator<StrictDoubleFmaAccumulator> {

  private double _sum;

  //--------------------------------------------------------------

  @Override
  public final boolean isExact () { return false; }

  @Override
  public final boolean noOverflow () { return false; }

  @Override
  public final Object value () {
    return Double.valueOf(doubleValue()); }

  @Override
  public final double doubleValue () { return _sum; }

  @Override
  public final StrictDoubleFmaAccumulator clear () {
    _sum = 0.0;
    return this; }

  @Override
  public final StrictDoubleFmaAccumulator add (final double z) {
    assert Double.isFinite(z);
    _sum += z;
    return this; }

  @Override
  public final StrictDoubleFmaAccumulator add2 (final double z) {
    assert Double.isFinite(z);
    _sum = StrictMath.fma(z,z,_sum);
    return this; }

  @Override
  public final StrictDoubleFmaAccumulator
  addProduct (final double z0,
              final double z1) {
    assert Double.isFinite(z0);
    assert Double.isFinite(z1);
    _sum = StrictMath.fma(z0,z1,_sum);
    return this; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private StrictDoubleFmaAccumulator () { super(); _sum = 0.0; }

  public static final StrictDoubleFmaAccumulator make () {
    return new StrictDoubleFmaAccumulator(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
