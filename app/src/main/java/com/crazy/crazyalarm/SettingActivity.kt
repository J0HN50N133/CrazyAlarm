package com.crazy.crazyalarm

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil
import com.crazy.crazyalarm.clockUtils.Configuration
import com.crazy.crazyalarm.databinding.ActivitySettingBinding
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions


class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    val CAMERA_REQ_CODE = 111
    val DECODE = 1
    private val REQUEST_CODE_SCAN_ONE = 0X01
    val SETTING = "setting"
    val MATH_CODE = "mathCode"
    val SCAN_STRING = "scanString"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                var modeCode = when (pos) {
                    0 -> 11
                    1 -> 12
                    2 -> 0
                    3 -> 1
                    4 -> 2
                    5 -> 3
                    6 -> 6
                    7 -> 8
                    8 -> 31
                    9 -> 5
                    10 -> 15
                    11 -> 14
                    else -> 0
                }
                Configuration.MathConf.modeCode = modeCode
                val editor = getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit()
                editor.putInt(MATH_CODE, modeCode)
                editor.apply()

//                val prefs =  getSharedPreferences(SETTING, MODE_PRIVATE)
//                val code = prefs.getInt(MATH_CODE,123)
//                    Toast.makeText(this@SettingActivity,code.toString(),Toast.LENGTH_SHORT).show()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val modeCode = 0
                Configuration.MathConf.modeCode = modeCode
                val editor = getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit()
                editor.putInt(MATH_CODE, modeCode)
                editor.apply()
            }
        }
    }

    //Activity回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK || data == null) {
            return
        }
        if (requestCode == REQUEST_CODE_SCAN_ONE) {
            val obj: HmsScan? = data.getParcelableExtra(ScanUtil.RESULT)
            obj?.let{
                Toast.makeText(this, "设置二维码成功！", Toast.LENGTH_SHORT).show()
                Configuration.ScanConf.scanString = it.originalValue
                getSharedPreferences(SETTING, MODE_PRIVATE).edit().run {
                    putString(SCAN_STRING, it.originalValue)
                    apply()
                }
//                getSharedPreferences(SETTING, MODE_PRIVATE).run {
//                    Toast.makeText(this@SettingActivity,getString(SCAN_STRING,""),Toast.LENGTH_SHORT).show()
//                }
            }



        }
    }

    //权限请求
    fun loadScanKitBtnClick(view: View) {
        requestPermission(CAMERA_REQ_CODE, DECODE)
    }

    //编辑请求权限
    private fun requestPermission(requestCode: Int, mode: Int) {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            requestCode
        )
    }

    //权限申请返回
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (permissions == null || grantResults == null) {
//            return
//        }
        if (grantResults.size < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return
        }
        if (requestCode == CAMERA_REQ_CODE) {
            //启动扫描Acticity
            ScanUtil.startScan(
                this,
                REQUEST_CODE_SCAN_ONE,
                HmsScanAnalyzerOptions.Creator().create()
            )
        }
    }


}