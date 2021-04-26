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

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    //TODO (03) Create a MutableLiveData variable tonight for one SleepNight.
    private var tonight = MutableLiveData<SleepNight?>()

    //TODO (04) Define a variable, nights. Then getAllNights() from the database //DONE
    //and assign to the nights variable.
    private val nights = database.getAllNights()

    //TODO (12) Transform nights into a nightsString using formatNights(). //DONE
    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    //TODO (05) In an init block, initializeTonight(), and implement it to launch a coroutine //DONE
    //to getTonightFromDatabase().
    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        // viewModelScope is the scope in which the coroutine will be launched
       viewModelScope.launch {
           tonight.value = getTonightFromDatabase()
       }
    }

    //TODO (06) Implement getTonightFromDatabase()as a suspend function. //DONE

    // This is the actual coroutine
    private suspend fun getTonightFromDatabase(): SleepNight? {
            var night = database.getTonight()

        if(night?.endTimeMilli != night?.startTimeMilli) {
            night = null
        }
        return night
    }



    //TODO (07) Implement the click handler for the Start button, onStartTracking(), using //DONE
    //coroutines. Define the suspend function insert(), to insert a new night into the database.
    fun onStartTracking(){
        viewModelScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            val newNight = SleepNight()
            insert(newNight)
            // Here we probably update the LiveData var tonight with the latest entry in the database
            // because we just added a new night
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(newNight: SleepNight) {
        database.insert(newNight)
    }

    //TODO (08) Create onStopTracking() for the Stop button with an update() suspend function. //DONE
    fun onStopTracking(){
        viewModelScope.launch {
            // In Kotlin, the return@label syntax is used for specifying which function among
            // several nested ones this statement returns from.
            // In this case, we are specifying to return from launch(),
            // not the lambda.
            val oldNight= tonight.value ?: return@launch
            // oldNight becomes the tonight, we update the endTime with the current time and then
            // we update the actual SleepNight in the database
            oldNight.endTimeMilli = System.currentTimeMillis()
            // update the night
            update(oldNight)
        }
    }

    private suspend fun update(oldNight: SleepNight) {
        database.update(oldNight)
    }

    //TODO (09) For the Clear button, created onClear() with a clear() suspend function. //DONE
    fun onClear(){
        viewModelScope.launch {
            clear()
            tonight.value = null
        }
    }

    private suspend fun clear(){
        database.clear()
    }

}

