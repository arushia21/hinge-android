package com.example.peoplecounter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.peoplecounter.model.PeopleCounterRepository

/**
 * ViewModel, uses Data Persistance
 *
 * goal: manage UI related data, persists through config changes
 * adds functionality, bridges UI and model
 *
 * Note: I'm using AndroidViewModel instead of regular ViewModel
 * allows me to use application context, for data persistence (so it works through app sessions)
 */
class PeopleCounterViewModel(application: Application) : AndroidViewModel(application) {
    
    // creates our repository - the MODEL
    // repository handles all the SharedPreferences details
    private val repo = PeopleCounterRepository(application.applicationContext)

    // private variables for the view model
    // keeping track of the curr and total count of people
    private val _currCount = MutableLiveData<Int>()
    private val _totalCount = MutableLiveData<Int>()
    
    // public variables: encapsulation
    // view can view these variables
    val currentCount: LiveData<Int> = _currCount
    val totalCount: LiveData<Int> = _totalCount
    
    /**
     * ViewModel initialized
     */
    init {
        // data persistence - loading saved data from previous app sessions
        loadSavedData()
    }
    
    /**
     *
     * load saved data from persistent storage
     * if the repo has no saved data, starts BOTH COUNTS with 0,0
     */
    private fun loadSavedData() {
        // check if repo has any saved data with SharedPreferences
        if (!repo.hasSavedData()) {
            // first time launch - start with 0,0
            // sets private variables here
            _currCount.value = 0
            _totalCount.value = 0
            // println("First launch - Starting with 0,0") // debugging here
        } else {
            // load existing saved data from previous sessions
            // returns Pair<current, total>
            val savedCounts = repo.getBothCounts()
            _currCount.value = savedCounts.first  // current count
            _totalCount.value = savedCounts.second   // total count
            // println("Loaded saved data - Current: ${savedCounts.first}, Total: ${savedCounts.second}")
        }
        // livedata updates immediately with the loaded data
    }
    
    /**
     * Logic to increment both current and total count: + button
     */
    fun incrementCount() {
        // get current values, default to 0 if null
        val current = _currCount.value ?: 0
        val total = _totalCount.value ?: 0
        
        // both go up by 1 when someone enters
        val newCurrent = current + 1
        val newTotal = total + 1
        
        // livedata: automatically updates ui
        _currCount.value = newCurrent
        _totalCount.value = newTotal
        
        // saves the values to persistent storage IMMEDIATELY, in case of crash/closing
        repo.saveBothCounts(newCurrent, newTotal)
        
        // println("Incremented - Current: $newCurrent, Total: $newTotal") //debugging
    }
    
    /**
     * Logic: decrementing current count only: - button
     * only works if current count is greater than 0
     */
    fun decrementCount() {
        // current value, default to 0 if null
        val current = _currCount.value ?: 0
        
        // can only have >= 0 people in store
        if (current > 0) {
            val newCurrent = current - 1
            
            // update current count only
            _currCount.value = newCurrent
            
            // save only the current count
            repo.saveCurrentCount(newCurrent)
            // println("Decremented - Current: $newCurrent") //debugging
        }
    }
    
    /**
     * Resent functionality
     */
    fun resetCounts() {
        // set both counts back to 0
        _currCount.value = 0
        _totalCount.value = 0
        
        // save the reset values to storage - persistence
        repo.saveBothCounts(0, 0)
        // println("Reset both counts to 0") //debugging
    }
    
    /**
     * Helper: checks if over 15 people for red text in View
     */
    fun isOverCapacity(): Boolean {
        return (_currCount.value ?: 0) > 15
    }
    
    /**
     * Helper: minus button hidden when current count is 0
     */
    fun shouldShowMinusButton(): Boolean {
        // return true if count > 0, false if count = 0
        return (_currCount.value ?: 0) > 0
    }
    
    /**
     * clear Saved Data: testing
     */
    fun clearAllSavedData() {
        // repo wipes all saved data
        repo.clearAllData()
        _currCount.value = 0
        _totalCount.value = 0

        //println("Cleared all saved data")
    }

    /**
     * check if we have saved data?
     */
    fun hasSavedData(): Boolean {
        // delegate to repo to check
        return repo.hasSavedData()
    }
}