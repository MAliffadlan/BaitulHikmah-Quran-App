package com.idn.smart.tiyas.quranqu.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.idn.smart.tiyas.quranqu.BuildConfig
import com.idn.smart.tiyas.quranqu.R
import com.idn.smart.tiyas.quranqu.adapter.AyatAdapter
import com.idn.smart.tiyas.quranqu.model.ModelAyat
import com.idn.smart.tiyas.quranqu.model.ModelSurah
import com.idn.smart.tiyas.quranqu.networking.Api
import kotlinx.android.synthetic.main.activity_detail_surah.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.util.ArrayList

class DetailSurahActivity : AppCompatActivity() {

    var nomor: String? = null
    var nama: String? = null
    var arti: String? = null
    var type: String? = null
    var ayat: String? = null
    var keterangan: String? = null
    var audio: String? = null
    var modelSurah: ModelSurah? = null
    var ayatAdapter: AyatAdapter? = null
    var progressDialog: ProgressDialog? = null
    var modelAyat: MutableList<ModelAyat> = ArrayList()
    var mHandler: Handler? = null

    @SuppressLint("RestrictedApi", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_surah)

        //set toolbar
        toolbar_detail.setTitle(null)
        setSupportActionBar(toolbar_detail)
        if (BuildConfig.DEBUG && supportActionBar == null) {
            error("Assertion failed")
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mHandler = Handler()

        //get data dari ListSurah
        modelSurah = intent.getSerializableExtra("detailSurah") as ModelSurah
        if (modelSurah != null) {
            nomor = modelSurah!!.nomor
            nama = modelSurah!!.nama
            arti = modelSurah!!.arti
            type = modelSurah!!.type
            ayat = modelSurah!!.ayat
            audio = modelSurah!!.audio
            keterangan = modelSurah!!.keterangan

            fabStop.visibility = View.GONE
            fabPlay.visibility = View.VISIBLE

            //Set text
            tvHeader.text = nama
            tvTitle.text = nama
            tvSubTitle.text = arti
            tvInfo.text = "$type - $ayat Ayat "

            tvKet.text = Html.fromHtml(
                keterangan,
                Html.FROM_HTML_MODE_COMPACT
            )

            //get & play Audio
            val mediaPlayer = MediaPlayer()
            fabPlay.setOnClickListener(View.OnClickListener {
                try {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    mediaPlayer.setDataSource(audio)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                fabPlay.visibility = View.GONE
                fabStop.visibility = View.VISIBLE
            })

            fabStop.setOnClickListener(View.OnClickListener {
                mediaPlayer.stop()
                mediaPlayer.reset()
                fabPlay.visibility = View.VISIBLE
                fabStop.visibility = View.GONE
            })
        }

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Sabar Bosskuu")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Downloading assets...")

        rvAyat.layoutManager = LinearLayoutManager(this)
        rvAyat.setHasFixedSize(true)

        //Methods get data
        listAyat()
    }

    private fun listAyat (){
        progressDialog!!.show()
        AndroidNetworking.get(Api.URL_LIST_AYAT)
                .addPathParameter("nomor", nomor)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        for (i in 0 until response.length()) {
                            try {
                                progressDialog!!.dismiss()
                                val dataApi = ModelAyat()
                                val jsonObject = response.getJSONObject(i)
                                dataApi.nomor = jsonObject.getString("nomor")
                                dataApi.arab = jsonObject.getString("ar")
                                dataApi.indo = jsonObject.getString("id")
                                dataApi.terjemahan = jsonObject.getString("tr")
                                modelAyat.add(dataApi)
                                showListAyat()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                Toast.makeText(this@DetailSurahActivity, "Connection interrupted!" +
                                        "Reconnecting To Server",
                                        Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onError(anError: ANError) {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@DetailSurahActivity, "Tidak ada jaringan internet!",
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun showListAyat() {
        ayatAdapter = AyatAdapter(this@DetailSurahActivity, modelAyat)
        rvAyat!!.adapter = ayatAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
