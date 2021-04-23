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

package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// TODO (01) Create an abstract class that extends RoomDatabase. //DONE
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase(){

    // TODO (02) Declare an abstract value of type SleepDatabaseDao. //DONE
    abstract val sleepDatabaseDao: SleepDatabaseDao

    // TODO (03) Declare a companion object. //DONE
    companion object {

        // TODO (04) Declare a @Volatile INSTANCE variable. //DONE
        /*
        * Volatile prevents the variable from being cached; any writing or reading will be done from
        * the main memory. So, the value is always up to date and the same across all threads.
        * If a thread changes the value, it is instantly visible to all other threads.
        */
        @Volatile
        private var INSTANCE: SleepDatabase? = null

        // TODO (05) Define a getInstance() method with a synchronized block. //DONE
        fun getInstance(context: Context):SleepDatabase{
            /*
            * With synchronized we make sure that only one thread at a time will have access to the
            * getInstance() , which makes sure that the database will get initialized only once.
            */
            synchronized(this){
                var instance = INSTANCE

                // TODO (06) Inside the synchronized block: //DONE
                // Check whether the database already exists,
                // and if it does not, use Room.databaseBuilder to create it.
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                            SleepDatabase::class.java,
                            "sleep_history_database")
                            // This handles what to do in case of updating the app (either by also
                            // updating/changing the schema or not. In our case we just destroy the
                            // old tables and create them all over (therefore,  data is lost)
                            // link in Kotlin Nanodegree folder explains all different cases
                            .fallbackToDestructiveMigration()
                            .build()
                }
                return instance
            }
        }
    }

}










