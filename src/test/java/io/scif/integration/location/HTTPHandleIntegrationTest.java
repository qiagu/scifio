
package io.scif.integration.location;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.scijava.Context;
import org.scijava.io.http.HTTPLocation;

/**
 * tests opening larger images
 * 
 * @author gabriel
 */
public class HTTPHandleIntegrationTest {
	
	private Context ctx = new Context();

	
	// @Test
	public void testJPEG() throws MalformedURLException, URISyntaxException, ImgIOException {
		ImgOpener opener = new ImgOpener(ctx);
		opener.openImgs(new HTTPLocation("https://imagej.net/images/Diatoms.jpg"));
	}
}
