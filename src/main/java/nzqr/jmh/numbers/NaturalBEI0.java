package nzqr.jmh.numbers;

import static nzqr.java.numbers.Numbers.hiWord;
import static nzqr.java.numbers.Numbers.loWord;
import static nzqr.java.numbers.Numbers.unsigned;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import nzqr.java.Debug;
import nzqr.java.Exceptions;
import nzqr.java.numbers.Ringlike;

/** immutable arbitrary-precision non-negative integers
 * (natural number) represented by big-endian
 * unsigned <code>int[]</code>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-09-04
 */

@SuppressWarnings("unchecked")
public final class NaturalBEI0 implements Ringlike<NaturalBEI0> {

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------
  /** This array is never modified.
   */

  private final int[] _words;
  private final int[] words () { return _words; }

  //--------------------------------------------------------------

  public final int endWord () { return words().length; }

  public final int word (final int i) {
    final int n = words().length;
    final int ii = n-i-1;
    if ((0<=ii) && (ii<n)) { return words()[ii]; }
    return 0; }

  public final long uword (final int i) {
    final int n = words().length;
    final int ii = n-i-1;
    if ((0<=ii) && (ii<n)) { return unsigned(_words[ii]); }
    return 0L; }

  /** Don't drop leading zeros. */
  public final int[] copyWords () {
    return Arrays.copyOf(words(),words().length); }

  public final NaturalBEI0 setWord (final int i,
                                final int w) {
    throw Exceptions.unsupportedOperation(this,"setWord",i,w); }

  public final NaturalBEI0 words (final int i0,
                              final int i1) {
    throw Exceptions.unsupportedOperation(this,"words",i0,i1); }
  
  //--------------------------------------------------------------

  public final int loInt () {
    // Search for lowest order nonzero int
    final int n = hiInt(); // might be 0
    for (int i=0;i<n;i++) {
      if (0!=word(i)) { return i; } }
    //assert 0==n;
    return 0; }

  public final int hiInt () {
    for (int i = endWord()-1;i>=0;i--) {
      if (0!=word(i) ) { return i+1; } }
    return 0; }

  public static final NaturalBEI0 ZERO = new NaturalBEI0(Bei0.ZERO);

  @Override
  public final boolean isZero () { return 0==words().length; }

  @Override
  public final NaturalBEI0 zero () { return ZERO; }

  //--------------------------------------------------------------
  // Natural
  //--------------------------------------------------------------
  // long based factories
  //--------------------------------------------------------------

  static final NaturalBEI0 sum (final long t0,
                                final long t1,
                                final int upShift) {
    //assert 0L<=t0;
    //assert 0L<=t1;
    //assert 0<=bitShift;
    final int[] u = Bei0.sum(t0,t1,upShift);
    return unsafe(u); }

  // only when (m1 << upShift) <= m0
  static final NaturalBEI0 difference (final long m0,
                                       final long m1,
                                       final int upShift) {
    //assert 0L<=m0;
    //assert 0L<=m1;
    //assert 0<=upShift;
    final int[] u = Bei0.difference(m0,m1,upShift);
    return unsafe(u); }

  // only when (m1 << upShift) <= m0
  static final NaturalBEI0 difference (final long m0,
                                       final int upShift,
                                       final long m1) {
    //assert 0L<=m0;
    final int[] u = Bei0.difference(Bei0.valueOf(m0,upShift),m1);
    return unsafe(u); }

  //--------------------------------------------------------------

  static final NaturalBEI0 product (final long t0,
                                    final long t1) {
    //assert 0L<=t0;
    //assert 0L<=t1;
    //if ((0L==t0||(0L==t1))) { return zero(); }

    final long hi0 = hiWord(t0);
    final long lo0 = loWord(t0);
    final long hi1 = hiWord(t1);
    final long lo1 = loWord(t1);

    final long lolo = lo0*lo1;
    final long hilo2 = (hi0*lo1) + (hi1*lo0);
    // TODO: fix lurking overflow issue
    // works here because t0,t1 53 bit double significands
    //final long hilo2 = Math.addExact(hi0*lo1,hi1*lo0);
    final long hihi = hi0*hi1;
    long sum = lolo;
    final int w0 = (int) sum;
    sum = (sum >>> 32) + hilo2;
    final int w1 = (int) sum;
    sum = (sum >>> 32) + hihi ;
    final int w2 = (int) sum;
    final int w3 = (int) (sum >>> 32);
    if (0!=w3) { return unsafe(new int[] { w3,w2,w1,w0, }); }
    if (0!=w2) { return unsafe(new int[] { w2,w1,w0, }); }
    if (0!=w1) { return unsafe(new int[] { w1,w0, }); }
    if (0!=w0) { return unsafe(new int[] { w0, }); }
    return ZERO; }

