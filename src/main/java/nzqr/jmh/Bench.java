package nzqr.jmh;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import com.google.common.collect.ImmutableMap;

import nzqr.java.algebra.Set;
import nzqr.java.algebra.Sets;
import nzqr.java.algebra.Structure;
import nzqr.java.algebra.TwoSetsOneOperation;
import nzqr.java.linear.Dn;
import nzqr.java.linear.Fn;
import nzqr.java.linear.Qn;
import nzqr.java.prng.PRNG;
import nzqr.jmh.linear.BigDecimalsN;
import nzqr.jmh.linear.BigFractionsN;
import nzqr.jmh.linear.ERationalsN;
import nzqr.jmh.linear.RatiosN;

// java -ea --illegal-access=warn -jar target/benchmarks.jar NaiveSum

/** Benchmark algebraic structure tests.
 *
 * <pre>
 * java -jar target\benchmarks.jar qnSpaceTest
 * java -jar target\benchmarks.jar .*SpaceTest
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-03-05
 */
@SuppressWarnings("unchecked")
public class Bench {

  //--------------------------------------------------------------
  // benchmarking structure validation --- not that useful
  //--------------------------------------------------------------

  private static final boolean testMembership (final Set set,
                                               final int ntrys) {
    final Supplier g =
      set.generator(
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b("seeds/Well44497b-2019-01-05.txt")));
    for (int i=0; i<ntrys; i++) {
      final Object x = g.get();
      if (! set.contains(x)) { return false; } }
    return true; }

  private static final boolean testEquivalence (final Set set,
                                                final int ntrys) {
    final Supplier g =
      set.generator(
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b("seeds/Well44497b-2019-01-07.txt")));
    for (int i=0; i<ntrys; i++) {
      if (! Sets.isReflexive(set,g)) { return false; }
      if (! Sets.isSymmetric(set,g)) { return false; } }
    return true; }

  private static final boolean setTests (final Set set,
                                         final int ntrys) {
    return
      testMembership(set,ntrys)
      &&
      testEquivalence(set,ntrys); }

  //--------------------------------------------------------------

  private static final boolean
  structureTests (final Structure s,
                  final int ntrys) {
    final Map<Set,Supplier> generators =
      s.generators(
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b("seeds/Well44497b-2019-01-09.txt")));
    for(final Predicate law : s.laws()) {
      for (int i=0; i<ntrys; i++) {
        if (! law.test(generators)) { return false; } } }
    return true; }

  //--------------------------------------------------------------

  private static final int TRYS = 1;
  private static final int[] DIMENSIONS =
    new int[] { 17 * 1023, };

  public static final boolean
  spaceTest (final TwoSetsOneOperation space) {
    if (! setTests(space,TRYS)) { return false; }
    if (! structureTests(space,TRYS)) { return false; }
    return true; }

  //--------------------------------------------------------------

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean bdnSpaceTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(BigDecimalsN.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean erSpaceTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(ERationalsN.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean bfnSpaceTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(BigFractionsN.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean rationSpaceTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(RatiosN.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean qnSpaceTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(Qn.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean dnSpaceTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(Dn.space(n))) { return false; } }
    return true; }

  @SuppressWarnings({ "static-method" })
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public final boolean fnSpaceTest () {
    for (final int n : DIMENSIONS) {
      if (! spaceTest(Fn.space(n))) { return false; } }
    return true; }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
