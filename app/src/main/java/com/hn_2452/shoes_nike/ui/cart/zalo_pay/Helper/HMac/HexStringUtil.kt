package com.example.zalopaykotlin.Helper.HMac

import java.util.Locale

class HexStringUtil {

    private val HEX_CHAR_TABLE = byteArrayOf(
        '0'.toByte(), '1'.toByte(), '2'.toByte(), '3'.toByte(),
        '4'.toByte(), '5'.toByte(), '6'.toByte(), '7'.toByte(),
        '8'.toByte(), '9'.toByte(), 'a'.toByte(), 'b'.toByte(),
        'c'.toByte(), 'd'.toByte(), 'e'.toByte(), 'f'.toByte()
    )

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param raw The raw byte array to convert.
     * @return The hexadecimal string representation of the byte array.
     */
    companion object {
        private val HEX_CHAR_TABLE = byteArrayOf(
            '0'.toByte(), '1'.toByte(), '2'.toByte(), '3'.toByte(),
            '4'.toByte(), '5'.toByte(), '6'.toByte(), '7'.toByte(),
            '8'.toByte(), '9'.toByte(), 'a'.toByte(), 'b'.toByte(),
            'c'.toByte(), 'd'.toByte(), 'e'.toByte(), 'f'.toByte()
        )
        fun byteArrayToHexString(raw: ByteArray): String {
            val hex = ByteArray(2 * raw.size)
            var index = 0

            for (b in raw) {
                val v = b.toInt() and 0xFF
                hex[index++] = HEX_CHAR_TABLE[v ushr 4]
                hex[index++] = HEX_CHAR_TABLE[v and 0x0F]
            }
            return String(hex)
        }
    }

}