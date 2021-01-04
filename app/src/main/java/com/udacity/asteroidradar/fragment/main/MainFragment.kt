package com.udacity.asteroidradar.fragment.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.constant.DataBaseFetchType
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity).application
        val viewModelFactory = ViewModelFactory(activity)
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val adapter = AsteroidAdapter(
            AsteroidAdapter.AsteroidOnClickListener {
                    asteroid ->  viewModel.setAsteroidetails(asteroid)
            }
        )
        binding.asteroidRecycler.adapter = adapter
        viewModel.asteroid.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.statusLoadingWheel.visibility=View.VISIBLE
            }else{
                binding.statusLoadingWheel.visibility=View.GONE
            }
        })
        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner, Observer {
            it?.let{
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.shownAsteroidDetail()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_menu -> viewModel.onFilterSelect(DataBaseFetchType.ALL)
            R.id.show_buy_menu -> viewModel.onFilterSelect(DataBaseFetchType.TODAY)
            R.id.show_rent_menu -> viewModel.onFilterSelect(DataBaseFetchType.WEEKLY)
        }
        return true
    }
}
