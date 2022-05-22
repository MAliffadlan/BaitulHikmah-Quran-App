package com.idn.smart.tiyas.quranqu.model

import java.io.Serializable

class ModelSurah : Serializable {

    var arti: String? = null

    @JvmField
    var asma: String? = null

    @JvmField
    var ayat: String? = null

    @JvmField
    var nama: String? = null

    @JvmField
    var type: String? = null
    var audio: String? = null

    @JvmField
    var nomor: String? = null
    var keterangan: String? = null

}