/*------------------------------------------------------------------------
 *  SymbolSet
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
class SymbolSet internal constructor(private var peer: Long) : java.util.AbstractCollection<Symbol>() {

    protected fun finalize() = destroy()

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
     * Retrieve an iterator over the Symbol elements in this collection.
     */
    override fun iterator(): SymbolIterator {
        val sym = firstSymbol(peer)
        return if (sym == 0L) SymbolIterator(null) else SymbolIterator(Symbol(sym))

    }

    override val size: Int
        external get

    /**
     * Retrieve C pointer to first symbol in the set.
     */
    private external fun firstSymbol(peer: Long): Long

    companion object {

        init {
            System.loadLibrary("zbar")
            init()
        }

        @JvmStatic
        private external fun init()
    }
}
