package com.example.myunlimitedpage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myunlimitedpage.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var page = 1
    private var isLoading = false
    private var totalPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)
        binding.swipeRefresh.setOnRefreshListener(this)
        setupRecyclerView()
        getUsers(false)
        binding.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = adapter.itemCount
                if (!isLoading && page < totalPage) {
                    if (visibleItemCount + pastVisibleItem >= total) {
                        page++
                        getUsers(false)
                    }
                }
            }
        })
    }

    private fun getUsers(isOnRefresh: Boolean) {
        isLoading = true
        if (!isOnRefresh) binding.progressBar.visibility = View.VISIBLE
        val parameters = HashMap<String, String>()
        parameters["page"] = page.toString()
        val client = ApiConfig.getApiService().getUsers(parameters)
        client.enqueue(object : Callback<UsersResponse> {
            override fun onResponse(
                call: Call<UsersResponse>,
                response: Response<UsersResponse>
            ) {
                totalPage = response.body()?.totalPages!!
                val listResponse = response.body()?.data
                if (listResponse != null) {
                    adapter.addList(listResponse)
                }
                binding.progressBar.visibility = View.INVISIBLE
                isLoading = false
                binding.swipeRefresh.isRefreshing = false
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView() {
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.layoutManager = layoutManager
        adapter = UserAdapter()
        binding.rvUsers.adapter = adapter
    }

    override fun onRefresh() {
        adapter.clear()
        page = 1
        getUsers(true)
    }
}