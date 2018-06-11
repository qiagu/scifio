
package io.scif.img;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import net.imglib2.exception.IncompatibleTypeException;

import org.junit.Test;
import org.scijava.Context;
import org.scijava.io.location.FileLocation;
import org.scijava.io.location.Location;
import org.scijava.io.location.LocationService;

/**
 * @author gabriel
 */
public class OpenKnownImgsTest {

	private final Context ctx = new Context();
	private final ImgOpener o = new ImgOpener(ctx);
	private final ImgSaver s = new ImgSaver(ctx);
	private final LocationService loc = ctx.getService(LocationService.class);

//	@Test
	public void testPNG() throws Exception {
		testImgFile("tiny-10x10x3.png", 10, 10, 3);
	}

//	@Test
	public void testJPEG() throws Exception {
		testImgFile("tiny-10x10x3.jpg", 10, 10, 3);
	}

//	@Test
	public void testTinyTiff() throws Exception {
		testImgFile("tiny-10x10x3.tif", 10, 10, 3);
	}

//	@Test
	public void testGIFRemote() throws Exception {
		testImgLoc("blobs.gif", 256, 254, 0, loc.resolve(
			"http://imagej.net/images/blobs.gif"));
	}

	public void testJPEGRemote() throws Exception {
		testImgLoc("2d-gel", 694, 391, 0, loc.resolve(
			"https://imagej.net/images/2D_Gel.jpg"));
	}

//	@Test
	public void testGIFLocal() throws Exception {
		testImgFile("blobs.gif", 256, 254, 0);
	}

	private void testImgFile(final String fileName, final int dim0,
		final int dim1, final int dim2) throws ImgIOException,
		IncompatibleTypeException, IOException
	{
		final FileLocation sourceLoc = new FileLocation(this.getClass().getResource(
			fileName).getFile());
		testImgLoc(fileName, dim0, dim1, dim2, sourceLoc);
	}

	private void testImgLoc(final String fileName, final int dim0, final int dim1,
		final int dim2, final Location sourceLoc) throws ImgIOException,
		IOException, IncompatibleTypeException
	{
		final SCIFIOImgPlus<?> img = o.openImgs(sourceLoc).get(0);

		assertEquals(3, img.numDimensions());
		assertEquals(dim0, img.dimension(0));
		assertEquals(dim1, img.dimension(1));
		assertEquals(dim2, img.dimension(2));

		final String[] split = fileName.split("\\.");
		final FileLocation id = new FileLocation(File.createTempFile(split[0], "." +
			split[1]));
		s.saveImg(id, img);

		final SCIFIOImgPlus<?> iout = o.openImgs(id).get(0);

		assertEquals(3, iout.numDimensions());
		assertEquals(dim0, iout.dimension(0));
		assertEquals(dim1, iout.dimension(1));
		assertEquals(dim2, iout.dimension(2));
	}
}
