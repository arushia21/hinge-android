package com.example.peoplecounter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

/**
 * ViewModel for the People Counter app
 * 
 * What is a ViewModel?
 * - The "brain" of your app that holds data and logic
 * - Survives screen rotations and configuration changes
 * - Uses LiveData to notify the UI when data changes
 * 
 * Why use LiveData?
 * - Automatically updates the UI when data changes
 * - Lifecycle-aware (only updates when the screen is visible)
 * - Prevents memory leaks
 */
class PeopleCounterViewModel : ViewModel() {
    
    // Private mutable variables (only this ViewModel can change them)
    private val _currentCount = MutableLiveData(0)
    private val _totalCount = MutableLiveData(0)
    
    // Public read-only variables (the UI can observe these)
    val currentCount: LiveData<Int> = _currentCount
    val totalCount: LiveData<Int> = _totalCount
    
    /**
     * Increments both current and total count
     * Called when user presses the "+" button
     */
    fun incrementCount() {
        val current = _currentCount.value ?: 0
        val total = _totalCount.value ?: 0
        
        _currentCount.value = current + 1
        _totalCount.value = total + 1
    }
    
    /**
     * Decrements only the current count (not total)
     * Called when user presses the "-" button
     * Only works if current count is greater than 0
     */
    fun decrementCount() {
        val current = _currentCount.value ?: 0
        if (current > 0) {
            _currentCount.value = current - 1
        }
    }
    
    /**
     * Resets both counters to zero
     * Called when user presses the "RESET" button
     */
    fun resetCounts() {
        _currentCount.value = 0
        _totalCount.value = 0
    }
    
    /**
     * Helper function to check if current count is over capacity (15 people)
     * Used to determine if text should be red
     */
    fun isOverCapacity(): Boolean {
        return (_currentCount.value ?: 0) > 15
    }
    
    /**
     * Helper function to check if minus button should be visible
     * Minus button is hidden when current count is 0
     */
    fun shouldShowMinusButton(): Boolean {
        return (_currentCount.value ?: 0) > 0
    }
}