  static final NaturalBEI0 fromSquare (final long t) {
    //assert 0L<=t;
    final long hi = hiWord(t);
    final long lo = loWord(t);
    final long lolo = lo*lo;
    final long hilo2 = (hi*lo) << 1;
    //final long hilo2 = Math.multiplyExact(2,hi*lo);
    final long hihi = hi*hi;
    long sum = lolo;
    final int w0 = (int) sum;
    sum = (sum >>> 32) + hilo2;
    final int w1 = (int) sum;
    sum = (sum >>> 32) + hihi ;
    final int w2 = (int) sum;
    final int w3 = (int) (sum >>> 32);
    if (0!=w3) { return unsafe(new int[] { w3,w2,w1,w0, }); }
    if (0!=w2) { return unsafe(new int[] { w2,w1,w0, }); }
    if (0!=w1) { return unsafe(new int[] { w1,w0, }); }
    if (0!=w0) { return unsafe(new int[] { w0, }); }
    return ZERO; }
  //return unsafe(Bei0.stripLeadingZeros(m)); }

  //--------------------------------------------------------------
  // add longs
  //--------------------------------------------------------------

  public final NaturalBEI0 add (final long m) {
    //assert 0L<=m;
    return unsafe(Bei0.add(words(),m)); }

  //--------------------------------------------------------------

  public final NaturalBEI0 add (final long u,
                                final int upShift) {
    //assert 0L<=m;
    final int[] vv = Bei0.add(words(),u,upShift);
    return unsafe(vv); }

  //--------------------------------------------------------------
  // subtract longs
  //--------------------------------------------------------------
  // only when val <= this

  public final NaturalBEI0 subtract (final long m) {
    //assert 0L<=m;
    final int[] u = Bei0.difference(words(),m);
    return unsafe(u); }

  // only when (m << upShift) <= this
  public final NaturalBEI0 subtract (final long m,
                                     final int upShift) {
    //assert 0L<=m;
    final int[] u = Bei0.subtract(words(),m,upShift);
    return unsafe(u); }

  //--------------------------------------------------------------
  // only when this <= m

  public final NaturalBEI0 subtractFrom (final long m) {
    //assert 0L<=m;
    final int[] u = Bei0.subtract(m,words());
    return unsafe(u); }

  // only when this <= (m << upShift)

  public final NaturalBEI0 subtractFrom (final long m,
                                         final int upShift) {
    //assert 0L<=m;
    final int[] ms = Bei0.shiftUp(m,upShift);
    final int[] u = Bei0.subtract(ms,words());
    return unsafe(u); }

  //--------------------------------------------------------------
  // arithmetic with (shifted) Naturals
  //--------------------------------------------------------------

  //--------------------------------------------------------------
  // Ringlike
  //--------------------------------------------------------------

  @Override
  public final NaturalBEI0 add (final NaturalBEI0 m) {
    return unsafe(Bei0.add(words(),m.words())); }

  public final NaturalBEI0 add (final NaturalBEI0 m,
                            final int upShift) {
    return add(m.shiftUp(upShift)); }

  // only when val <= this

  @Override
  public final NaturalBEI0 subtract (final NaturalBEI0 m) {
    return unsafe(
      Bei0.subtract(words(),m.words())); }

  //--------------------------------------------------------------

  @Override
  public final NaturalBEI0 absDiff (final NaturalBEI0 u) {
    final int c01 = compareTo(u);
    if (0<c01) { return subtract(u); }
    if (0>c01) { return u.subtract(this); }
    return ZERO; }

  //--------------------------------------------------------------

  @Override
  public final boolean isOne () { return equals(ONE); }

  //--------------------------------------------------------------
  // square
  //--------------------------------------------------------------

  @Override
  public final NaturalBEI0 square () {
    if (isZero()) { return valueOf(0L); }
    if (isOne()) { return ONE; }
    return unsafe(Bei0.square(words(),false)); }

  //--------------------------------------------------------------
  // multiply
  //--------------------------------------------------------------

