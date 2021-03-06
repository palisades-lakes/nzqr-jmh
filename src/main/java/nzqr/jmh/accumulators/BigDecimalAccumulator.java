package nzqr.jmh.accumulators;

import java.math.BigDecimal;

import nzqr.java.accumulators.Accumulator;

/** Naive sum of <code>double</code> values with BigDecimal
 * accumulator (for testing).
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-04-21
 */
public final class BigDecimalAccumulator

implements Accumulator<BigDecimalAccumulator> {

  private BigDecimal _sum;

  //--------------------------------------------------------------

  @Override
  public final boolean isExact () { return true; }

  @Override
  public final boolean noOverflow () { return true; }

  // start with only immediate needs

  @Override
  public final BigDecimalAccumulator clear () {
    _sum = BigDecimal.ZERO;
    return this; }

  @Override
  public final Object value () { return _sum; }

  @Override
  public final double doubleValue () {
    return _sum.doubleValue(); }

  @Override
  public final BigDecimalAccumulator add (final double z) {
    assert Double.isFinite(z);
    _sum = _sum.add(new BigDecimal(z));
    return this; }

  //  @Override
  //  public final BigDecimalAccumulator addAll (final double[] z)  {
  //    for (final double zi : z) {
  //      _sum = _sum.add(new BigDecimal(zi)); }
  //    return this; }

  @Override
  public final BigDecimalAccumulator add2 (final double z) {
    assert Double.isFinite(z);
    final BigDecimal bd = new BigDecimal(z);
    _sum = _sum.add(bd.multiply(bd));
    return this; }

  @Override
  public final BigDecimalAccumulator addProduct (final double z0,
                                                 final double z1) {
    assert Double.isFinite(z0);
    assert Double.isFinite(z1);
    _sum = _sum.add(
      new BigDecimal(z0)
      .multiply(
        new BigDecimal(z1)));
    return this; }

  //  @Override
  //  public final BigDecimalAccumulator addProducts (final double[] z0,
  //                                        final double[] z1)  {
  //    final int n = z0.length;
  //    assert n == z1.length;
  //    for (int i=0;i<n;i++) { addProduct(z0[i],z1[i]); }
  //    return this; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private BigDecimalAccumulator () { super(); clear(); }

  public static final BigDecimalAccumulator make () {
    return new BigDecimalAccumulator(); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
