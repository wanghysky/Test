package com.why.test

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.why.lib_base.base.BaseActivity
import com.why.lib_base.kotterknife.bindOptionalView
import com.why.lib_base.kotterknife.bindView
import com.why.test.vm.MainViewModel

class MainActivity : BaseActivity<MainViewModel>() {

    private val tvMain by bindOptionalView<TextView>(R.id.tv_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvMain?.setOnClickListener {
            viewModel.getArticlePageList()
        }
    }
}