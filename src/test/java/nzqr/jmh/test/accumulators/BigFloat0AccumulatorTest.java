package nzqr.jmh.test.accumulators;

import java.util.List;

import org.junit.jupiter.api.Test;

import nzqr.java.test.Common;
import nzqr.java.test.accumulators.EFloatAccumulator;

//----------------------------------------------------------------
/** Test summation algorithms.
 * <p>
 * <pre>
 * mvn test -Dtest=nzqr/java/test/accumulators/BigFloat0AccumulatorTest > BigFloat0AccumulatorTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2019-12-02
 */

public final class BigFloat0AccumulatorTest {

  //--------------------------------------------------------------
  private static final int DIM = 256;
  private static final List<String> accumulators =
    List.of("nzqr.jmh.accumulators.BigFloat0Accumulator");
  @SuppressWarnings("static-method")
  @Test
  public final void tests () {
    //Debug.DEBUG=false;
    //Debug.println();
    //Debug.println(Classes.className(this));
    Common.sumTests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      EFloatAccumulator.make());
    Common.l2Tests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      EFloatAccumulator.make());
    Common.dotTests(
      Common.generators(DIM),
      Common.makeAccumulators(accumulators),
      EFloatAccumulator.make()); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
