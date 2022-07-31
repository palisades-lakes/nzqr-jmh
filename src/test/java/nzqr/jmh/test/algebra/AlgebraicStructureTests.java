package nzqr.jmh.test.algebra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

import nzqr.java.algebra.Set;
import nzqr.java.algebra.Structure;
import nzqr.java.prng.PRNG;
import nzqr.jmh.numbers.BigDecimals;
import nzqr.jmh.numbers.BigFractions;
import nzqr.jmh.numbers.ERationals;
import nzqr.jmh.numbers.Ratios;

//----------------------------------------------------------------
/** Common code for testing sets.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2021-07-08
 */

@SuppressWarnings("unchecked")
public final class AlgebraicStructureTests {

  private static final int TRYS = 4;
  static final int SPACE_TRYS = 3;

  //--------------------------------------------------------------

  private static final void
  structureTests (final Structure s,
                  final int n) {
    SetTests.tests(s);
    final Map<Set,Supplier> generators =
      s.generators(
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b("seeds/Well44497b-2019-01-09.txt")));
    for(final Predicate law : s.laws()) {
      for (int i=0; i<n; i++) {
        final boolean result = law.test(generators);
        assertTrue(result); } } }

  //--------------------------------------------------------------

  @SuppressWarnings({ "static-method" })
  @Test
  public final void tests () {

    structureTests(ERationals.ADDITIVE_MAGMA,TRYS);
    structureTests(ERationals.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(ERationals.FIELD,TRYS);

    structureTests(BigDecimals.ADDITIVE_MAGMA,TRYS);
    structureTests(BigDecimals.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(BigDecimals.RING,TRYS);

    structureTests(BigFractions.ADDITIVE_MAGMA,TRYS);
    structureTests(BigFractions.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(BigFractions.FIELD,TRYS);

    structureTests(Ratios.ADDITIVE_MAGMA,TRYS);
    structureTests(Ratios.MULTIPLICATIVE_MAGMA,TRYS);
    structureTests(Ratios.FIELD,TRYS);

  }


  //--------------------------------------------------------------
}
//--------------------------------------------------------------
