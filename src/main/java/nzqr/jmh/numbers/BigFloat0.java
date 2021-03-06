package nzqr.jmh.numbers;

import static nzqr.java.numbers.Doubles.doubleMergeBits;
import static nzqr.java.numbers.Floats.floatMergeBits;
import static nzqr.java.numbers.Numbers.loBit;

import java.util.Objects;

import nzqr.java.Exceptions;
import nzqr.java.numbers.BigFloat;
import nzqr.java.numbers.Doubles;
import nzqr.java.numbers.Floats;
import nzqr.java.numbers.Longs;
import nzqr.java.numbers.Numbers;
import nzqr.java.numbers.Ringlike;

/** A sign times a {@link NaturalBEI0} significand times 2 to a
 * <code>int</code> exponent.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-04
 */

@SuppressWarnings("unchecked")
public final class BigFloat0 implements Ringlike<BigFloat0> {

  //--------------------------------------------------------------
  // instance fields and methods
  //--------------------------------------------------------------

  private final boolean _nonNegative;
  public final boolean nonNegative () { return _nonNegative; }

  private final int _exponent;
  public final int exponent () { return _exponent; }

  // must always be non-negative
  private final NaturalBEI0 _significand;
  public final NaturalBEI0 significand () { return _significand; }

  //--------------------------------------------------------------
  // Ringlike
  //--------------------------------------------------------------

  @Override
  public final boolean isZero () {
    return significand().isZero(); }

  @Override
  public final boolean isOne () {
    return BigFloat0.this.isOne(); }

  //--------------------------------------------------------------

  @Override
  public final BigFloat0 negate () {
    if (isZero()) { return this; }
    return valueOf(! nonNegative(),significand(),exponent()); }

  //--------------------------------------------------------------

  private static final BigFloat0 add (final boolean p0,
                                      final NaturalBEI0 t0,
                                      final int e0,
                                      final boolean p1,
                                      final NaturalBEI0 t1,
                                      final int e1) {
    if (e0<e1) { return add(p1,t1,e1,p0,t0,e0); }
    final int de = e0-e1;
    if (p0^p1) { 
      // different signs
      final NaturalBEI0 t0s = ((de>0) ? t0.shiftUp(de) : t0); 
      final int c01 = t0s.compareTo(t1);
      // t1 > t0s
      if (0>c01) { return valueOf(p1,t1.subtract(t0s),e1); }
      // t0s > t1
      if (0<c01) { return valueOf(p0,t0s.subtract(t1),e1); }
      return ZERO; }
    // same signs
    if (0<de) { return valueOf(p0,t1.add(t0.shiftUp(de)),e1);}
    return valueOf(p0,t0.add(t1),e1); }

  //--------------------------------------------------------------

  @Override
  public final BigFloat0 add (final BigFloat0 q) {
    return add(
      nonNegative(),
      significand(),
      exponent(),
      q.nonNegative(),
      q.significand(),
      q.exponent()); }

  //--------------------------------------------------------------

  private static final BigFloat0
  add6 (final boolean p0,
        final NaturalBEI0 t0,
        final boolean p1,
        final long t1,
        final int upShift,
        final int e) {
    //assert 0L<t1;
    //assert 0<=upShift
    if (p0==p1) { return valueOf(p0,t0.add(t1,upShift),e); }
    final int c = t0.compareTo(t1,upShift);
    if (0<c) { return valueOf(p0,t0.subtract(t1,upShift),e); }
    if (0>c) { return valueOf(p1,t0.subtractFrom(t1,upShift),e); }
    return ZERO; }

  //--------------------------------------------------------------

  private static final BigFloat0
  add5a (final boolean p0,
         final NaturalBEI0 t0,
         final boolean p1,
         final long t1,
         final int e) {
    //assert 0L<=t1;
    //assert 0L<=t1;
    if (p0==p1) { return valueOf(p0,t0.add(t1),e); }
    // different signs
    final int c = t0.compareTo(t1);
    // t0>t1
    if (0<c) { return valueOf(p0,t0.subtract(t1),e); }
    // t1>t0
    if (0>c) { return valueOf(p1,t0.subtractFrom(t1),e); }
    return ZERO; }

