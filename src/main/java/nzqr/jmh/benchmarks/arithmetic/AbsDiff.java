package nzqr.jmh.benchmarks.arithmetic;

/** <pre>
 * java -cp target\benchmarks.jar nzqr.jmh.benchmarks.arithmetic.AbsDiff
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2022-11-14
 */

public class AbsDiff extends Base {

  @Override
  public final Object operation (final Object z0,
                                 final Object z1) {
    return Naturals.get().absDiff(z0,z1); }

  public static final void main (final String[] args)  {
    Defaults.run("AbsDiff"); } }
