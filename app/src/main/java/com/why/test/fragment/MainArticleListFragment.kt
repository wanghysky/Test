package com.why.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.why.lib_base.base.BaseFragment
import com.why.lib_base.kotterknife.bindView
import com.why.test.R
import com.why.test.data.bean.ArticleList
import com.why.test.view.MainArticleListAdapter
import com.why.test.vm.MainArticleListViewModel

/**
 *
 * @author why
 * @date 2022/11/14 18:08
 */
class MainArticleListFragment : BaseFragment()  {


    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainArticleListViewModel::class.java)
    }

    private val recyclerView by bindView<RecyclerView>(R.id.rv_list)


    private val adapter: MainArticleListAdapter by lazy {
        MainArticleListAdapter(arrayListOf())
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_article_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.getString(MAIN_TYPE, "") ?: ""
        val list = viewModel.getArticleList(type)
        initRecycler(list)
    }

    private fun initRecycler(list: MutableList<ArticleList>) {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        recyclerView.itemAnimator = itemAnimator
        recyclerView.layoutManager = layoutManager
        adapter.list = list
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val MAIN_TYPE = "main_type"
    }


}