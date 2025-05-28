package com.example.peoplecounter.model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Repository class for People Counter app
 * 
 * What is a Repository?
 * - The "data layer" in MVVM architecture
 * - Handles all data storage and retrieval (files, databases, network, etc.)
 * - Provides a clean interface for the ViewModel to get/save data
 * - Hides the implementation details (ViewModel doesn't need to know about SharedPreferences)
 * 
 * Why use SharedPreferences?
 * - Perfect for storing small pieces of data (like numbers, strings, booleans)
 * - Data persists even when app is killed or device is restarted
 * - Built into Android, no extra dependencies needed
 * - Fast and simple to use
 */
class PeopleCounterRepository(context: Context) {
    
    // SharedPreferences object - this is our "file" where data is stored
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, // The name of our preferences file
        Context.MODE_PRIVATE // Only our app can access this file
    )
    
    // Constants for the keys we use to store data
    // Think of these like labels on boxes where we store our numbers
    companion object {
        private const val PREFS_NAME = "people_counter_prefs" // Name of our storage file
        private const val KEY_CURRENT_COUNT = "current_count" // Label for current count box
        private const val KEY_TOTAL_COUNT = "total_count"     // Label for total count box
    }
    
    /**
     * Save the current count to persistent storage
     * 
     * How it works:
     * 1. Get an "editor" (like opening the file for writing)
     * 2. Put the value with its key (like putting a number in a labeled box)
     * 3. Apply the changes (like saving and closing the file)
     */
    fun saveCurrentCount(count: Int) {
        sharedPreferences.edit {
            putInt(KEY_CURRENT_COUNT, count)
        } // apply() saves asynchronously (in the background)
    }
    
    /**
     * Save the total count to persistent storage
     * Same process as saveCurrentCount, but for the total
     */
//    fun saveTotalCount(count: Int) {
//        sharedPreferences.edit {
//            putInt(KEY_TOTAL_COUNT, count)
//        }
//    }
    
    /**
     * Save both counts at the same time (more efficient)
     * 
     * Why this is better than calling saveCurrentCount() and saveTotalCount() separately:
     * - Only one file write operation instead of two
     * - Atomic operation (both values are saved together or not at all)
     */
    fun saveBothCounts(currentCount: Int, totalCount: Int) {
        sharedPreferences.edit {
            putInt(KEY_CURRENT_COUNT, currentCount)
                .putInt(KEY_TOTAL_COUNT, totalCount)
        }
    }
    
    /**
     * Load the current count from persistent storage
     * 
     * How it works:
     * 1. Ask SharedPreferences for the value with the given key
     * 2. If no value exists (first time running app), return the default value (0)
     * 
     * @return The saved current count, or 0 if no saved value exists
     */
    private fun getCurrentCount(): Int {
        return sharedPreferences.getInt(KEY_CURRENT_COUNT, 0) // 0 is the default value
    }
    
    /**
     * Load the total count from persistent storage
     * Same process as getCurrentCount, but for the total
     */
    private fun getTotalCount(): Int {
        return sharedPreferences.getInt(KEY_TOTAL_COUNT, 0) // 0 is the default value
    }
    
    /**
     * Load both counts at the same time
     * Returns a Pair (like a box with two compartments)
     * 
     * @return Pair<currentCount, totalCount>
     */
    fun getBothCounts(): Pair<Int, Int> {
        val currentCount = getCurrentCount()
        val totalCount = getTotalCount()
        return Pair(currentCount, totalCount)
    }
    
    /**
     * Clear all saved data (useful for testing or reset functionality)
     * This removes all data from our SharedPreferences file
     */
    fun clearAllData() {
        sharedPreferences.edit {
            clear() // Remove everything
        }
    }
    
    /**
     * Check if we have any saved data
     * Useful to know if this is the first time the app is running
     * 
     * @return true if we have saved data, false if this is a fresh start
     */
    fun hasSavedData(): Boolean {
        return sharedPreferences.contains(KEY_CURRENT_COUNT) || 
               sharedPreferences.contains(KEY_TOTAL_COUNT)
    }
}