  //--------------------------------------------------------------

  private static final BigFloat0
  addSameExponent (final boolean p0,
                   final long t0,
                   final boolean p1,
                   final long t1,
                   final int upShift,
                   final int e) {
    if (p0^p1) { // different signs
      final int c = Longs.compare(t0,t1,upShift);
      if (0==c) { return ZERO; }
      if (0>c) { // t1 > t0
        return valueOf(
          p1,
          NaturalBEI0.difference(t1,upShift,t0),
          e); }
      // t0 > t1
      return valueOf(
        p0,
        NaturalBEI0.difference(t0,t1,upShift),
        e); }
    return valueOf(p0,NaturalBEI0.sum(t0,t1,upShift),e); }

  //--------------------------------------------------------------

  private static final BigFloat0
  add (final boolean p0,
       final NaturalBEI0 t0,
       final int e0,
       final boolean p1,
       final long t11,
       final int e11) {
    //assert 0L<=t11;
    //if (0L==t11) { return valueOf(p0,t0,e0); }
    // minimize long bits
    final int shift = Numbers.loBit(t11);
    final long t1 = (t11>>>shift);
    final int e1 = e11+shift;
    if (e0<e1) { return add6(p0,t0,p1,t1,e1-e0,e0); }
    if (e0==e1) { return add5a(p0,t0,p1,t1,e0); } 
    return add5a(p0,t0.shiftUp(e0-e1),p1,t1,e1); }

  //--------------------------------------------------------------

  private final static BigFloat0
  add (final boolean p0,
       final long t00,
       final int e00,
       final boolean p1,
       final long t11,
       final int e11) {

    //assert 0L<=t00;
    //assert 0L<=t11;

    // minimize long bits
    final int shift0 = Numbers.loBit(t00);
    // 64==shift0 if t00 is zero
    if (64==shift0) { return valueOf(p1,t11,e11); }
    final long t0=(t00>>>shift0);
    final int e0=e00+shift0;

    final int shift1 = Numbers.loBit(t11);
    // 64==shift1 if t11 is zero
    if (64==shift1) { return valueOf(p0,t0,e0); }
    final long t1=(t11>>>shift1);
    final int e1=e11+shift1;

    final int de = e1-e0;
    if (0<=de) { return addSameExponent(p0,t0,p1,t1,de,e0); }
    return addSameExponent(p1,t1,p0,t0,-de,e1); }

  //--------------------------------------------------------------

  public final BigFloat0
  add (final double z) {
    //assert Double.isFinite(z);
    // escape on zero needed for add() 
    if (0.0==z) { return this; }
    return add(
      nonNegative(),
      significand(),
      exponent(),
      Doubles.nonNegative(z),
      Doubles.significand(z),
      Doubles.exponent(z)); }

  public final BigFloat0
  addAll (final double[] z) {
    //assert Double.isFinite(z);
    BigFloat0 s = this;
    for (final double zi : z) { s = s.add(zi); }
    return s; }

  //--------------------------------------------------------------

  public final BigFloat0
  addAbs (final double z) {
    //assert Double.isFinite(z);
    // escape on zero needed for add() 
    if (0.0==z) { return this; }
    return add(
      nonNegative(),
      significand(),
      exponent(),
      true,
      Doubles.significand(z),
      Doubles.exponent(z)); }

  public final BigFloat0
  addAbsAll (final double[] z) {
    //assert Double.isFinite(z);
    BigFloat0 s = this;
    for (final double zi : z) { s = s.addAbs(zi); }
    return s; }

  //--------------------------------------------------------------

  @Override
  public final BigFloat0
  subtract (final BigFloat0 q) {
    return add(
      nonNegative(),
      significand(),
      exponent(),
      ! q.nonNegative(),
      q.significand(),
      q.exponent()); }

  public final BigFloat0
  subtract (final double z) {
    // escape on zero needed for add() 
    if (0.0==z) { return this; }
    return add(
      nonNegative(),
      significand(),
      exponent(),
      ! Doubles.nonNegative(z),
      Doubles.significand(z),
      Doubles.exponent(z)); }

