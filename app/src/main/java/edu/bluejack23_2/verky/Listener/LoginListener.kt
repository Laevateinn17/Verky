package edu.bluejack23_2.verky.Listener

interface AuthListener {
    fun OnStarted();
    fun OnSuccess();
    fun OnFailure(message : String);
}