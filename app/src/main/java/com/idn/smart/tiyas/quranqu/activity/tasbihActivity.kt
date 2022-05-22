package com.idn.smart.tiyas.quranqu.activity


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.idn.smart.tiyas.quranqu.R
import kotlinx.android.synthetic.main.activity_tasbih.*

class tasbihActivity : AppCompatActivity() {
    private var dzikir : TextView? =  null
    private var zikir = 0
    private var vibe : Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasbih)

        dzikir = findViewById<View>(R.id.dzikir) as TextView
        vibe = getSystemService(VIBRATOR_SERVICE) as Vibrator
        cdv_reset.setOnClickListener {
            dzikir!!.text = "0"
        }
    }

    fun  Subhanallah(view: View?){
        zikir += 1
        tampil(zikir)
        vibe!!.vibrate(100)
    }

    fun  Alhamdulillah(view: View?){
        zikir += 1
        tampil(zikir)
        vibe!!.vibrate(100)
    }

    fun  AllahuAkbar(view: View?){
        zikir += 1
        tampil(zikir)
        vibe!!.vibrate(100)
    }

    fun  Lailahaillallah(view: View?){
        zikir += 1
        tampil(zikir)
        vibe!!.vibrate(100)
    }

    @SuppressLint("SetTextI18n")
    private fun tampil(zikir: Int) {
        dzikir!!.text = "" + zikir

    }




}