  public final static BigFloat0
  difference (final double z0,
              final double z1) {
    return
      add(
        Doubles.nonNegative(z0),
        Doubles.significand(z0),
        Doubles.exponent(z0),
        ! Doubles.nonNegative(z1),
        Doubles.significand(z1),
        Doubles.exponent(z1)); }

  //--------------------------------------------------------------

  @Override
  public final BigFloat0
  abs () {
    if (nonNegative()) { return this; }
    return negate(); }

  //--------------------------------------------------------------
  // used in Rational.addWithDenom()?

  public static final BigFloat0
  product (final NaturalBEI0 x0,
           final boolean p1,
           final long x1) {
    //assert 0L<=x1;
    final int e0 = x0.loBit();
    final int e1 = loBit(x1);
    final NaturalBEI0 y0 = ((0==e0) ? x0 : x0.shiftDown(e0));
    final long y1 = (((0==e1)||(64==e1)) ? x1 : (x1 >>> e1));
    return valueOf(p1,y0.multiply(y1),e0+e1); }

  private final BigFloat0
  multiply (final boolean p,
            final NaturalBEI0 t,
            final int e) {
    return valueOf(
      (! (nonNegative() ^ p)),
      significand().multiply(t),
      Math.addExact(exponent(),e)); }

  @Override
  public final BigFloat0
  multiply (final BigFloat0 q) {
    return
      multiply(q.nonNegative(),q.significand(),q.exponent()); }

  public final BigFloat0
  multiply (final double z) {
    //assert Double.isFinite(z);
    return
      multiply(
        Doubles.nonNegative(z),
        NaturalBEI0.valueOf(Doubles.significand(z)),
        Doubles.exponent(z)); }

  //--------------------------------------------------------------

  @Override
  public final BigFloat0
  square () {
    //if (isZero() ) { return EMPTY; }
    //if (isOne()) { return ONE; }
    return valueOf(true,significand().square(),2*exponent()); }

  //--------------------------------------------------------------

  //  public final BigFloat0
  //  add2 (final double z) {
  //    //assert Double.isFinite(z);
  //    // twoMul gives 2 adds
  //    final double z2 = z*z;
  //    final double e = Math.fma(z,z,-z2);
  //    return add(z2).add(e); }

  public final BigFloat0
  add2 (final double z) {
    //assert Double.isFinite(z);
    final long tz = Doubles.significand(z);
    final int ez = Doubles.exponent(z);
    final int s = Numbers.loBit(tz);
    final long t;
    final int e;
    if ((0==s) || (64==s)) { t=tz; e=ez; }
    else { t=(tz>>>s); e=ez+s; }
    final NaturalBEI0 t2 = NaturalBEI0.fromSquare(t);
    final int e2 = (e<<1);
    return add(
      nonNegative(),
      significand(),
      exponent(),
      true,
      t2,
      e2); }

  public final BigFloat0
  add2All (final double[] z) {
    BigFloat0 s = this;
    for (final double zi : z) { s = s.add2(zi); }
    return s; }

  //--------------------------------------------------------------

  public BigFloat0 addL1 (final double z0,
                          final double z1) {
    if (z0>z1) { return add(z0).add(-z1); }
    if (z0<z1) { return add(-z0).add(z1); }
    return this; }

  public final BigFloat0
  addL1Distance (final double[] z0,
                 final double[] z1) {
    final int n = z0.length;
    //assert n==z1.length;
    BigFloat0 s = this;
    for (int i=0;i<n;i++) { s = s.addL1(z0[i],z1[i]); }
    return s; }

  //--------------------------------------------------------------
  // internal special case: add 2*z0*z1

