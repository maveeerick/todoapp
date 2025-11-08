package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import com.example.todoapp.model.Todo
class TodoViewModel : ViewModel() {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery

    // Combine todos and search query to produce filtered results
    val todos: StateFlow<List<Todo>> = combine(_todos, _searchQuery) { todosList, query ->
        if (query.isBlank()) {
            todosList
        } else {
            todosList.filter { todo ->
                todo.title.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addTask(title: String) {
        val nextId = (_todos.value.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Todo(id = nextId, title = title)
        _todos.value = _todos.value + newTask
    }
    fun toggleTask(id: Int) {
        _todos.value = _todos.value.map { t ->
            if (t.id == id) t.copy(isDone = !t.isDone) else t
        }
    }
    fun deleteTask(id: Int) {
        _todos.value = _todos.value.filterNot { it.id == id }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}