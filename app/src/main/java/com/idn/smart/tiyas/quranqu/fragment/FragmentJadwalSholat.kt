@file:Suppress("DEPRECATION")

package com.idn.smart.tiyas.quranqu.fragment

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.idn.smart.tiyas.quranqu.R
import com.idn.smart.tiyas.quranqu.model.DaftarKota
import com.idn.smart.tiyas.quranqu.utils.ClientAsyncTask
import com.vivekkaushik.datepicker.DatePickerTimeline
import kotlinx.android.synthetic.main.fragment_jadwal_sholat.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentJadwalSholat : BottomSheetDialogFragment() {

    var mString: String? = null
    private var listDaftarKota: MutableList<DaftarKota>? = null
    private var mDaftarKotaAdapter: ArrayAdapter<DaftarKota>? = null
    var progressDialog: ProgressDialog? = null

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view!!.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    companion object {
        @JvmStatic
        fun newInstance(string: String?): FragmentJadwalSholat {
            val f = FragmentJadwalSholat()
            val args = Bundle()
            args.putString("detail", string)
            f.arguments = args
            return f
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mString = arguments!!.getString("detail")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_jadwal_sholat, container, false)
        progressDialog = ProgressDialog(activity)
        progressDialog!!.setTitle("Mohon di tunggu ya")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Sedang menampilkan data...")

        val spKota: Spinner = v.findViewById(R.id.spinKota)
        listDaftarKota = ArrayList()
        mDaftarKotaAdapter = ArrayAdapter(getActivity()!!.getApplicationContext(),
                android.R.layout.simple_spinner_item,
                listDaftarKota as ArrayList<DaftarKota>)
        mDaftarKotaAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spKota.adapter = mDaftarKotaAdapter
        spKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val spinKota = mDaftarKotaAdapter!!.getItem(position)
                loadJadwal(spinKota!!.id)
            }
        }

        val datePickerTimeline: DatePickerTimeline = v.findViewById(R.id.dateTimeline)
        val date = Calendar.getInstance()
        val mYear: Int = date.get(Calendar.YEAR)
        val mMonth: Int = date.get(Calendar.MONTH)
        val mDay: Int = date.get(Calendar.DAY_OF_MONTH)

        datePickerTimeline.setInitialDate(mYear, mMonth, mDay)
        datePickerTimeline.setDisabledDateColor(ContextCompat.getColor(activity!!, R.color.colorAccent))
        datePickerTimeline.setActiveDate(date)

        val dates = arrayOf(Calendar.getInstance().time)
        datePickerTimeline.deactivateDates(dates)

        loadKota()

        return v
    }

    private fun loadJadwal(id: Int?) {
        try {
            progressDialog!!.show()
            val idKota = id.toString()
            val current = SimpleDateFormat("yyyy-MM-dd")
            val tanggal = current.format(Date())
            val url = "https://api.banghasan.com/sholat/format/json/jadwal/kota/$idKota/tanggal/$tanggal"
            val task = ClientAsyncTask(this, object : ClientAsyncTask.OnPostExecuteListener {
                override fun onPostExecute(result: String) {
                    try {
                        progressDialog!!.dismiss()
                        val jsonObj = JSONObject(result)
                        val objJadwal = jsonObj.getJSONObject("jadwal")
                        val obData = objJadwal.getJSONObject("data")

                        tv_subuh.text = obData.getString("subuh")
                        tv_dzuhur.text = obData.getString("dzuhur")
                        tv_ashar.text = obData.getString("ashar")
                        tv_maghrib.text = obData.getString("maghrib")
                        tv_isya.text = obData.getString("isya")

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            })
            task.execute(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun loadKota() {
        try {
            progressDialog!!.show()
            val url = "https://api.banghasan.com/sholat/format/json/kota"
            val task = ClientAsyncTask(this, object : ClientAsyncTask.OnPostExecuteListener {
                override fun onPostExecute(result: String) {
                    try {
                        progressDialog!!.dismiss()
                        val jsonObj = JSONObject(result)
                        val jsonArray = jsonObj.getJSONArray("kota")
                        var daftarKota: DaftarKota?
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            daftarKota = DaftarKota()
                            daftarKota.id = obj.getInt("id")
                            daftarKota.nama = obj.getString("nama")
                            listDaftarKota!!.add(daftarKota)
                        }
                        mDaftarKotaAdapter!!.notifyDataSetChanged()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            })
            task.execute(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}