  private final BigFloat0
  addProductTwice (final double z0,
                   final double z1) {
    //assert Double.isFinite(z0);
    //assert Double.isFinite(z1);
    if ((0.0==z0) || (0.0==z1)) { return this; }

    final long t01 = Doubles.significand(z0);
    final int e01 = Doubles.exponent(z0);
    final int shift0 = Numbers.loBit(t01);
    final long t0 = (t01>>>shift0);
    final int e0 = e01+shift0;

    final long t11 = Doubles.significand(z1);
    final int e11 = Doubles.exponent(z1);
    final int shift1 = Numbers.loBit(t11);
    final long t1 = (t11>>>shift1);
    final int e1 = e11+shift1;

    return
      add(
        nonNegative(),
        significand(),
        exponent(),
        Doubles.nonNegative(z0)==Doubles.nonNegative(z1),
        NaturalBEI0.product(t0,t1),
        e0+e1+1); }

  //--------------------------------------------------------------

  public final BigFloat0
  addL2 (final double z0,
         final double z1) {
    final double mz1 = -z1;
    return
      add2(z0).add2(z1).addProductTwice(z0,mz1); }

  public final BigFloat0
  addL2Distance (final double[] z0,
                 final double[] z1) {
    final int n = z0.length;
    //assert n==z1.length;
    BigFloat0 s = this;
    for (int i=0;i<n;i++) { s = s.addL2(z0[i],z1[i]); }
    return s; }

  //--------------------------------------------------------------
  //    public final BigFloat0
  //    addProduct (final double z0,
  //                final double z1) {
  //      // twoMul -> 2 adds.
  //      final double z01 = z0*z1;
  //      //assert Double.isFinite(z01);
  //      final double e = Math.fma(z0,z1,-z01);
  //      return add(z01).add(e); }

  public final BigFloat0
  addProduct (final double z0,
              final double z1) {
    //assert Double.isFinite(z0);
    //assert Double.isFinite(z1);
    if ((0.0==z0) || (0.0==z1)) { return this; }

    final long t01 = Doubles.significand(z0);
    final int e01 = Doubles.exponent(z0);
    final int shift0 = Numbers.loBit(t01);
    final long t0 = (t01>>>shift0);
    final int e0 = e01+shift0;

    final long t11 = Doubles.significand(z1);
    final int e11 = Doubles.exponent(z1);
    final int shift1 = Numbers.loBit(t11);
    final long t1 = (t11>>>shift1);
    final int e1 = e11+shift1;

    return
      add(
        nonNegative(),
        significand(),
        exponent(),
        Doubles.nonNegative(z0)==Doubles.nonNegative(z1),
        NaturalBEI0.product(t0,t1),
        e0+e1); }

  public final BigFloat0 
  addProducts (final double[] z0,
               final double[] z1)  {
    final int n = z0.length;
    //assert n==z1.length;
    BigFloat0 s = this;
    for (int i=0;i<n;i++) { s = s.addProduct(z0[i],z1[i]); }
    return s; }

  //--------------------------------------------------------------
  /** Exact <code>a*x+y</code> (aka fma). */

  public static final BigFloat0
  axpy (final double a,
        final double x,
        final double y) {
    return valueOf(y).addProduct(a,x); }

  /** Exact <code>a*x+y</code> (aka fma). */

  public static final BigFloat0[]
    axpy (final double[] a,
          final double[] x,
          final double[] y) {
    final int n = a.length;
    //assert n==x.length;
    //assert n==y.length;
    final BigFloat0[] bf = new BigFloat0[n];
    for (int i=0;i<n;i++) { bf[i] = axpy(a[i],x[i],y[i]); }
    return bf; }

  /** Exact <code>this*x+y</code> (aka fma). */

  public static final BigFloat0
  axpy (final double aa,
        final BigFloat0 x,
        final double y) {
    return x.multiply(aa).add(y); }

  /** Exact <code>a*this+y</code> (aka fma). */

  public static final BigFloat0[] axpy (final double[] a,
                                       final BigFloat0[] x,
                                       final double[] y) {
    final int n = x.length;
    //assert n==x.length;
    //assert n==y.length;
    final BigFloat0[] bf = new BigFloat0[n];
    for (int i=0;i<n;i++) { bf[i] = axpy(a[i],x[i],y[i]); }
    return bf; }

  //--------------------------------------------------------------
  // Number methods
  //--------------------------------------------------------------
  /** Unsupported.
   *
   * TODO: should it really truncate or round instead? Or
   * should there be more explicit round, floor, ceil, etc.?
   */
  @Override
  public final int intValue () {
    throw Exceptions.unsupportedOperation(this,"longValue"); }

