/*------------------------------------------------------------------------
 *  ImageScanner
 *
 *  Copyright 2007-2010 (c) Jeff Brown <spadix@users.sourceforge.net>
 *
 *  This file is part of the ZBar Bar Code Reader.
 *
 *  The ZBar Bar Code Reader is free software; you can redistribute it
 *  and/or modify it under the terms of the GNU Lesser Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  The ZBar Bar Code Reader is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 *  of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with the ZBar Bar Code Reader; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 *  Boston, MA  02110-1301  USA
 *
 *  http://sourceforge.net/projects/zbar
 *------------------------------------------------------------------------*/

package com.yanzhenjie.zbar

/**
 * Read barcodes from 2-D images.
 */
class ImageScanner {

    /**
     * Retrieve decode results for last scanned image.
     *
     * @returns the SymbolSet result container
     */
    val results: SymbolSet
        get() = SymbolSet(getResults(peer))

    /**
     * C pointer to a zbar_image_scanner_t.
     */
    private var peer: Long = 0

    constructor() {
        peer = create()
    }

    /**
     * Clean up native data associated with an instance.
     */
    @Synchronized
    fun destroy() {
        if (peer != 0L) {
            destroy(peer)
            peer = 0
        }
    }

    protected fun finalize() = destroy()

    /**
     * Set config for indicated symbology (0 for all) to specified value.
     */
    @Throws(IllegalArgumentException::class)
    external fun setConfig(symbology: Int, config: Int, value: Int)

    /**
     * Parse configuration string and apply to image scanner.
     */
    external fun parseConfig(config: String)

    /**
     * Enable or disable the inter-image result cache (default disabled).
     * Mostly useful for scanning video frames, the cache filters duplicate
     * results from consecutive images, while adding some consistency
     * checking and hysteresis to the results.  Invoking this method also
     * clears the cache.
     */
    external fun enableCache(enable: Boolean)

    /**
     * Scan for symbols in provided Image.
     * The image format must currently be "Y800" or "GRAY".
     *
     * @returns the number of symbols successfully decoded from the image.
     */
    external fun scanImage(image: Image): Int

    private external fun getResults(peer: Long): Long

    /**
     * Destroy the associated peer instance.
     */
    private external fun destroy(peer: Long)

    /**
     * Create an associated peer instance.
     */
    private external fun create(): Long

    companion object {
        init {
            System.loadLibrary("zbar")
            init()
        }

        @JvmStatic
        private external fun init()
    }
}
