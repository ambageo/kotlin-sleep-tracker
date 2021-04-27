/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.launch

//TODO (03) Using the code in SleepTrackerViewModel for reference, create SleepQualityViewModel //DONE
//with coroutine setup and navigation setup.
class SleepQualityViewModel(private val sleepNightKey : Long =0L, val database: SleepDatabaseDao) : ViewModel(){

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker : LiveData<Boolean?>
    get() = _navigateToSleepTracker

    fun doneNavigating(){
        _navigateToSleepTracker.value = null
    }

    //TODO (04) implement the onSetSleepQuality() click handler using coroutines. //DONE
    fun onSetSleepQuality(quality: Int){
        viewModelScope.launch {
            val night = database.get(sleepNightKey) ?: return@launch
            night.sleepQuality = quality
            database.update(night)

            // Set the state variable to true to alert the observer and trigger the navigation
            _navigateToSleepTracker.value = true
        }
    }
}

