package com.crazy.crazyalarm.closeModeActivity


import android.os.Bundle
import android.widget.Toast
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil
import com.crazy.crazyalarm.clockUtils.BasicRingActivity
import com.crazy.crazyalarm.clockUtils.Configuration
import com.crazy.crazyalarm.databinding.ActivityMathBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.concurrent.thread


class MathActivity : BasicRingActivity() {
    private lateinit var binding: ActivityMathBinding
    private val PROBLEM = "problem"
    private val ANSWER = "solution"
    var ans: String = "A"
    var trueAnswer: String = "A"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMathBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val noticeFlag = intent.getIntExtra(AlarmManagerUtil.NOTICEFLAG, AlarmManagerUtil.BothSoundAndVibrator)
        giveNotice(noticeFlag)
        sendRequestWithOkHttp(Configuration.MathConf.modeCode)
//        binding.sendRequestBtn.setOnClickListener {
//            sendRequestWithOkHttp(Configuration.MathConf.modeCode)
//        }
        binding.Selected.setOnCheckedChangeListener{ group, checkedId ->
            ans = when(checkedId) {
                binding.choose1.id -> "A"
                binding.choose2.id -> "B"
                binding.choose3.id -> "C"
                else -> ""
            }
        }

        binding.checkAnswers.setOnClickListener{
            if(ans == trueAnswer){
                stopNotice(noticeFlag)
                finish()
             //Ture
            }
            else{
                Toast.makeText(this, "回答错误!", Toast.LENGTH_SHORT).show()
                //False
                sendRequestWithOkHttp(Configuration.MathConf.modeCode)
                runOnUiThread {
                    binding.choose1.isChecked = false
                    binding.choose2.isChecked = false
                    binding.choose3.isChecked = false
                }
            }

        }

    }
    private fun sendRequestWithOkHttp(type_id: Int) {
    thread {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://johnsonleeee.xyz:9090/$type_id")
                .build()
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()
            if (responseData != null) {
                val jsonObject = JSONObject(responseData)
                val problem = problemParse(jsonObject.getString(PROBLEM), type_id)
                val answer = jsonObject.getString(ANSWER)
                var arr = setanswer(answer, type_id)
                showMathProblem(problem, arr)
                runOnUiThread{

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
    fun setanswer(answer: String, type_id: Int): MutableList<String>{
        val noise = {num: String-> (num.toFloat()+(-5..5).random().toFloat()).toString() }
        val ans = mutableListOf(answer)
        var random1 = ""
        var random2 = ""
        if (type_id == 11){
            val fractionArr = answer.split("/")
            random1 = noise(fractionArr[0])+"/"+noise(fractionArr[1])
            random2 = noise(fractionArr[0])+"/"+noise(fractionArr[1])
        }else{
            random1 = noise(answer)
            random2 = noise(answer)
            while(random1 == random2){
                var random1 = noise(answer)
                var random2 = noise(answer)
            }
        }
        ans.add(random1)
        ans.add(random2)
        ans.shuffle()
        for(index in 0..2){
            if(answer == ans[index]){
                trueAnswer = when(index){
                    0 -> "A"
                    1 -> "B"
                    2 -> "C"
                    else -> ""
                }
            }
        }
        return ans
    }

    private fun showMathProblem(problem: String, arr: MutableList<String>) {
        runOnUiThread {
            // 在这里进行UI操作，将结果显示到界面上
            val html="\n" +
                 " <html >\n" +
                 "<head>\n" +
                 "    <script type=\"text/x-mathjax-config\">\n" +
                 " MathJax.Hub.Config({\"HTML-CSS\": { preferredFont: \"TeX\", availableFonts: [\"STIX\",\"TeX\"], linebreaks: { automatic:true }, EqnChunk: (MathJax.Hub.Browser.isMobile ? 10 : 50) },\n" +
                 " tex2jax: { inlineMath: [ [\"\$\", \"\$\"], [\"\\\\\\\\(\",\"\\\\\\\\)\"] ], displayMath: [ [\"\$\$\",\"\$\$\"], [\"\\\\[\", \"\\\\]\"] ], processEscapes: true, ignoreClass: \"tex2jax_ignore|dno\" },\n" +
                 " TeX: {\n" +
                 " extensions: [\"begingroup.js\"],\n" +
                 " noUndefined: { attributes: { mathcolor: \"red\", mathbackground: \"#FFEEEE\", mathsize: \"90%\" } },\n" +
                 " Macros: { href: \"{}\" }\n" +
                 " },\n" +
                 " messageStyle: \"none\",\n" +
                 " styles: { \".MathJax_Display, .MathJax_Preview, .MathJax_Preview > *\": { \"background\": \"inherit\" } },\n" +
                 " SEEditor: \"mathjaxEditing\"\n" +
                 " });\n" +
                 " </script>\n" +
                 "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.5/MathJax.js?config=TeX-MML-AM_CHTML\"></script>\n" +
                 "    </head>\n" +
                 " <body >\n" +
                 "    <h3>请选择给出数学题的正确答案，输入正确闹钟停止：</h3>\n" +
                 "<span> $problem</span>\n" +
                 "</body>\n" +
                 "</html>"
            binding.webView.settings.javaScriptEnabled=true
            binding.webView.loadDataWithBaseURL("",html,"text/html","UTF-8","")
            binding.answer1.text= arr[0]
            binding.answer2.text= arr[1]
            binding.answer3.text= arr[2]
        }
    }
    // 把问题字符串进行转换
    private fun problemParse(problem: String?, type_id: Int): String {
        return "\$$problem\$"
    }
    // 对答案进行解析
//    private fun answerParse(answer: String, type_id: Int): String {
//        when(type_id){
//            11->{
//
//            }
//            else->{
//                return answer
//            }
//        }
//    }
}





