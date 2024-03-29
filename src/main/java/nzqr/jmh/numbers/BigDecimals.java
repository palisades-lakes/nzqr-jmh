package nzqr.jmh.numbers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.CollectionSampler;
import org.apache.commons.rng.sampling.distribution.ContinuousSampler;
import org.apache.commons.rng.sampling.distribution.ContinuousUniformSampler;

import nzqr.java.algebra.OneSetOneOperation;
import nzqr.java.algebra.OneSetTwoOperations;
import nzqr.java.algebra.Set;
import nzqr.java.numbers.Doubles;
import nzqr.java.prng.Generator;
import nzqr.java.prng.GeneratorBase;

/** The set of rational numbers represented by
 * <code>BigDecimal</code>.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-15
 */
@SuppressWarnings({"unchecked","static-method"})
public final class BigDecimals implements Set {

  //--------------------------------------------------------------
  // operations for algebraic structures over BigDecimals.
  //--------------------------------------------------------------

  // TODO: is consistency with other algebraic structure classes
  // worth the indirection?

  private final BigDecimal add (final BigDecimal q0,
                                final BigDecimal q1) {
    //assert contains(q0);
    //assert contains(q1);
    return q0.add(q1); }

  public final BinaryOperator<BigDecimal> adder () {
    return new BinaryOperator<> () {
      @Override
      public final String toString () { return "BD.add()"; }
      @Override
      public final BigDecimal apply (final BigDecimal q0,
                                     final BigDecimal q1) {
        return BigDecimals.this.add(q0,q1); } }; }

  //--------------------------------------------------------------

  public final BigDecimal additiveIdentity () {
    return BigDecimal.valueOf(0L); }

  //--------------------------------------------------------------

  // TODO: is consistency with other algebraic structure classes
  // worth the indirection?

  private final BigDecimal negate (final BigDecimal q) {
    //assert contains(q);
    return q.negate(); }

  public final UnaryOperator<BigDecimal> additiveInverse () {
    return new UnaryOperator<> () {
      @Override
      public final String toString () { return "BD.negate()"; }
      @Override
      public final BigDecimal apply (final BigDecimal q) {
        return BigDecimals.this.negate(q); } }; }

  //--------------------------------------------------------------

  private final BigDecimal multiply (final BigDecimal q0,
                                     final BigDecimal q1) {
    //assert contains(q0);
    //assert contains(q1);
    return q0.multiply(q1); }

  public final BinaryOperator<BigDecimal> multiplier () {
    return new BinaryOperator<>() {
      @Override
      public final String toString () { return "BD.multiply()"; }
      @Override
      public final BigDecimal apply (final BigDecimal q0,
                                     final BigDecimal q1) {
        return BigDecimals.this.multiply(q0,q1); } }; }

  //--------------------------------------------------------------

  public final BigDecimal multiplicativeIdentity () {
    return BigDecimal.ONE; }

  //--------------------------------------------------------------

  private final BigDecimal reciprocal (final BigDecimal q) {
    //assert contains(q);
    // only a partial inverse
    if (BigDecimal.valueOf(0L).equals(q)) { return null; }
    return BigDecimal.ONE.divide(q);  }


  public final UnaryOperator<BigDecimal> multiplicativeInverse () {
    return new UnaryOperator<> () {
      @Override
      public final String toString () { return "BD.inverse()"; }
      @Override
      public final BigDecimal apply (final BigDecimal q) {
        return BigDecimals.this.reciprocal(q); } }; }

  //--------------------------------------------------------------
  // Set methods
  //--------------------------------------------------------------

  @Override
  public final boolean contains (final Object element) {
    return element instanceof BigDecimal; }

  //--------------------------------------------------------------

  /** Note: BigDecimal.equal doesn't test for equality as rational
   * numbers.
   */
  public final boolean equals (final BigDecimal q0,
                               final BigDecimal q1) {
    if (q0 == q1) { return true; }
    if (null == q0) {
      if (null == q1) { return true; }
      return false; }
    if (null == q1) { return false; }
    final int c = q0.compareTo(q1);
    return 0 == c; }

  @Override
  public final BiPredicate equivalence () {
    return new BiPredicate<BigDecimal,BigDecimal>() {
      @Override
      public final String toString () { return "BD.equals()"; }
      @Override
      public final boolean test (final BigDecimal q0,
                                 final BigDecimal q1) {
        return BigDecimals.this.equals(q0,q1); } }; }

  //--------------------------------------------------------------

  @Override
  public final Supplier generator (final Map options) {
    final UniformRandomProvider urp = Set.urp(options);
    final Generator bfs = BigDecimals.bigDecimalGenerator(urp);
    return
      new Supplier () {
      @Override
      public final Object get () { return bfs.next(); } }; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final int hashCode () { return 0; }

  // singleton?
  @Override
  public final boolean equals (final Object that) {
    return that instanceof BigDecimals; }

  @Override
  public final String toString () { return "BD"; }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------
  /** Intended primarily for testing. Sample a random double
   * (see nzqr.java.prng.DoubleSampler)
   * and convert to <code>BigDecimal</code>
   * with DOUBLE_P probability;
   * otherwise return <code>BigDecimal.valueOf(0L)</code> or
   * {@link BigDecimal#ONE}, <code>BigDecimal.valueOf(10L)</code>,
   * with equal probability (these are potential edge cases).
   *
   * TODO: sample rounding modes?
   */

  public static final Generator
  bigDecimalGenerator (final UniformRandomProvider urp) {
    final double dp = 0.9;
    return new GeneratorBase ("bigDecimalGenerator") {
      private final ContinuousSampler choose =
        new ContinuousUniformSampler(urp,0.0,1.0);
      private final Generator fdg = Doubles.finiteGenerator(urp);
      private final CollectionSampler edgeCases =
        new CollectionSampler(
          urp,
          List.of(
            BigDecimal.valueOf(0L),
            BigDecimal.ONE,
            BigDecimal.valueOf(10L)));
      @Override
      public Object next () {
        final boolean edge = choose.sample() > dp;
        if (edge) { return edgeCases.sample(); }
        return new BigDecimal(fdg.nextDouble()); } }; }

  public static final Generator
  bigDecimalGenerator (final int n,
                       final UniformRandomProvider urp) {
    return new GeneratorBase ("bigDecimalGenerator:" + n) {
      final Generator g = BigDecimals.bigDecimalGenerator(urp);
      @Override
      public final Object next () {
        final BigDecimal[] z = new BigDecimal[n];
        for (int i=0;i<n;i++) { z[i] = (BigDecimal) g.next(); }
        return z; } }; }

  private BigDecimals () { }

  private static final BigDecimals SINGLETON =
    new BigDecimals();

  public static final BigDecimals get () { return SINGLETON; }

  //--------------------------------------------------------------
  // pre-defined structures
  //--------------------------------------------------------------

  public static final OneSetOneOperation ADDITIVE_MAGMA =
    OneSetOneOperation.magma(get().adder(),get());

  public static final OneSetOneOperation MULTIPLICATIVE_MAGMA =
    OneSetOneOperation.magma(get().multiplier(),get());

  public static final OneSetTwoOperations RING =
    OneSetTwoOperations.commutativeRing(
      get().adder(),
      get().additiveIdentity(),
      get().additiveInverse(),
      get().multiplier(),
      get().multiplicativeIdentity(),
      get());


}
//--------------------------------------------------------------