  /** Unsupported.
   *
   * TODO: should it really truncate or round instead? Or
   * should there be more explicit round, floor, ceil, etc.?
   */
  @Override
  public final long longValue () {
    throw Exceptions.unsupportedOperation(this,"longValue"); }

  //--------------------------------------------------------------

  private static final boolean roundUp (final NaturalBEI0 t,
                                        final int e) {
    if (! t.testBit(e-1)) { return false; }
    for (int i=e-2;i>=0;i--) {
      if (t.testBit(i)) { return true; } }
    return t.testBit(e); }

  //--------------------------------------------------------------
  /** @return closest half-even rounded <code>float</code>
   */

  @Override
  public final float floatValue () {
    final boolean nn = nonNegative();
    final NaturalBEI0 s0 = significand();
    final int e0 = exponent();
    if (s0.isZero()) { return (nn ? 0.0F : -0.0F); }

    // DANGER: what if hiBit isn't in the int range?
    final int eh = s0.hiBit();
    final int es =
      Math.max(Floats.MINIMUM_EXPONENT_INTEGRAL_SIGNIFICAND-e0,
        Math.min(
          Floats.MAXIMUM_EXPONENT_INTEGRAL_SIGNIFICAND-e0-1,
          eh-Floats.SIGNIFICAND_BITS));
    if (0==es) {
      return floatMergeBits(nn,s0.intValue(),e0); }
    if (0 > es) {
      final int e1 = e0 + es;
      final int s1 = (s0.intValue() << -es);
      return floatMergeBits(nn,s1,e1); }
    if (eh <= es) { return (nn ? 0.0F : -0.0F); }
    // eh > es > 0
    final boolean up = roundUp(s0,es);
    // TODO: faster way to select the right bits as a int?
    //final int s1 = s0.shiftDown(es).intValue();
    final int s1 = s0.getShiftedInt(es);
    final int e1 = e0 + es;
    if (up) {
      final int s2 = s1 + 1;
      if (Numbers.hiBit(s2) > Floats.SIGNIFICAND_BITS) { // carry
        // lost bit has to be zero, since there was just a carry
        final int s3 = (s2 >> 1);
        final int e3 = e1 + 1;
        return floatMergeBits(nn,s3,e3); }
      // no carry
      return floatMergeBits(nn,s2,e1); }
    // round down
    return floatMergeBits(nn,s1,e1); }

  //--------------------------------------------------------------
  /** @return closest half-even rounded <code>double</code>
   */

  @Override
  public final double doubleValue () {
    final boolean nn = nonNegative();
    final NaturalBEI0 s0 = significand();
    final int e0 = exponent();
    if (s0.isZero()) { return (nn ? 0.0 : -0.0); }
    final int eh = s0.hiBit();
    final int es =
      Math.max(Doubles.MINIMUM_EXPONENT_INTEGRAL_SIGNIFICAND-e0,
        Math.min(
          Doubles.MAXIMUM_EXPONENT_INTEGRAL_SIGNIFICAND-e0-1,
          eh-Doubles.SIGNIFICAND_BITS));
    if (eh-es>Doubles.SIGNIFICAND_BITS) {
      return 
        (nn ? 
          Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY); }
    if (0==es) {
      return doubleMergeBits(nn,s0.longValue(),e0); }
    if (0 > es) {
      final int e1 = e0 + es;
      final long s1 = (s0.longValue() << -es);
      return doubleMergeBits(nn,s1,e1); }
    if (eh <= es) { return (nn ? 0.0 : -0.0); }
    // eh > es > 0
    final boolean up = roundUp(s0,es);
    final long s1 = s0.getShiftedLong(es);
    final int e1 = e0 + es;
    if (up) {
      final long s2 = s1 + 1L;
      if (Numbers.hiBit(s2) > Doubles.SIGNIFICAND_BITS) { // carry
        // lost bit has to be zero, since there was just a carry
        final long s3 = (s2 >> 1);
        final int e3 = e1 + 1;
        return doubleMergeBits(nn,s3,e3); }
      // no carry
      return doubleMergeBits(nn,s2,e1); }
    // round down
    return doubleMergeBits(nn,s1,e1); }

