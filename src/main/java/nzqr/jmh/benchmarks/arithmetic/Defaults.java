package nzqr.jmh.benchmarks.arithmetic;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import nzqr.java.SystemInfo;

/** Base for arithmetic operation benchmarks.
 *
 * <pre>
 * java -cp target\benchmarks.jar nzqr.jmh.benchmarks.arithmetic.Base
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2023-12-21
 */

public final class Defaults {

  //--------------------------------------------------------------

  private static final DateTimeFormatter DTF =
    DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

  public static final String now () {
    return LocalDateTime.now().format(DTF); }

  //--------------------------------------------------------------

  public static final Options options (final String fileName,
                                       final String includes) {
    final File parent = new File("output");
    final boolean created = parent.mkdirs();
    assert created;
    final File csv =
      new File(parent,
        fileName
        + "-"  + SystemInfo.model()
        + "-" + now()
        + ".csv");
    //final File json =
    //  new File(parent, fileName + "-" + now() + ".json");
    return new OptionsBuilder()
      .mode(Mode.AverageTime)
      .timeUnit(TimeUnit.MILLISECONDS)
      .include(includes)
      //.resultFormat(ResultFormatType.JSON)
      //.result(json.getPath())
      .resultFormat(ResultFormatType.CSV)
      .result(csv.getPath())
      .threads(1)
      .shouldFailOnError(true)
      .shouldDoGC(true)
      .jvmArgs(
        "--enable-preview",
        "-Xmx12g",
        "-Xms12g",
        //"-Xmn2500m",
        "-XX:+UseFMA",
        "-Xbatch",
        "-server")
      .forks(3)
      .warmupIterations(5)
      .warmupTime(TimeValue.seconds(16))
      .measurementIterations(5)
      .measurementTime(TimeValue.seconds(16))
      .build(); }

  //--------------------------------------------------------------

  public static final void run (final String fileName,
                                final String includes) {

    try {
      final Runner runner =
        new Runner(Defaults.options(fileName,includes));
      runner.run(); }
    catch (final RunnerException e) {
      throw new RuntimeException(e); } }

  public static final void run (final String includes) {
    run(includes,includes); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
