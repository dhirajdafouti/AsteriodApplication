package com.udacity.asteroidradar.fragment.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.constant.DataBaseFetchType
import com.udacity.asteroidradar.database.getDataBase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber


class MainViewModel(private val application: Application) : ViewModel() {
    //Live data for logging ..
    private val _networkStatus: MutableLiveData<String> = MutableLiveData<String>()
    val networkStatus: LiveData<String> get() = _networkStatus

    private val _navigateToDetailFragment = MutableLiveData<Asteroid>()
    val navigateToDetailFragment: LiveData<Asteroid>
        get() = _navigateToDetailFragment

    private val database = getDataBase(application)
    private val asteroidsRepository = AsteroidRepository(database)

    private val _progresssBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBar: LiveData<Boolean> get() = _progresssBar


    init {
        viewModelScope.launch {
            refreshNetworkAsteroidData()
            refreshPictureOfTheDay()
        }
    }

    val asteroid = asteroidsRepository.asteroids
    val imageOfTheDay = asteroidsRepository.pictureOfDay
    val loadingStatus = progressBar

    fun shownAsteroidDetail() {
        _navigateToDetailFragment.value = null
    }

    fun setAsteroidetails(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun onFilterSelect(filter: DataBaseFetchType) {
        asteroidsRepository.applyDatBaseFilter(filter)
    }

    private fun refreshPictureOfTheDay() {
        viewModelScope.launch {
            try {
                _networkStatus.value = "Started"
                asteroidsRepository.syncPictureOfTheDay()
            } catch (e: Exception) {
                _networkStatus.value = "Failure: ${e.message}"
                Timber.e(TAG,_networkStatus.value)
            } finally {
                _networkStatus.value = "DONE"
            }

        }
    }

    private fun refreshNetworkAsteroidData() {
        _progresssBar.value = true
        viewModelScope.launch {
            try {
                _networkStatus.value = "Started"
                asteroidsRepository.syncNetworkAsteroid()
            } catch (e: Exception) {
                _networkStatus.value = "Failure: ${e.message}"
                Timber.e(TAG,_networkStatus.value)
            } finally {
                _progresssBar.value = false
                _progresssBar.postValue(false)
                _networkStatus.value = "DONE"
            }

        }
    }
    companion object{
        private val TAG:String= MainViewModel::class.java.simpleName
    }
}