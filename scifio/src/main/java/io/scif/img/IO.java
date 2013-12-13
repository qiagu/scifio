/*
 * #%L
 * SCIFIO library for reading and converting scientific file formats.
 * %%
 * Copyright (C) 2011 - 2013 Open Microscopy Environment:
 *   - Board of Regents of the University of Wisconsin-Madison
 *   - Glencoe Software, Inc.
 *   - University of Dundee
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package io.scif.img;

import io.scif.Reader;
import io.scif.Writer;
import io.scif.refs.RefManagerService;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.Context;

/**
 * A static utility class for easy access to {@link ImgSaver} and
 * {@link ImgOpener} methods. Also includes type-convenience methods for quickly
 * opening various image types.
 * <p>
 * NB: No exceptions are thrown by any methods in this class. Instead they are
 * all caught, logged, and null is returned.
 * </p>
 * <p>
 * NB: Each of these methods occurs in its own {@link Context}
 * </p>
 * 
 * @author Mark Hiner
 */
public final class IO {

	// -- Input Methods --

	/**
	 * @see ImgOpener#openImg(String)
	 */
	public static ImgPlus<?> open(final String source) throws ImgIOException {
		ImgOpener opener = new ImgOpener();
		ImgPlus<?> imgPlus = opener.openImg(source);
		register(imgPlus, opener);
		return imgPlus;
	}

	/**
	 * As {@link ImgOpener#openImg(String)} with a guaranteed {@link FloatType}.
	 */
	public static ImgPlus<FloatType> openFloat(final String source)
		throws ImgIOException
	{
		ImgOpener opener = new ImgOpener();
		ImgPlus<FloatType> imgPlus = opener.openImg(source, new FloatType());
		register(imgPlus, opener);
		return imgPlus;
	}

	/**
	 * As {@link ImgOpener#openImg(String)} with a guaranteed {@link DoubleType}.
	 */
	public static ImgPlus<DoubleType> openDouble(final String source)
		throws ImgIOException
	{
		ImgOpener opener = new ImgOpener();
		ImgPlus<DoubleType> imgPlus = opener.openImg(source, new DoubleType());
		register(imgPlus, opener);
		return imgPlus;
	}

	/**
	 * As {@link ImgOpener#openImg(String)} with a guaranteed
	 * {@link UnsignedByteType}.
	 */
	public static ImgPlus<UnsignedByteType> openUnsignedByte(final String source)
		throws ImgIOException
	{
		ImgOpener opener = new ImgOpener();
		ImgPlus<UnsignedByteType> imgPlus = opener.openImg(source, new UnsignedByteType());
		register(imgPlus, opener);
		return imgPlus;
	}

	/**
	 * @see ImgOpener#openImg(String, RealType)
	 */
	public static <T extends RealType<T> & NativeType<T>> ImgPlus<T> openImg(
		final String source, final T type) throws ImgIOException
	{
		ImgOpener opener = new ImgOpener();
		ImgPlus<T> imgPlus = opener.openImg(source, type);
		register(imgPlus, opener);
		return imgPlus;
	}

	/**
	 * @see ImgOpener#openImg(String, ImgOptions)
	 */
	public static ImgPlus<?>
		openImg(final String source, final ImgOptions imgOptions)
			throws ImgIOException
	{
		ImgOpener opener = new ImgOpener();
		ImgPlus<?> imgPlus = opener.openImg(source, imgOptions);
		register(imgPlus, opener);
		return imgPlus;
	}

	/**
	 * @see ImgOpener#openImg(String, RealType, ImgOptions)
	 */
	public static <T extends RealType<T> & NativeType<T>> ImgPlus<T> openImg(
		final String source, final T type, final ImgOptions imgOptions)
		throws ImgIOException
	{
		ImgOpener opener = new ImgOpener();
		ImgPlus<T> imgPlus = opener.openImg(source, type, imgOptions);
		register(imgPlus, opener);
		return imgPlus;
	}

