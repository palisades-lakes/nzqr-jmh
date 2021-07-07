package nzqr.jmh.test.polynomial;

import org.junit.jupiter.api.Test;

import nzqr.java.test.Common;
import nzqr.jmh.polynomial.MonomialBigFloat0;
import nzqr.jmh.polynomial.MonomialDoubleBF0;

//----------------------------------------------------------------
/** Test desired properties of monimial calculators.
 * <p>
 * <pre>
 * mvn -q -Dtest=nzqr/java/test/polynomial/MonomialTest test > MT.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-10
 */

public final class MonomialTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void dbf () { 
    Common.monomial(MonomialDoubleBF0.class); } 

  @SuppressWarnings({ "static-method" })
  @Test
  public final void bf () { 
    Common.monomial(MonomialBigFloat0.class); } 

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
