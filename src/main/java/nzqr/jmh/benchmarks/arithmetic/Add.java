package nzqr.jmh.benchmarks.arithmetic;

import nzqr.java.numbers.Naturals;

/** <pre>
 * java -cp target\benchmarks.jar nzqr.jmh.benchmarks.arithmetic.Add
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2022-09-04
 */

public class Add extends Base {

  @Override
  public final Object operation (final Object z0,
                                 final Object z1) {
    return Naturals.get().add(z0,z1); }

  public static final void main (final String[] args)  {
    Defaults.run("Add"); } }