  //--------------------------------------------------------------
  // Comparable methods
  //--------------------------------------------------------------

  @Override
  public final int compareTo (final BigFloat0 q) {

    if (nonNegative() && (! q.nonNegative())) { return 1; }
    if ((! nonNegative()) && q.nonNegative()) { return -1; }
    // same signs
    final NaturalBEI0 t0 = significand();
    final NaturalBEI0 t1 = q.significand();
    final int e0 = exponent();
    final int e1 = q.exponent();
    final int c;
    if (e0 <= e1) { c = t0.compareTo(t1.shiftUp(e1-e0)); }
    else { c = t0.shiftUp(e0-e1).compareTo(t1); }
    return (nonNegative() ? c : -c); }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------


  private static final boolean reducedEquals (final BigFloat0 a,
                                              final BigFloat0 b) {
    // assuming a and b have minimum significand and maximum 
    // exponent
    if (a==b) { return true; }
    if (null==a) { return false; }
    // assuming reduced
    if (! a.significand().equals(b.significand())) { return false; }
    if (a.significand().isZero()) { return true; }
    if (a.nonNegative()!=b.nonNegative()) { return false; }
    if (a.exponent()!=b.exponent()) { return false; }
    return true; }

  public final boolean equals (final BigFloat0 q) {
    return reducedEquals(reduce(),q.reduce()); }

  @Override
  public boolean equals (final Object o) {
    if (!(o instanceof BigFloat)) { return false; }
    return equals(o); }

  @Override
  public int hashCode () {
    final BigFloat0 a = reduce();
    int h = 17;
    h = (31*h) + (a.nonNegative() ? 0 : 1);
    h = (31*h) + a.exponent();
    h = (31*h) + Objects.hash(a.significand());
    return h; }

