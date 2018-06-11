
package io.scif;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;

import org.junit.Test;
import org.scijava.Context;
import org.scijava.io.handle.DataHandleService;
import org.scijava.io.location.FileLocation;
import org.scijava.io.location.Location;
import org.scijava.log.LogService;

public class AxisGuesserTest {

	/** Axis type for unclassified axes. */
	public static final int UNKNOWN_AXIS = 0;

	/** Axis type for focal planes. */
	public static final int Z_AXIS = 1;

	/** Axis type for time points. */
	public static final int T_AXIS = 2;

	/** Axis type for channels. */
	public static final int C_AXIS = 3;

	/** Axis type for series. */
	public static final int S_AXIS = 4;

	/** Method for testing pattern guessing logic. */

	// @Test
	public void testAxisguessing() throws FormatException, IOException {
		final Context context = new Context();
		final SCIFIO scifio = new SCIFIO(context);
		final LogService log = scifio.log();
		final DataHandleService dataHandleService = context.getService(
			DataHandleService.class);
		URL resource = this.getClass().getResource("formats/tiny-10x10x3.tif");
		final FileLocation file = new FileLocation(resource.getFile());

		final String pat = scifio.filePattern().findPattern(file.getName());
		if (pat == null) log.info("No pattern found.");
		else {
			log.info("Pattern = " + pat);
			final FilePattern fp = new FilePattern(file, pat, dataHandleService);
			if (fp.isValid()) {
				log.info("Pattern is valid.");
				final Location id = fp.getFiles()[0];
				if (!dataHandleService.exists(id)) {
					log.info("File '" + id + "' does not exist.");
				}
				else {
					// read dimensional information from first file
					log.info("Reading first file ");
					final Reader reader = scifio.initializer().initializeReader(id);
					final AxisType[] dimOrder = (AxisType[]) reader.getMetadata().get(0)
						.getAxes().toArray();
					final long sizeZ = reader.getMetadata().get(0).getAxisLength(Axes.Z);
					final long sizeT = reader.getMetadata().get(0).getAxisLength(
						Axes.TIME);
					final long sizeC = reader.getMetadata().get(0).getAxisLength(
						Axes.CHANNEL);
					final boolean certain = reader.getMetadata().get(0).isOrderCertain();
					reader.close();
					log.info("[done]");
					log.info("\tdimOrder = " + Arrays.toString(dimOrder) + " (" + (certain
						? "certain" : "uncertain") + ")");
					log.info("\tsizeZ = " + sizeZ);
					log.info("\tsizeT = " + sizeT);
					log.info("\tsizeC = " + sizeC);

					// guess axes
					final AxisGuesser ag = new AxisGuesser(fp, dimOrder, sizeZ, sizeT,
						sizeC, certain);

					// output results
					final String[] blocks = fp.getBlocks();
					final String[] prefixes = fp.getPrefixes();
					final int[] axes = ag.getAxisTypes();
					final AxisType[] newOrder = ag.getAdjustedOrder();
					final boolean isCertain = ag.isCertain();
					log.info("Axis types:");
					for (int i = 0; i < blocks.length; i++) {
						String axis;
						switch (axes[i]) {
							case Z_AXIS:
								axis = "Z";
								break;
							case T_AXIS:
								axis = "T";
								break;
							case C_AXIS:
								axis = "C";
								break;
							default:
								axis = "?";
						}
						log.info("\t" + blocks[i] + "\t" + axis + " (prefix = " +
							prefixes[i] + ")");
					}
					if (!Arrays.equals(dimOrder, newOrder)) {
						log.info("Adjusted dimension order = " + Arrays.toString(newOrder) +
							" (" + (isCertain ? "certain" : "uncertain") + ")");
					}
				}
			}
			else log.info("Pattern is invalid: " + fp.getErrorMessage());
		}
	}

}

// -- Notes --

// INPUTS: file pattern, dimOrder, sizeZ, sizeT, sizeC, isCertain
//
// 1) Fill in all "known" dimensional axes based on known patterns and
//    conventions
//      * known internal axes (ZCT) have isCertain == true
//      * known dimensional axes have a known pattern or convention
//    After that, we are left with only unknown slots, which we must guess.
//
// 2) First, we decide whether we really "believe" the reader. There is a
//    special case where we may decide that it got Z and T mixed up:
//      * if a Z block was found, but not a T block:
//          if !isOrderCertain, and sizeZ > 1, and sizeT == 1, swap 'em
//      * else if a T block was found, but not a Z block:
//          if !isOrderCertain and sizeT > 1, and sizeZ == 1, swap 'em
//    At this point, we can (have to) trust the internal ordering, and use it
//    to decide how to fill in the remaining dimensional blocks.
//
// 3) Set canBeZ to true iff no Z block is assigned and sizeZ == 1.
//    Set canBeT to true iff no T block is assigned and sizeT == 1.
//    Go through the blocks in order from left to right:
//      * If canBeZ, assign Z and set canBeZ to false.
//      * If canBeT, assign T and set canBeT to false.
//      * Otherwise, assign C.
//
// OUTPUTS: list of axis assignments, new dimOrder