  public final NaturalBEI0 multiply (final long that) {
    //assert 1L<=that;
    return unsafe(Bei0.multiply(words(),that)); }

//  @Override
//  public final NaturalBEI0 multiply (final long that,
//                                 final int shift) {
//    //assert 1L<=that;
//    return multiply(valueOf(that,shift)); }

  @Override
  public final NaturalBEI0 multiply (final NaturalBEI0 that) {
    return unsafe(
      Bei0.multiply(words(),that.words())); }

  //--------------------------------------------------------------
  // Division
  //--------------------------------------------------------------

  private static final boolean
  useKnuthDivision (final NaturalBEI0 num,
                    final NaturalBEI0 den) {
    return Bei0.useKnuthDivision(num.words(),den.words()); }

  //--------------------------------------------------------------
  // Knuth algorithm
  //--------------------------------------------------------------

  private final NaturalBEI0
  divideKnuth (final NaturalBEI0 that) {
    final MutableNaturalBEI0 q = MutableNaturalBEI0.make();
    final MutableNaturalBEI0 num = MutableNaturalBEI0.valueOf(this.words());
    final MutableNaturalBEI0 den = MutableNaturalBEI0.valueOf(that.words());
    num.divideKnuth(den,q,false);
    return valueOf(q.getValue()); }

  public final List<NaturalBEI0>
  divideAndRemainderKnuth (final NaturalBEI0 u) {
    final NaturalBEI0 that = u;
    final MutableNaturalBEI0 q = MutableNaturalBEI0.make();
    final MutableNaturalBEI0 num = MutableNaturalBEI0.valueOf(this.words());
    final MutableNaturalBEI0 den = MutableNaturalBEI0.valueOf(that.words());
    final MutableNaturalBEI0 r = num.divideKnuth(den,q,true);
    return List.of(
      valueOf(q.getValue()), 
      valueOf(r.getValue())); }

  private final NaturalBEI0 remainderKnuth (final NaturalBEI0 that) {
    final MutableNaturalBEI0 q = MutableNaturalBEI0.make();
    final MutableNaturalBEI0 num = MutableNaturalBEI0.valueOf(this.words());
    final MutableNaturalBEI0 den = MutableNaturalBEI0.valueOf(that.words());
    final MutableNaturalBEI0 r = num.divideKnuth(den,q,true);
    return valueOf(r.getValue()); }

  //--------------------------------------------------------------

  public final List<NaturalBEI0>
  divideAndRemainderBurnikelZiegler (final NaturalBEI0 u) {
    final NaturalBEI0 that = u;
    final MutableNaturalBEI0 q = MutableNaturalBEI0.make();
    final MutableNaturalBEI0 num = MutableNaturalBEI0.valueOf(this.words());
    final MutableNaturalBEI0 den = MutableNaturalBEI0.valueOf(that.words());
    final MutableNaturalBEI0 r =
      num.divideAndRemainderBurnikelZiegler(den,q);
    final NaturalBEI0 qq =
      q.isZero() ? valueOf(0L) : valueOf(q.getValue());
      final NaturalBEI0 rr =
        r.isZero() ? valueOf(0L) : valueOf(r.getValue());
        return List.of(qq,rr); }

  private final NaturalBEI0
  divideBurnikelZiegler (final NaturalBEI0 that) {
    return 
      divideAndRemainderBurnikelZiegler(that).get(0); }

  private final NaturalBEI0
  remainderBurnikelZiegler (final NaturalBEI0 that) {
    return  
      divideAndRemainderBurnikelZiegler(that).get(1); }

  //--------------------------------------------------------------
  // division Ringlike api
  //--------------------------------------------------------------

  @Override
  public final NaturalBEI0
  divide (final NaturalBEI0 that) {
    //assert (! that.isZero());
    final NaturalBEI0 u = that;
    if (that.isOne()) { return this; }
    if (useKnuthDivision(this,u)) { return divideKnuth(u); }
    return divideBurnikelZiegler(u); }

  @Override
  public List<NaturalBEI0>
  divideAndRemainder (final NaturalBEI0 that) {
    //assert (! that.isZero());
    final NaturalBEI0 u = that;
    if (useKnuthDivision(this,u)) {
      return divideAndRemainderKnuth(u); }
    return divideAndRemainderBurnikelZiegler(u); }

