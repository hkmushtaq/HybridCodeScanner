/*------------------------------------------------------------------------
 *  SymbolIterator
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
 * Iterator over a SymbolSet.
 */
class SymbolIterator : MutableIterator<Symbol> {

    private var current: Symbol?

    constructor(first: Symbol?) {
        this.current = first
    }

    /**
     * Returns true if the iteration has more elements.
     */
    override fun hasNext(): Boolean {
        return current != null
    }

    /**
     * Retrieves the next element in the iteration.
     */
    override fun next(): Symbol {
        val result: Symbol =
            current ?: throw NoSuchElementException("access past end of SymbolIterator")
        val sym = result.next
        if (sym != 0L)
            current = Symbol(sym)
        else
            current = null
        return result
    }

    /**
     * Raises UnsupportedOperationException.
     */
    override fun remove() {
        throw UnsupportedOperationException("SymbolIterator is immutable")
    }
}