  @Override
  public final String toString () {
    return
      (nonNegative() ? "" : "-")
      + "0x" + significand().toString()
      + "p" + exponent(); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------

  private BigFloat0 (final boolean p0,
                     final NaturalBEI0 t0,
                     final int e0) {
    _nonNegative = p0;
    _significand = t0;
    _exponent = e0; } 

  //--------------------------------------------------------------

  //  private static final BigFloat0 reduce (final boolean p0,
  //                                        final NaturalBEI0 t0,
  //                                        final int e0) {
  //    //if (t0.isZero()) { return ZERO; }
  //    final int shift = t0.loBit();
  //    if (0>=shift) { return new BigFloat0(p0,t0,e0); }
  //    return new BigFloat0(p0, t0.shiftDown(shift),e0+shift); }

  private final BigFloat0 reduce () {
    final boolean p0 = nonNegative();
    final NaturalBEI0 t0 = significand();
    final int e0 = exponent();
    final int shift = t0.loBit();
    if (0>=shift) { return this; }
    return new BigFloat0(p0, t0.shiftDown(shift),e0+shift); }

  public static final BigFloat0 valueOf (final boolean p0,
                                         final NaturalBEI0 t0,
                                         final int e0) {
    if (t0.isZero()) { return ZERO; }
    //return reduce(p0,t0,e0); }
    return new BigFloat0(p0,t0,e0); }

  //--------------------------------------------------------------

  private static final BigFloat0 valueOf (final boolean nonNegative,
                                          final long t0,
                                          final int e0)  {
    //if (0L==t0) { return ZERO; }
    //assert 0L<t0;
    final int shift = Numbers.loBit(t0);
    final long t1;
    final int e1;
    if ((0==shift)||(64==shift)) { t1=t0; e1=e0; }
    else { t1 = (t0 >>> shift); e1 = e0 + shift; }
    return valueOf(nonNegative,NaturalBEI0.valueOf(t1),e1); }

  public static final BigFloat0 valueOf (final double z)  {
    return valueOf(
      Doubles.nonNegative(z),
      Doubles.significand(z),
      Doubles.exponent(z)); }

  //--------------------------------------------------------------

  private static final BigFloat0 valueOf (final boolean nonNegative,
                                          final int t0,
                                          final int e0)  {
    //if (0==t0) { return ZERO; }
    return valueOf(nonNegative,NaturalBEI0.valueOf(t0),e0); }

  public static final BigFloat0 valueOf (final float x)  {
    return valueOf(
      Floats.nonNegative(x),
      Floats.significand(x),
      Floats.exponent(x)); }

  //--------------------------------------------------------------

  //  public static final BigFloat0 valueOf (final byte t)  {
  //    if (0<=t) { return valueOf(true,NaturalBEI0.valueOf(t),0); }
  //    return valueOf(false,NaturalBEI0.valueOf(-t),0); }
  //
  //  public static final BigFloat0 valueOf (final short t)  {
  //    if (0<=t) { return valueOf(true,NaturalBEI0.valueOf(t),0); }
  //    return valueOf(false,NaturalBEI0.valueOf(-t),0); }
  //
  //  public static final BigFloat0 valueOf (final int t)  {
  //    if (0<=t) { return valueOf(true,NaturalBEI0.valueOf(t),0); }
  //    return valueOf(false,NaturalBEI0.valueOf(-t),0); }
  //
  //  public static final BigFloat0 valueOf (final long t)  {
  //    if (0<=t) { return valueOf(true,NaturalBEI0.valueOf(t),0); }
  //    return valueOf(false,NaturalBEI0.valueOf(-t),0); }

  //--------------------------------------------------------------

  //  public static final BigFloat0 valueOf (final Double x)  {
  //    return valueOf(x.doubleValue()); }
  //
  //  public static final BigFloat0 valueOf (final Float x)  {
  //    return valueOf(x.floatValue()); }
  //
  //  public static final BigFloat0 valueOf (final Byte x)  {
  //    return valueOf(x.byteValue()); }
  //
  //  public static final BigFloat0 valueOf (final Short x)  {
  //    return valueOf(x.shortValue()); }
  //
  //  public static final BigFloat0 valueOf (final Integer x)  {
  //    return valueOf(x.intValue()); }
  //
  //  public static final BigFloat0 valueOf (final Long x)  {
  //    return valueOf(x.longValue()); }
  //
  //  public static final BigFloat0 valueOf (final BigDecimal x)  {
  //    throw Exceptions.unsupportedOperation(null,"valueOf",x); }
  //
  //  public static final BigFloat0 valueOf (final NaturalBEI0 x)  {
  //    return valueOf(true,x,0); }
  //
  //  public static final BigFloat0 valueOf (final Number x)  {
  //    if (x instanceof Double) { return valueOf((Double) x); }
  //    if (x instanceof Float) { return valueOf((Float) x); }
  //    if (x instanceof Byte) { return valueOf((Byte) x); }
  //    if (x instanceof Short) { return valueOf((Short) x); }
  //    if (x instanceof Integer) { return valueOf((Integer) x); }
  //    if (x instanceof Long) { return valueOf((Long) x); }
  //    if (x instanceof BigDecimal) { return valueOf((BigDecimal) x); }
  //    throw Exceptions.unsupportedOperation(null,"valueOf",x); }
  //
  //  public static final BigFloat0 valueOf (final Object x)  {
  //    if (x instanceof BigFloat0) { return (BigFloat0) x; }
  //    if (x instanceof NaturalBEI0) { return valueOf((NaturalBEI0) x); }
  //    return valueOf((Number) x); }

  //--------------------------------------------------------------

  public static final BigFloat0 ZERO =
    new BigFloat0(true,NaturalBEI0.valueOf(0L),0);

  //  public static final BigFloat0 ONE =
  //    new BigFloat0(true,NaturalBEI0.ONE,0);
  //
  //  public static final BigFloat0 TWO =
  //    new BigFloat0(true,NaturalBEI0.ONE,1);
  //
  //  public static final BigFloat0 TEN =
  //    new BigFloat0(true,NaturalBEI0.valueOf(5),1);
  //
  //  public static final BigFloat0 MINUS_ONE =
  //    new BigFloat0(false,NaturalBEI0.ONE,0);

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
