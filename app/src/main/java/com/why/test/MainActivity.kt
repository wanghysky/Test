package com.why.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.why.lib_base.base.BaseActivity
import com.why.lib_base.kotterknife.bindView
import com.why.lib_base.util.StatusBarUtil
import com.why.test.fragment.MainArticleFragment
import com.why.test.vm.MainViewModel

class MainActivity : BaseActivity<MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragment()
    }

    private fun initFragment() {
        val fragment = MainArticleFragment()
        supportFragmentManager.beginTransaction().add(R.id.fl_container, fragment, MainArticleFragment.TAG).commitAllowingStateLoss()
    }

    override fun setFullScreen(): Boolean {
        return true
    }

    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }

}