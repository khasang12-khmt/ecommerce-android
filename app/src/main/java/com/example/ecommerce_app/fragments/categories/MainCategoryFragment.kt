package com.example.ecommerce_app.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_app.adapters.BestDealsAdapter
import com.example.ecommerce_app.adapters.BestProductsAdapter
import com.example.ecommerce_app.adapters.SpecialProductAdapter
import com.example.ecommerce_app.databinding.FragmentMainCategoryBinding
import com.example.ecommerce_app.utils.Resource
import com.example.ecommerce_app.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private val TAG = "MainCategoryFragment"

@AndroidEntryPoint
class MainCategoryFragment: Fragment() {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductAdapter: SpecialProductAdapter
    private lateinit var bestProductsAdapter: BestProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout? {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRv()
        setupBestDealsRv()
        setupBestProductsRv()
        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        specialProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.msg.toString())
                        Toast.makeText(requireContext(),it.msg,Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }

            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.msg.toString())
                        Toast.makeText(requireContext(),it.msg,Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }

            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.bestDeals.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.msg.toString())
                        Toast.makeText(requireContext(),it.msg,Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }

            }
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDeals.apply{
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }

    private fun setupBestProductsRv() {
        bestProductsAdapter = BestProductsAdapter()
        binding.rvBestProducts.apply{
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun hideLoading() {
        binding.mainProgressbar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainProgressbar.visibility = View.VISIBLE
    }

    private fun setupSpecialProductRv() {
        specialProductAdapter = SpecialProductAdapter()
        binding.rvSpecialProducts.apply{
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductAdapter
        }
    }
}