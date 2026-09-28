package com.orbit.recovery.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orbit.recovery.data.UserPreferencesRepository
import com.orbit.recovery.data.dataStore
import com.orbit.recovery.data.remote.AnthropicApiService
import com.orbit.recovery.data.remote.AnthropicRequest
import com.orbit.recovery.data.remote.ApiMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ChatMessage(val role: String, val content: String)

class CoachViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserPreferencesRepository(application.dataStore)
    private val apiService = AnthropicApiService.create()
    private val gson = Gson()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val prefs = repository.preferencesFlow.first()
            val savedMessagesJson = prefs[UserPreferencesRepository.COACH_MESSAGES_KEY] ?: "[]"
            val type = object : TypeToken<List<ChatMessage>>() {}.type
            var loadedMessages: List<ChatMessage> = gson.fromJson(savedMessagesJson, type) ?: emptyList()
            
            if (loadedMessages.isEmpty()) {
                val userName = prefs[UserPreferencesRepository.NAME_KEY] ?: "Alex"
                loadedMessages = listOf(
                    ChatMessage(
                        role = "assistant",
                        content = "Hi $userName, I'm your Orbit Coach. I'm here whenever you need a calm place to think things through. How are you feeling right now?"
                    )
                )
                saveMessages(loadedMessages)
            }
            
            _messages.value = loadedMessages
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || _isLoading.value) return

        viewModelScope.launch {
            val currentList = _messages.value.toMutableList()
            currentList.add(ChatMessage(role = "user", content = text))
            _messages.value = currentList
            saveMessages(currentList)

            _isLoading.value = true

            try {
                val prefs = repository.preferencesFlow.first()
                val habit = prefs[UserPreferencesRepository.HABIT_KEY] ?: "a habit"
                val name = prefs[UserPreferencesRepository.NAME_KEY] ?: "Alex"
                val streak = 0 
                
                val systemPrompt = "You are Orbit Coach — a warm, non-judgmental recovery companion. The user is working on: $habit. Their name is $name. They have been free for $streak days. Your role: listen, validate, and guide gently. Keep responses short (2–3 short paragraphs max). Never shame. Celebrate small wins. End with a gentle question or encouragement. If they mention crisis or self-harm, compassionately direct them to emergency services."

                val apiMessages = _messages.value.map { ApiMessage(role = it.role, content = it.content) }
                
                val request = AnthropicRequest(
                    system = systemPrompt,
                    messages = apiMessages
                )
                
                val response = apiService.sendMessage(request)
                val responseText = response.content.firstOrNull()?.text ?: "I heard you, but I couldn't formulate a response."
                
                val newList = _messages.value.toMutableList()
                newList.add(ChatMessage(role = "assistant", content = responseText))
                _messages.value = newList
                saveMessages(newList)
            } catch (e: Exception) {
                val newList = _messages.value.toMutableList()
                newList.add(ChatMessage(role = "assistant", content = "I'm having trouble connecting right now. Take a breath — you've got this."))
                _messages.value = newList
                saveMessages(newList)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun saveMessages(msgs: List<ChatMessage>) {
        val limitedMessages = if (msgs.size > 50) msgs.takeLast(50) else msgs
        val json = gson.toJson(limitedMessages)
        repository.saveCoachMessages(json)
    }
}
