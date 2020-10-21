package com.konkuk.select.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.konkuk.select.R
import com.konkuk.select.network.Fbase
import kotlinx.android.synthetic.main.fragment_closet.*

private const val BOTTOMSHEET_CLOSETLIST_REQUEST_CODE = 1
private const val CLOSET_ID_MESSAGE = "closetId"
private const val CLOSET_TITLE_MESSAGE = "closetTitle"
// Params
private const val CLOSET_ID_PARAM = "closetId"
private const val CLOSET_NAME_PARAM = "closetName"

class ClosetFragment : Fragment() {

    private var closetId: String = ""
    private var closetName: String = ""

    companion object {
        @JvmStatic
        fun newInstance(closetId: String, closetName: String) =
            ClosetFragment().apply {
                arguments = Bundle().apply {
                    putString(CLOSET_ID_PARAM, closetId)
                    putString(CLOSET_NAME_PARAM, closetName)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it ->
            closetId = it.getString(CLOSET_ID_PARAM).toString()
            closetName = it.getString(CLOSET_NAME_PARAM).toString() // 옷장 리스트에서 넘어온 경우
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_closet, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_closet_name.text = if (closetName == "") "전체 옷장" else closetName
        settingFragment(closetId, Fbase.uid.toString()) //이걸 데이터 onresume?때하면 되지않나
        settingOnClickListener()
    }

    private fun settingFragment(closetId: String, uid: String) {
        fragmentManager?.let {
            it.beginTransaction()
                .replace(
                    R.id.closet_container,
                    ClothesListFragment.newInstance(closetId, uid)
                )
                .commit()
        }
    }

    private fun settingOnClickListener() {
        tv_closet_name.setOnClickListener {
            // 아래에서 셀렉리스트 나오기
            Toast.makeText(activity, "클릭!!", Toast.LENGTH_SHORT).show()
            showBottomSheetDialogFragment()
        }
    }

    private fun showBottomSheetDialogFragment() {
        val bottomSheetFragment = BottomSheetClosetListDialog()
        fragmentManager?.let {
            bottomSheetFragment.setTargetFragment(this, BOTTOMSHEET_CLOSETLIST_REQUEST_CODE)
            bottomSheetFragment.show(it, bottomSheetFragment.tag)
        }
    }

    fun passClosetData(id: String, title: String): Intent {
        val intent = Intent()
        intent.putExtra(CLOSET_ID_MESSAGE, id)
        intent.putExtra(CLOSET_TITLE_MESSAGE, title)
        return intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode !== Activity.RESULT_OK) {
            return
        }
        if (requestCode === BOTTOMSHEET_CLOSETLIST_REQUEST_CODE) {
            Log.d("closetTitle", BOTTOMSHEET_CLOSETLIST_REQUEST_CODE.toString())
            if (data != null) {
                data.getStringExtra(CLOSET_ID_MESSAGE)?.let {
                    closetId = it
                    Log.d("closetTitle", it)
                }
                data.getStringExtra(CLOSET_TITLE_MESSAGE)?.let {
                    closetName = it
                    Log.d("closetTitle", it)
                }
            }
        }
    }
}