  @Override
  public final NaturalBEI0 remainder (final NaturalBEI0 that) {
    //assert (! that.isZero());
    final NaturalBEI0 u = that;
    if (useKnuthDivision(this,u)) {
      return remainderKnuth(u); }
    return remainderBurnikelZiegler(u); }

  //--------------------------------------------------------------
  // gcd
  //--------------------------------------------------------------

  @Override
  public final NaturalBEI0 gcd (final NaturalBEI0 that) {
    final MutableNaturalBEI0 a = MutableNaturalBEI0.valueOf(words());
    final NaturalBEI0 u = that;
    final MutableNaturalBEI0 b = MutableNaturalBEI0.valueOf(u.words());
    final MutableNaturalBEI0 result = a.hybridGCD(b);
    return valueOf(result.getValue()); }

  // remove common factors as if numerator and denominator
  //  public static final NaturalBEI[] reduce (final NaturalBEI n0,
  //                                          final NaturalBEI d0) {
  //    final MutableNaturalBEI0[] nd =
  //      MutableNaturalBEI0.reduce(
  //        MutableNaturalBEI0.valueOf(n0._mag),
  //        MutableNaturalBEI0.valueOf(d0._mag));
  //    return new NaturalBEI[]
  //      { valueOf(nd[0].getValue()),
  //        valueOf(nd[1].getValue()), }; }

  public static final NaturalBEI0[] reduce (final NaturalBEI0 n0,
                                        final NaturalBEI0 d0) {
    final int shift =
      Math.min(
        Bei0.loBit(n0.words()),
        Bei0.loBit(d0.words()));
    final NaturalBEI0 n = (shift != 0) ? n0.shiftDown(shift) : n0;
    final NaturalBEI0 d = (shift != 0) ? d0.shiftDown(shift) : d0;
    if (n.equals(d)) { return new NaturalBEI0[] { ONE, ONE, }; }
    if (d.isOne()) { return new NaturalBEI0[] { n, ONE, }; }
    if (n.isOne()) { return new NaturalBEI0[] { ONE, d, }; }
    final NaturalBEI0 gcd = n.gcd(d);
    if (gcd.compareTo(ONE) > 0) {
      return new NaturalBEI0[] { n.divide(gcd), d.divide(gcd), }; }
    return new NaturalBEI0[] { n, d, }; }

  //--------------------------------------------------------------
  // Uints
  //--------------------------------------------------------------

  public final int loBit () { return Bei0.loBit(words()); }

  public final int hiBit () { return Bei0.hiBit(words()); }

  public final NaturalBEI0 shiftDown (final int n) {
    //assert 0<=n;
    return unsafe(Bei0.shiftDown(words(),n)); }

  public final NaturalBEI0 shiftUp (final int n) {
    //assert 0<=n;
    return unsafe(Bei0.shiftUp(words(),n)); }

  // get the least significant int words of (m >>> shift)

  //--------------------------------------------------------------

  public final int getShiftedInt (final int n) {
    //assert 0<=n;
    return Bei0.getShiftedInt(words(),n); }

  // get the least significant two int words of (m >>> shift) as a
  // long

  public final long getShiftedLong (final int n) {
    //assert 0<=n;
    return Bei0.getShiftedLong(words(),n); }

  //--------------------------------------------------------------

  public final boolean testBit (final int n) {
    return Bei0.testBit(words(),n); }

  public final NaturalBEI0 setBit (final int n) {
    return unsafe(Bei0.setBit(words(),n)); }

  //--------------------------------------------------------------
  // Comparable interface+
  //--------------------------------------------------------------

  @Override
  public final int compareTo (final NaturalBEI0 that) {
    final NaturalBEI0 u = that;
    return Bei0.compare(words(),u.words()); }

  public final int compareTo (final long u) {
    //assert 0L<=y;
    return Bei0.compare(words(),u); }

  public final int compareTo (final long that,
                              final int upShift) {
    //assert 0L<=that;
    return Bei0.compare(words(),that,upShift); }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public int hashCode () {
    int hashCode = 0;
    for (final int element : words()) {
      hashCode = (int) ((31 * hashCode) + unsigned(element)); }
    return hashCode; }

  @Override
  public boolean equals (final Object x) {
    if (x==this) { return true; }
    if (!(x instanceof NaturalBEI0)) { return false; }
    final NaturalBEI0 xInt = (NaturalBEI0) x;
    final int[] m = words();
    final int len = m.length;
    final int[] xm = xInt.words();
    if (len != xm.length) { return false; }
    for (int i = 0; i < len; i++) {
      if (xm[i] != m[i]) { return false; } }
    return true; }

