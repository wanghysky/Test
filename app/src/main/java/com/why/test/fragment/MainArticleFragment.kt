package com.why.test.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.why.lib_base.base.BaseFragment
import com.why.lib_base.kotterknife.bindView
import com.why.lib_base.util.ListItemDecoration
import com.why.test.R
import com.why.test.data.bean.Tabs
import com.why.test.view.MainBannerAdapter
import com.why.test.vm.MainArticleListViewModel
import kotlin.math.absoluteValue

/**
 *
 * @author why
 * @date 2022/11/14 16:49
 */
class MainArticleFragment : BaseFragment() {

    private val recyclerSpace = 10

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainArticleListViewModel::class.java)
    }

    private val appBarLayout by bindView<AppBarLayout>(R.id.appbar_layout)
    private val banner by bindView<RecyclerView>(R.id.rl_banner)
    private val bannerAdapter: MainBannerAdapter by lazy {
        MainBannerAdapter(arrayListOf())
    }

    private val articleListFragmentAdapter: ArticleListFragmentAdapter by lazy {
        ArticleListFragmentAdapter(this, viewModel.tabList)
    }


    private val tabLayout by bindView<TabLayout>(R.id.tab_layout)
    private val viewPager by bindView<ViewPager2>(R.id.vp_list)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_article_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBanner()
        viewPager.adapter = articleListFragmentAdapter
        TabLayoutMediator(tabLayout, viewPager, getTabConfigurationStrategy()).attach()
        addTabListener()
        initObserver()
        viewModel.getArticlePageList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        remove()
    }

    private fun initBanner() {
        banner.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        banner.addItemDecoration(ListItemDecoration(recyclerSpace))
        banner.adapter = bannerAdapter
    }


    private fun initObserver() {
        viewModel.bannerData.observe(this, "bannerData") {
            bannerAdapter.update(it.toMutableList())
        }
        viewModel.tabsData.observe(this, "tabsData") {
            refreshTabLayout(it.toMutableList())
            notifyListUpdate()
            var position = -1
            it.mapIndexed { index, tabs ->
                if (tabs.id == viewModel.currentTabSelectId) {
                    position = index
                    return@mapIndexed
                }
            }
            if (viewModel.currentTabSelectId > 0 && position > 0) {
                viewModel.tabScrollTo.value = position
            }
            viewModel.currentTabSelectId = -1
        }
        viewModel.tabScrollTo.observe(this, "tabScrollTo") {
            tabLayout.getTabAt(it)?.select()
        }
    }

    private fun notifyListUpdate() {
        articleListFragmentAdapter.notifyDataSetChanged()
    }

    private fun addTabListener() {
        appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener)
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)

    }

    private val onOffsetChangedListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.height == verticalOffset.absoluteValue) {
                context?.let {
                    tabLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            it,
                            R.color.white
                        )
                    )
                }
            } else {
                context?.let {
                    tabLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            it,
                            R.color.transparent
                        )
                    )
                }
            }
        }

    private val tabSelectListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                createSelectTab(tab)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            tab?.let {
                createDefaultTab(tab)
            }
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }


    private fun remove() {
        appBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener)
        tabLayout.removeOnTabSelectedListener(tabSelectListener)
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
    }


    private fun getTabConfigurationStrategy() =
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            createDefaultTab(
                tab,
                viewModel.tabsData.value?.toMutableList()?.get(position)?.name ?: ""
            )
        }


    private fun createSelectTab(
        tab: TabLayout.Tab?
    ) {
        val view = tab?.customView
        if (null == view) {
            tab?.setCustomView(R.layout.main_article_tab_layout_text)
        }
        val textView: TextView = tab?.customView?.findViewById(R.id.tv_text) as TextView
        textView.textSize = 17f
        context?.let { textView.setTextColor(ContextCompat.getColor(it, R.color.main_blue)) }
        textView.typeface = Typeface.DEFAULT_BOLD
    }

    private fun createDefaultTab(
        tab: TabLayout.Tab,
        text: String = ""
    ) {
        var view = tab.customView
        if (null == view) {
            tab.setCustomView(R.layout.main_article_tab_layout_text)
        }
        view = tab.customView
        val textView: TextView = tab.customView?.findViewById(R.id.tv_text) as TextView
        if (text.isNotEmpty()) {
            textView.text = text
        }
        context?.let { textView.setTextColor(ContextCompat.getColor(it, R.color.black_666)) }
        textView.typeface = Typeface.DEFAULT
        textView.textSize = 15f
        view?.setOnClickListener {
            tab.select()
        }
    }


    private fun refreshTabLayout(list: MutableList<Tabs>) {
        if (list.size > 0) {
            tabLayout.removeAllTabs()
            list.mapIndexed { _, tabs ->
                val tab = tabLayout.newTab()
                createDefaultTab(tab, tabs.name)
                tabLayout.addTab(tab)
            }
        }
        tabLayout.removeOnTabSelectedListener(tabSelectListener)
        tabLayout.addOnTabSelectedListener(tabSelectListener)
    }

    private inner class ArticleListFragmentAdapter(
        activity: Fragment,
        val list: MutableList<Tabs>
    ) : FragmentStateAdapter(activity) {

        val fragmentList = arrayListOf<MainArticleListFragment>()

        override fun getItemCount(): Int {
            return list.size
        }

        override fun createFragment(p0: Int): Fragment {
            val fragment = MainArticleListFragment()
            val bundle = Bundle().apply {
                putString(MainArticleListFragment.MAIN_TYPE, list[p0].name)
            }
            fragment.arguments = bundle
            fragmentList.add(fragment)
            return fragment
        }

    }


    companion object{
        const val TAG = "MainArticleFragment"
    }
}