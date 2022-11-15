package nzqr.jmh.benchmarks.arithmetic;

/** <pre>
 * java --enable-preview -cp target\benchmarks.jar nzqr.jmh.benchmarks.arithmetic.DivideAndRemainder
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2022-11-14
 */

public class DivideAndRemainder extends Base {

  @Override
  public final Object operation (final Object z0,
                                 final Object z1) {
    return Naturals.get().divideAndRemainder(z0,z1); }

  public static final void main (final String[] args)  {
    Defaults.run("DivideAndRemainder"); } }
