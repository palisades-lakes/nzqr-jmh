package nzqr.jmh.polynomial;

import nzqr.java.linear.Dn;
import nzqr.java.polynomial.Polynomial;
import nzqr.jmh.numbers.BigFloat0;

/** Approximate cubic {@link Polynomial} 
 * using <code>BigFloat</code>.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-10
 */

@SuppressWarnings("unchecked")
public final class MonomialBigFloat0 
implements Polynomial<BigFloat0> {

  private final BigFloat0[] _a;

  //--------------------------------------------------------------

  @Override
  public final boolean isExact () { return true; }

  //--------------------------------------------------------------

  private static final BigFloat0 fma (final double a,
                                      final BigFloat0 b,
                                      final BigFloat0 c) {
    return b.multiply(a).add(c); }

  @Override
  public final BigFloat0 value (final double x) {
    int i = _a.length-1;
    if (0>i) { return BigFloat0.ZERO; }
    BigFloat0 tmp = _a[i--];
    while (0<=i) { tmp = fma(x,tmp,_a[i--]); }
    return tmp; }

  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) {
    return value(x).doubleValue(); }

  //--------------------------------------------------------------
  /** Unsafe, retains reference to <code>a</code>. */

  private MonomialBigFloat0 (final double[] a) {
    final int n = a.length;
    assert 0.0!=a[a.length-1];
    final BigFloat0[] b = new BigFloat0[n];
    for (int i=0;i<n;i++) { b[i] = BigFloat0.valueOf(a[i]); }
    _a=b; }

  public static final MonomialBigFloat0 make (final double[] a) {
    return new MonomialBigFloat0(Dn.stripTrailingZeros(a)); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

