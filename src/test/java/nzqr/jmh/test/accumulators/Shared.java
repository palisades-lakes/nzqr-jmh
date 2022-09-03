package nzqr.jmh.test.accumulators;

import java.util.List;

/** Shared data/code for accumulator tests.
 * Not instantiable. Class slots/methods only.
 *
 * @author mcdonald dot john dot alan at gmail dot com
 * @version 2019-07-22
 */
public final class Shared {

  public static final int TEST_DIM = 256; //(1 * 64 * 1024) - 1;
  //1 << 14;

  public static final List<String> accumulators () {
    return
      List.of(
        "nzqr.jmh.accumulators.BigFloat0Accumulator"
        //,
        //"nzqr.jmh.accumulators.IFastAccumulator",
        //"nzqr.jmh.accumulators.ZhuHayesGCAccumulator",
        //"nzqr.jmh.accumulators.ZhuHayesGCBranch",
        //"nzqr.jmh.accumulators.ZhuHayesBranch"
        // ,
        // "nzqr.java.accumulators.RationalFloatAccumulator"
        // ,
        // // Same as non-strict, just slower
        // "nzqr.jmh.accumulators.DoubleAccumulator",
        // "nzqr.jmh.accumulators.StrictDoubleFmaAccumulator",
        // // overflow unless values more limited
        // "nzqr.jmh.accumulators.FloatAccumulator",
        // "nzqr.jmh.accumulators.FloatFmaAccumulator",
        // // Too slow to keep testing
        // "nzqr.jmh.accumulators.BigDecimalAccumulator",
        // "nzqr.jmh.accumulators.BigFractionAccumulator",
        // "nzqr.java.accumulators.DoubleFmaAccumulator",
        // "nzqr.jmh.accumulators.KahanFmaAccumulator",
        // //,
        // // Broken in many ways.
        // // Doesn't overflow to infinity, or accumulate extreme
        // // values correctly.
        // // Slow as well.
        // //"nzqr.jmh.accumulators.RatioAccumulator"
        ); }

  //--------------------------------------------------------------
  // disable constructor
  //--------------------------------------------------------------

  private Shared () {
    throw new UnsupportedOperationException(
      "can't instantiate " + getClass()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
