package com.why.test.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.why.lib_base.base.BaseActivity
import com.why.lib_base.ext.initTitle
import com.why.lib_base.kotterknife.bindOptionalView
import com.why.lib_base.kotterknife.bindView
import com.why.lib_base.network.IpManager
import com.why.lib_base.util.StatusBarUtil
import com.why.test.R
import com.why.test.data.bean.ArticleList
import com.why.test.vm.WebViewModel

/**
 *
 * @author why
 * @date 2022/11/14 12:15
 */
class WebActivity : BaseActivity<WebViewModel>() {

    private var agentWeb: AgentWeb ?= null

    private val toolbar by bindOptionalView<Toolbar>(R.id.toolbar)
    private val flWeb by bindView<FrameLayout>(R.id.fl_web)

    private var url: String ?= null
    private var titleText: String ?= null
    private val baseUrl = IpManager.getDefaultIP()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        intent.apply {
            url = baseUrl + getStringExtra(EXTRA_ARTICLE)
        }
        toolbar?.apply {
            setSupportActionBar(this)
            initTitle("加载中...")
        }
        StatusBarUtil.setImmersionStatus(this)
        initView()
    }

    private fun initView() {
        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(flWeb, FrameLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    titleText = title ?: ""
                    toolbar?.title = title
                }
            })
            .createAgentWeb()
            .ready()
            .go(url)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_web, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 注意：item.setIcon(R.drawable.ic_un_collect)这样设置一点击菜单就恢复成onPrepareOptionsMenu的初始状态
        when (item.itemId) {
            R.id.item_refresh -> { // 刷新
                agentWeb?.urlLoader?.reload()
            }
            R.id.item_openByBrowser -> { // 用默认浏览器打开
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    companion object {
        private const val EXTRA_ARTICLE = "extra_article"

        /**
         * 页面跳转
         *
         * @param context Context
         * @param article Article
         */
        fun launch(context: Context, url: String) {
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(EXTRA_ARTICLE, url)
            })
        }
    }

}