	/**
	 * @see ImgOpener#openImg(String, ImgFactory)
	 */
	@SuppressWarnings("rawtypes")
	public static ImgPlus<?>
		openImg(final String source, final ImgFactory imgFactory)
			throws ImgIOException
	{
		ImgOpener opener = new ImgOpener();
		ImgPlus<?> imgPlus = opener.openImg(source, imgFactory);
		register(imgPlus, opener);
		return imgPlus;
	}

	/**
	 * @see ImgOpener#openImg(String, ImgFactory, ImgOptions)
	 */
	@SuppressWarnings("rawtypes")
	public static ImgPlus<?> openImg(final String source,
		final ImgFactory imgFactory, final ImgOptions imgOptions)
		throws ImgIOException
	{
		ImgOpener opener = new ImgOpener();
		ImgPlus<?> imgPlus = opener.openImg(source, imgFactory, imgOptions);
		register(imgPlus, opener);
		return imgPlus;
	}

	/**
	 * @see ImgOpener#openImg(String, ImgFactory, RealType)
	 */
	public static <T extends RealType<T>> ImgPlus<T> openImg(final String source,
		final ImgFactory<T> imgFactory, final T type) throws ImgIOException
	{
		ImgOpener opener = new ImgOpener();
		ImgPlus<T> imgPlus = opener.openImg(source, imgFactory, type);
		register(imgPlus, opener);
		return imgPlus;
	}

	/**
	 * @see ImgOpener#openImg(Reader, RealType, ImgOptions)
	 */
	public static <T extends RealType<T> & NativeType<T>> ImgPlus<T> openImg(
		final Reader reader, final T type, final ImgOptions imgOptions)
		throws ImgIOException
	{
		ImgOpener opener = new ImgOpener();
		ImgPlus<T> imgPlus = opener.openImg(reader, type, imgOptions);
		register(imgPlus, opener);
		return imgPlus;
	}

	/**
	 * @see ImgOpener#openImg(Reader, RealType, ImgFactory, ImgOptions)
	 */
	public static <T extends RealType<T>> ImgPlus<T> openImg(final Reader reader,
		final T type, final ImgFactory<T> imgFactory, final ImgOptions imgOptions)
		throws ImgIOException
	{
		ImgOpener opener = new ImgOpener();
		ImgPlus<T> imgPlus = opener.openImg(reader, type, imgFactory, imgOptions);
		register(imgPlus, opener);
		return imgPlus;
	}

	// -- Output Methods --

	/**
	 * @see ImgSaver#saveImg(String, Img)
	 */
	public static void saveImg(
		final String dest, final Img<?> img) throws ImgIOException
	{
		try {
			new ImgSaver().saveImg(dest, img);
		}
		catch (final IncompatibleTypeException e) {
			throw new ImgIOException(e);
		}
	}

	/**
	 * @see ImgSaver#saveImg(String, ImgPlus, int)
	 */
	public static void saveImg(
		final String dest, final ImgPlus<?> imgPlus, final int imageIndex)
		throws ImgIOException
	{
		try {
			new ImgSaver().saveImg(dest, imgPlus, imageIndex);
		}
		catch (final IncompatibleTypeException e) {
			throw new ImgIOException(e);
		}
	}

	/**
	 * @see ImgSaver#saveImg(Writer, Img)
	 */
	public static void saveImg(
		final Writer writer, final Img<?> img) throws ImgIOException
	{
		try {
			new ImgSaver().saveImg(writer, img);
		}
		catch (final IncompatibleTypeException e) {
			throw new ImgIOException(e);
		}
	}

	/**
	 * @see ImgSaver#saveImg(Writer, ImgPlus, int)
	 */
	public static void saveImg(
		final Writer writer, final ImgPlus<?> imgPlus, final int imageIndex)
		throws ImgIOException
	{
		try {
			new ImgSaver().saveImg(writer, imgPlus, imageIndex);
		}
		catch (final IncompatibleTypeException e) {
			throw new ImgIOException(e);
		}
	}

	// -- Helper methods --

	private static void register(ImgPlus<?> imgPlus,
		AbstractImgIOComponent component)
	{
		Context ctx = component.getContext();
		RefManagerService refManagerService =
			ctx.getService(RefManagerService.class);
		refManagerService.manage(imgPlus, ctx);
	}
}