  /** hex string. */
  @Override
  public String toString () { return Debug.toHexString(words()); }

  //  /** hex string. */
  //  @Override
  //  public String toString (final int radix) {
  //    //assert radix==0x10;
  //    return Debug.toHexString(words()); }

  //--------------------------------------------------------------
  // Number interface+
  //--------------------------------------------------------------

  public final byte[] bigEndianBytes () {
    return Bei0.toByteArray(words()); }

  public final BigInteger bigIntegerValue () {
    return Bei0.bigIntegerValue(words()); }

  @Override
  public final int intValue () { return Bei0.intValue(words()); }

  @Override
  public final long longValue () { return Bei0.longValue(words()); }

  //--------------------------------------------------------------

  @Override
  public final float floatValue () {
    return Bei0.floatValue(words()); }

  @Override
  public final double doubleValue () {
    return Bei0.doubleValue(words()); }

  //--------------------------------------------------------------
  // construction
  //-------------------------------------------------------------

  private NaturalBEI0 (final int[] mag) { _words = mag; }

  // assume no leading zeros
  public static final NaturalBEI0 unsafe (final int[] m) {
    return new NaturalBEI0(m); }

  // TODO: m may leak into NaturalBEI without copy!!
  public static final NaturalBEI0 valueOf (final int[] m) {
    final int[] m1 = Bei0.stripLeadingZeros(m);
    return new NaturalBEI0(m1); }

  public static final NaturalBEI0 valueOf (final byte[] b,
                                           final int off,
                                           final int len) {
    return unsafe(Bei0.stripLeadingZeros(b,off,len)); }

  /** Return a {@link Natural} equivalent to the unsigned 
   * value of <code>u</code>.
   */
  public static final NaturalBEI0 valueOf (final int u) {
    if (u==0) { return ZERO; }
    return unsafe(new int[] {u}); }

  public static final NaturalBEI0 valueOf (final byte[] b) {
    return valueOf(b,0,b.length); }

  public static final NaturalBEI0 valueOf (final BigInteger bi) {
    return valueOf(bi.toByteArray()); }

  //-------------------------------------------------------------

  public static final NaturalBEI0 valueOf (final String s,
                                           final int radix) {
    return unsafe(Bei0.toInts(s,radix)); }

  public static final NaturalBEI0 valueOf (final String s) {
    return valueOf(s,0x10); }

  //--------------------------------------------------------------
  // cached values
  //--------------------------------------------------------------

  private static final int MAX_CONSTANT = 16;
  private static final NaturalBEI0 posConst[] =
    new NaturalBEI0[MAX_CONSTANT+1];

  private static volatile NaturalBEI0[][] powerCache;

  /** The cache of logarithms of radices for base conversion. */
  private static final double[] logCache;

  static {
    for (int i = 1; i <= MAX_CONSTANT; i++) {
      final int[] magnitude = new int[1];
      magnitude[0] = i;
      posConst[i] = unsafe(magnitude); }
    // Initialize the cache of radix^(2^x) values used for base
    // conversion with just the very first value. Additional
    // values will be created on demand.
    powerCache = new NaturalBEI0[Character.MAX_RADIX + 1][];
    logCache = new double[Character.MAX_RADIX + 1];
    for (
      int i = Character.MIN_RADIX;
      i <= Character.MAX_RADIX;
      i++) {
      powerCache[i] = new NaturalBEI0[] { NaturalBEI0.valueOf(i) };
      logCache[i] = Math.log(i); } }

  public static final NaturalBEI0 ONE = valueOf(1);
  public static final NaturalBEI0 TWO = valueOf(2);
  public static final NaturalBEI0 TEN = valueOf(10);

  //--------------------------------------------------------------

  public static final NaturalBEI0 valueOf (final long x) {
    if (x==0) { return ZERO; }
    //assert 0L < x;
    if (x <= MAX_CONSTANT) { return posConst[(int) x]; }
    return unsafe(Bei0.valueOf(x)); }

  //--------------------------------------------------------------

  public static final NaturalBEI0 valueOf (final long x,
                                           final int upShift) {
    if (0L==x) { return valueOf(0L); }
    //assert 0L < x;
    return unsafe(Bei0.shiftUp(x,upShift)); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------

