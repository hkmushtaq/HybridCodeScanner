/*------------------------------------------------------------------------
 *  Symbol
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
 * Immutable container for decoded result symbols associated with an image
 * or a composite symbol.
 */
public class Symbol {

    /**
     * C pointer to a zbar_symbol_t.
     */
    private var peer: Long = 0

    /**
     * Cached attributes.
     */
    private var type: Int = 0

    internal constructor(peer: Long) {
        this.peer = peer
    }

    /**
     * Retrieve symbology boolean configs settings used during decode.
     */
    val configMask: Int
        external get

    /**
     * Retrieve symbology characteristics detected during decode.
     */
    val modifierMask: Int
        external get

    /**
     * Retrieve data decoded from symbol as a String.
     */
    val data: String
        external get

    /**
     * Retrieve raw data bytes decoded from symbol.
     */
    val dataBytes: ByteArray
        external get

    /**
     * Retrieve a symbol confidence metric.  Quality is an unscaled,
     * relative quantity: larger values are better than smaller
     * values, where "large" and "small" are application dependent.
     */
    val quality: Int
        external get

    /**
     * Retrieve current cache count.  When the cache is enabled for
     * the image_scanner this provides inter-frame reliability and
     * redundancy information for video streams.
     *
     * @returns < 0 if symbol is still uncertain
     * @returns 0 if symbol is newly verified
     * @returns > 0 for duplicate symbols
     */
    val count: Int
        external get

    /**
     * Retrieve an approximate, axis-aligned bounding box for the
     * symbol.
     */
    val bounds: IntArray?
        get() {
            val n = getLocationSize(peer)
            if (n <= 0)
                return null

            val bounds = IntArray(4)
            var xmin = Integer.MAX_VALUE
            var xmax = Integer.MIN_VALUE
            var ymin = Integer.MAX_VALUE
            var ymax = Integer.MIN_VALUE

            for (i in 0 until n) {
                val x = getLocationX(peer, i)
                if (xmin > x) xmin = x
                if (xmax < x) xmax = x

                val y = getLocationY(peer, i)
                if (ymin > y) ymin = y
                if (ymax < y) ymax = y
            }
            bounds[0] = xmin
            bounds[1] = ymin
            bounds[2] = xmax - xmin
            bounds[3] = ymax - ymin
            return bounds
        }

    /**
     * Retrieve general axis-aligned, orientation of decoded
     * symbol.
     */
    val orientation: Int
        external get

    val next: Long
        external get

    /**
     * Retrieve components of a composite result.
     */
    val components: SymbolSet
        get() = SymbolSet(getComponents(peer))

    protected fun finalize() {
        destroy()
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

    /**
     * Release the associated peer instance.
     */
    private external fun destroy(peer: Long)

    /**
     * Retrieve type of decoded symbol.
     */
    fun getType(): Int {
        if (type == 0)
            type = getType(peer)
        return type
    }

    private external fun getType(peer: Long): Int

    private external fun getLocationSize(peer: Long): Int

    private external fun getLocationX(peer: Long, idx: Int): Int

    private external fun getLocationY(peer: Long, idx: Int): Int

    fun getLocationPoint(idx: Int): IntArray {
        val p = IntArray(2)
        p[0] = getLocationX(peer, idx)
        p[1] = getLocationY(peer, idx)
        return p
    }

    private external fun getComponents(peer: Long): Long

    companion object {
        /**
         * No symbol decoded.
         */
        const val NONE = 0
        /**
         * Symbol detected but not decoded.
         */
        const val PARTIAL = 1

        /**
         * EAN-8.
         */
        const val EAN8 = 8
        /**
         * UPC-E.
         */
        const val UPCE = 9
        /**
         * ISBN-10 (from EAN-13).
         */
        const val ISBN10 = 10
        /**
         * UPC-A.
         */
        const val UPCA = 12
        /**
         * EAN-13.
         */
        const val EAN13 = 13
        /**
         * ISBN-13 (from EAN-13).
         */
        const val ISBN13 = 14
        /**
         * Interleaved 2 of 5.
         */
        const val I25 = 25
        /**
         * DataBar (RSS-14).
         */
        const val DATABAR = 34
        /**
         * DataBar Expanded.
         */
        const val DATABAR_EXP = 35
        /**
         * Codabar.
         */
        const val CODABAR = 38
        /**
         * Code 39.
         */
        const val CODE39 = 39
        /**
         * PDF417.
         */
        const val PDF417 = 57
        /**
         * QR Code.
         */
        const val QRCODE = 64
        /**
         * Code 93.
         */
        const val CODE93 = 93
        /**
         * Code 128.
         */
        const val CODE128 = 128

        init {
            System.loadLibrary("zbar")
            init()
        }

        @JvmStatic
        private external fun init()
    }
}
