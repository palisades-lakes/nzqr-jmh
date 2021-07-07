package nzqr.jmh.polynomial;

import nzqr.java.linear.Dn;
import nzqr.java.polynomial.Polynomial;
import nzqr.jmh.numbers.BigFloat0;

/** Approximate cubic {@link Polynomial} 
 * using <code>double</code> coefficients with 
 * {@link BigFloat0} accumulator for exactness.
 * 
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-10
 */

@SuppressWarnings("unchecked")
public final class MonomialDoubleBF0 
implements Polynomial<BigFloat0> {

  private final double[] _a;

  //--------------------------------------------------------------

  @Override
  public final boolean isExact () { return true; }

  //--------------------------------------------------------------

  @Override
  public final BigFloat0 value (final double x) {
    int i = _a.length-1;
    if (0>i) { return BigFloat0.ZERO; }
    if (0==i) { return BigFloat0.valueOf(_a[0]); }
    BigFloat0 tmp = BigFloat0.axpy(x,_a[i],_a[i-1]);
    //System.out.println(tmp);
    //System.out.println(Double.toHex)
    i=i-2;
    for (;0<=i;i--) { tmp = BigFloat0.axpy(x,tmp,_a[i]); }
    return tmp; }

  //--------------------------------------------------------------

  @Override
  public final double doubleValue (final double x) {
    final int n = _a.length;
    if (0==n) { return 0.0; }
    if (1==n) { return _a[0]; }
    return value(x).doubleValue(); }

  //--------------------------------------------------------------
  /** Unsafe, retains reference to <code>a</code>. */
  
  private MonomialDoubleBF0 (final double[] a) {
    assert 0.0!=a[a.length-1];
    _a=a; }

  public static final MonomialDoubleBF0 make (final double[] a) {
    return new MonomialDoubleBF0(Dn.copyWoutTrailingZeros(a)); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

