package com.example.peoplecounter.model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * MODEL class (serves as the data layer)
 * handles data storage and retrieval
 * Using SharedPreferences here, to store primitive data types, data persistence
 */
class PeopleCounterRepository(context: Context) {

    // SharedPreferences object, file to store data
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, // preferences file
        Context.MODE_PRIVATE
    )
    
    // constants for the keys in SharedPreferences
    // name, current count, total count
    companion object {
        private const val PREFS_NAME = "people_counter_prefs"
        private const val KEY_CURRENT_COUNT = "current_count" // current_count box
        private const val KEY_TOTAL_COUNT = "total_count"     // total_count box
    }
    
    /**
     * save the current count ONLY to persistent storage
     * using key value storage, use editor to write to file
     */
    fun saveCurrentCount(count: Int) {
        sharedPreferences.edit {
            putInt(KEY_CURRENT_COUNT, count)
        } // apply() saves asynchronously
    }
    
//    /**
//     * Save the total count to persistent storage
//     * Same process as saveCurrentCount, but for the total
//     */
//    fun saveTotalCount(count: Int) {
//        sharedPreferences.edit {
//            putInt(KEY_TOTAL_COUNT, count)
//        }
//    }
    
    /**
     * save both current and total counts to persistent storage - atomic operation
     * (more efficient)
     */
    fun saveBothCounts(currentCount: Int, totalCount: Int) {
        sharedPreferences.edit {
            putInt(KEY_CURRENT_COUNT, currentCount)
                .putInt(KEY_TOTAL_COUNT, totalCount)
        }
    }

    // loading functions below
    /**
     * load the current count
     * @return The saved current count, or 0 if no saved
     */
    private fun getCurrentCount(): Int {
        // use SharedPreferences for value from key
        return sharedPreferences.getInt(KEY_CURRENT_COUNT, 0) // 0 is the default value
    }
    
    /**
     * load the total count
     */
    private fun getTotalCount(): Int {
        return sharedPreferences.getInt(KEY_TOTAL_COUNT, 0)
    }
    
    /**
     * load both counts
     * Pair<currentCount, totalCount>
     */
    fun getBothCounts(): Pair<Int, Int> {
        val currentCount = getCurrentCount()
        val totalCount = getTotalCount()
        return Pair(currentCount, totalCount)
    }
    /**
     * clear all saved data - reset to 0 & testing
     */
    fun clearAllData() {
        sharedPreferences.edit {
            clear()
        }
    }

    /**
     * check for saved data
     * called in viewmodel to check if we have saved data
     */
    fun hasSavedData(): Boolean {
        return sharedPreferences.contains(KEY_CURRENT_COUNT) ||
                sharedPreferences.contains(KEY_TOTAL_COUNT)
    }
}
