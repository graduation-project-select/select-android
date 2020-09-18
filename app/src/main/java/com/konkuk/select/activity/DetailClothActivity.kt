package com.konkuk.select.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.konkuk.select.R
import com.konkuk.select.adpater.CodiItemAdapter
import com.konkuk.select.model.Codi
import kotlinx.android.synthetic.main.activity_detail_cloth.*

class DetailClothActivity : AppCompatActivity() {
    lateinit var codiItemAdapter: CodiItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_cloth)

        // ToolBar 변경하는 코드
        val includeToolBar : View = findViewById(R.id.toolbar)
        val leftBtn : ImageView = includeToolBar.findViewById(R.id.left_iv)
        val title : TextView = includeToolBar.findViewById(R.id.title_tv)
        val rightBtn : ImageView = includeToolBar.findViewById(R.id.right_iv)

        leftBtn.setImageResource(R.drawable.back)
        title.text = "옷 상세보기"
        rightBtn.setImageResource(0)

        var codiList = ArrayList<Codi>()
        codiList.add(Codi("111", "#데이트룩", "0"));
        codiList.add(Codi("111", "#데이트룩", "0"));
        codiList.add(Codi("111", "#데이트룩", "0"));
        codiList.add(Codi("111", "#데이트룩", "0"));
        codiList.add(Codi("111", "#데이트룩", "0"));

        codi_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        codiItemAdapter = CodiItemAdapter(codiList)
        codi_rv.adapter = codiItemAdapter

    }
}