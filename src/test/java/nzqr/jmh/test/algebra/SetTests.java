package nzqr.jmh.test.algebra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

import nzqr.java.Classes;
import nzqr.java.algebra.Set;
import nzqr.java.algebra.Sets;
import nzqr.java.prng.PRNG;
import nzqr.jmh.numbers.BigDecimals;
import nzqr.jmh.numbers.BigFractions;
import nzqr.jmh.numbers.ERationals;
import nzqr.jmh.numbers.Ratios;

//----------------------------------------------------------------
/** Common code for testing sets.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-15
 */

@SuppressWarnings("unchecked")
public final class SetTests {

  private static final int TRYS = 1023;

  private static final void testMembership (final Set set) {
    final Supplier g =
      set.generator(
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b("seeds/Well44497b-2019-01-05.txt")));
    for (int i=0; i<TRYS; i++) {
      //System.out.println("set=" + set);
      final Object x = g.get();
      //System.out.println("element=" + x);
      assertTrue(
        set.contains(x),
        () -> set.toString() + "\n does not contain \n" +
          Classes.className(x) + ": " +
          x); } }

  private static final void testEquivalence (final Set set) {
    final Supplier g =
      set.generator(
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b("seeds/Well44497b-2019-01-07.txt")));
    for (int i=0; i<TRYS; i++) {
      assertTrue(Sets.isReflexive(set,g));
      assertTrue(Sets.isSymmetric(set,g)); } }

  public static final void tests (final Set set) {
    testMembership(set);
    testEquivalence(set); }

  //--------------------------------------------------------------

  @SuppressWarnings({ "static-method" })
  @Test
  public final void eRationals () {
    SetTests.tests(ERationals.get()); }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void bigDecimals () {
    SetTests.tests(BigDecimals.get()); }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void bigFractions () {
    SetTests.tests(BigFractions.get()); }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void ratios () {
    SetTests.tests(Ratios.get()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
