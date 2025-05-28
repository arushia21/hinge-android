package com.example.peoplecounter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.peoplecounter.model.PeopleCounterRepository

/**
 * ViewModel for the People Counter app - Updated with Data Persistence
 * 
 * What changed?
 * - Now extends AndroidViewModel instead of ViewModel (so we can access Context)
 * - Uses PeopleCounterRepository to save and load data
 * - Automatically loads saved data when created
 * - Automatically saves data whenever it changes
 * 
 * Why AndroidViewModel?
 * - AndroidViewModel gives us access to the Application context
 * - We need context to create SharedPreferences
 * - Application context is safe to use (won't cause memory leaks)
 */
class PeopleCounterViewModel(application: Application) : AndroidViewModel(application) {
    
    // Create our repository (the data storage handler)
    private val repository = PeopleCounterRepository(application.applicationContext)
    
    // Private mutable variables (only this ViewModel can change them)
    private val _currentCount = MutableLiveData<Int>()
    private val _totalCount = MutableLiveData<Int>()
    
    // Public read-only variables (the UI can observe these)
    val currentCount: LiveData<Int> = _currentCount
    val totalCount: LiveData<Int> = _totalCount
    
    /**
     * Initialize the ViewModel
     * This runs automatically when the ViewModel is created
     */
    init {
        loadSavedData()
    }
    
    /**
     * Load saved data from persistent storage
     * This runs when the app starts up
     */
    private fun loadSavedData() {
        // Check if this is the first time the app is running
        if (!repository.hasSavedData()) {
            // First time launch - start with 0,0
            _currentCount.value = 0
            _totalCount.value = 0
            println("First launch - Starting with 0,0")
        } else {
            // Load existing saved data
            val savedCounts = repository.getBothCounts()
            _currentCount.value = savedCounts.first  // savedCounts.first is the current count
            _totalCount.value = savedCounts.second   // savedCounts.second is the total count
            println("Loaded saved data - Current: ${savedCounts.first}, Total: ${savedCounts.second}")
        }
    }
    
    /**
     * Increments both current and total count
     * Called when user presses the "+" button
     * 
     * What's new: Now saves data after updating
     */
    fun incrementCount() {
        val current = _currentCount.value ?: 0
        val total = _totalCount.value ?: 0
        
        // Update the values
        val newCurrent = current + 1
        val newTotal = total + 1
        
        _currentCount.value = newCurrent
        _totalCount.value = newTotal
        
        // Save the new values to persistent storage
        repository.saveBothCounts(newCurrent, newTotal)
        
        // Optional: Log for debugging
        println("Incremented - Current: $newCurrent, Total: $newTotal")
    }
    
    /**
     * Decrements only the current count (not total)
     * Called when user presses the "-" button
     * Only works if current count is greater than 0
     * 
     * What's new: Now saves data after updating
     */
    fun decrementCount() {
        val current = _currentCount.value ?: 0
        if (current > 0) {
            val newCurrent = current - 1
            _currentCount.value = newCurrent
            
            // Save only the current count (total stays the same)
            repository.saveCurrentCount(newCurrent)
            
            // Optional: Log for debugging
            println("Decremented - Current: $newCurrent")
        }
    }
    
    /**
     * Resets both counters to zero
     * Called when user presses the "RESET" button
     * 
     * What's new: Now saves data after updating
     */
    fun resetCounts() {
        _currentCount.value = 0
        _totalCount.value = 0
        
        // Save the reset values
        repository.saveBothCounts(0, 0)
        
        // Optional: Log for debugging
        println("Reset both counts to 0")
    }
    
    /**
     * Helper function to check if current count is over capacity (15 people)
     * Used to determine if text should be red
     * 
     * No changes needed here
     */
    fun isOverCapacity(): Boolean {
        return (_currentCount.value ?: 0) > 15
    }
    
    /**
     * Helper function to check if minus button should be visible
     * Minus button is hidden when current count is 0
     * 
     * No changes needed here
     */
    fun shouldShowMinusButton(): Boolean {
        return (_currentCount.value ?: 0) > 0
    }
    
    /**
     * Optional: Method to clear all saved data
     * Useful for testing or if you want to add a "Clear All Data" feature
     */
    fun clearAllSavedData() {
        repository.clearAllData()
        _currentCount.value = 0
        _totalCount.value = 0
        println("Cleared all saved data")
    }
    
    /**
     * Optional: Check if we have saved data
     * Could be useful to show a "Welcome back!" message or tutorial for new users
     */
    fun hasSavedData(): Boolean {
        return repository.hasSavedData()
    }
}