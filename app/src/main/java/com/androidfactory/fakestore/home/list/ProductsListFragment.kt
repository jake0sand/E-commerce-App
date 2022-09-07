package com.androidfactory.fakestore.home.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.androidfactory.fakestore.databinding.FragmentProductsListBinding
import com.androidfactory.fakestore.home.list.composables.ProductCardItem
import com.androidfactory.fakestore.model.ui.UiFilter
import com.androidfactory.fakestore.model.ui.UiProduct
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class ProductsListFragment : Fragment() {

    private var _binding: FragmentProductsListBinding? = null
    private val binding by lazy { _binding!! }

    private val viewModel: ProductsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = UiProductEpoxyController(viewModel)
        binding.epoxyRecyclerView.setController(controller)

        binding.epoxyRecyclerView.visibility = View.GONE
        binding.composeView.setContent {

            Column {
                LazyColumn {
                    items(20) {
                        ProductCardItem(
                            productTitle = "Fjallraven blah blah blah",
                            productCategory = "Men's clothing",
                            image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        combine(
            viewModel.store.stateFlow.map { it.products },
            viewModel.store.stateFlow.map { it.favoriteProductIds },
            viewModel.store.stateFlow.map { it.expandedProductIds },
            viewModel.store.stateFlow.map { it.productFilterInfo },
            viewModel.store.stateFlow.map { it.inCartProductIds }
        ) { listOfProducts, setOfFavoriteIds, setOfExpandedProductIds, productFilterInfo, inCartProductIds ->

            if (listOfProducts.isEmpty()) {
                return@combine ProductsListFragmentUiState.Loading
            }

            val uiProducts = listOfProducts.map { product ->
                UiProduct(
                    product = product,
                    isFavorite = setOfFavoriteIds.contains(product.id),
                    isExpanded = setOfExpandedProductIds.contains(product.id),
                    isInCart = inCartProductIds.contains(product.id)
                )
            }

            val uiFilters = productFilterInfo.filters.map { filter ->
                UiFilter(
                    filter = filter,
                    isSelected = productFilterInfo.selectedFilter?.equals(filter) == true
                )
            }.toSet()

            val filteredProducts = if (productFilterInfo.selectedFilter == null) {
                uiProducts
            } else {
                uiProducts.filter { it.product.category == productFilterInfo.selectedFilter.value }
            }

            return@combine ProductsListFragmentUiState.Success(uiFilters, filteredProducts)

        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) { uiState ->
            controller.setData(uiState)
        }

        viewModel.refreshProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}