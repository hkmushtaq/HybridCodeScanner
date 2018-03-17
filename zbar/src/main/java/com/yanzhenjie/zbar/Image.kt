package com.yanzhenjie.zbar

/**
 * Kotlin version of Image.java
 * stores image data samples along with associated format and size
 * metadata.
 *
 * @author hkmushtaq
 * @since 03/17/2018
 */
public class Image {

    val symbols: SymbolSet
        get() = SymbolSet(getSymbols(peer))

    private var peer: Long = 0
    private var data: Any? = null

    companion object {
        init {
            System.loadLibrary("Store")
            init()
        }

        @JvmStatic
        private external fun init()
    }

    constructor() {
        peer = create()
    }

    constructor(width: Int, height: Int) : this() {
        setSize(width, height)
    }

    constructor(width: Int, height: Int, format: String) : this(width, height) {
        setFormat(format)
    }

    constructor(format: String) : this() {
        setFormat(format)
    }

    constructor(peer: Long) {
        this.peer = peer
    }

    fun convert(format: String): Image? {
        val newPeer = convert(peer, format)
        if (newPeer == 0L) {
            return null
        }
        return Image(newPeer)
    }

    @Synchronized
    fun destroy() {
        if (peer != 0L) {
            destroy(peer)
            peer = 0
        }
    }

    protected fun finalize() = destroy()


    //================================================================================
    // region Externals
    //================================================================================

    private external fun create(): Long
    private external fun destroy(peer: Long)
    private external fun convert(peer: Long, format: String): Long

    external fun getFormat(): String
    external fun getSequence(): Int
    external fun getWidth(): Int
    external fun getHeight(): Int
    external fun getSize(): Array<Int>
    external fun getCrop(): IntArray
    external fun getData(): ByteArray
    external fun getSymbols(peer: Long): Long

    external fun setFormat(format: String)
    external fun setSequence(seq: Int)

    external fun setSize(width: Int, height: Int)
    external fun setSize(size: IntArray)
    external fun setCrop(x: Int, y: Int, width: Int, height: Int)
    external fun setCrop(crop: IntArray)
    external fun setData(data: ByteArray)
    external fun setData(data: IntArray)

    // endregion


}