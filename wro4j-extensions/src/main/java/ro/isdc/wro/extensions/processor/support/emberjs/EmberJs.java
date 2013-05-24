package ro.isdc.wro.extensions.processor.support.emberjs;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Vector;

import ro.isdc.wro.extensions.locator.WebjarUriLocator;
import ro.isdc.wro.extensions.processor.support.template.AbstractJsTemplateCompiler;


/**
 * EmberJS is a framework which provide a templating engine built on top of Handlebars.
 *
 * @author blemoine
 */
public class EmberJs
    extends AbstractJsTemplateCompiler {
  /*
   * The Ember Template Compiler is built for CommonJs environment, but Rhino doesn't comply with CommonJs Standard
   * There is no 'exports' object in Rhino, so this file creates it, as well as an helper function
   */
  private static final String DEFAULT_HEADLESS_RHINO_JS = "headless-rhino.js";

  /**
   * visible for testing, the init of a HandlebarsJs template
   */
  @Override
  public String compile(final String content, final String name) {
    final String precompiledFunction = super.compile(content, "");
    return String.format("(function() {Ember.TEMPLATES[%s] = Ember.Handlebars.template(%s)})();", name, precompiledFunction);
  }

  @Override
  protected String getCompileCommand() {
    // Function present in headless-ember
    return "precompile";
  }

  @Override
  protected InputStream getCompilerAsStream() throws IOException {
    final Vector<InputStream> inputStreams = new Vector<InputStream>();
    inputStreams.add(new WebjarUriLocator().locate(WebjarUriLocator.createUri("jquery.js")));
    inputStreams.add(new WebjarUriLocator().locate(WebjarUriLocator.createUri("handlebars.js")));
    inputStreams.add(new WebjarUriLocator().locate(WebjarUriLocator.createUri("ember.js")));
    inputStreams.add(EmberJs.class.getResourceAsStream(DEFAULT_HEADLESS_RHINO_JS));
    //inputStreams.add(EmberJs.class.getResourceAsStream(DEFAULT_EMBER_TEMPLATE_COMPILER_JS));
    //inputStreams.add(fis3);

    return new SequenceInputStream(inputStreams.elements());
  }
}
