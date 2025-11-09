package com.example.todoapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.viewmodel.TodoViewModel
import com.example.todoapp.viewmodel.TodoFilter
import com.example.todoapp.model.Todo
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Arrangement


@Composable
fun TodoScreen(vm: TodoViewModel = viewModel()) {

    // Collect state dari ViewModel
    val todos by vm.todos.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val currentFilter by vm.currentFilter.collectAsState()
    val activeTodosCount by vm.activeTodosCount.collectAsState()
    val completedTodosCount by vm.completedTodosCount.collectAsState()

    // Text untuk input tugas
    var text by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // TextField untuk search
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { vm.updateSearchQuery(it) },
            label = { Text("Cari tugas...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = currentFilter == TodoFilter.ALL,
                onClick = { vm.setFilter(TodoFilter.ALL) },
                label = { Text("Semua") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = currentFilter == TodoFilter.ACTIVE,
                onClick = { vm.setFilter(TodoFilter.ACTIVE) },
                label = { Text("Aktif") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = currentFilter == TodoFilter.COMPLETED,
                onClick = { vm.setFilter(TodoFilter.COMPLETED) },
                label = { Text("Selesai") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Counter untuk Tugas Aktif dan Selesai
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Aktif: $activeTodosCount")
            Text(text = "Selesai: $completedTodosCount")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TextField untuk input tugas
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Tambah tugas...") },
            modifier = Modifier.fillMaxWidth()
        )

        // Tombol tambah
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    vm.addTask(text.trim())
                    text = ""
                }
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            Text("Tambah")
        }

        Divider()

        // List Todo
        LazyColumn {
            items(todos) { todo ->
                TodoItem(
                    todo = todo,
                    onToggle = { vm.toggleTask(todo.id) },
                    onDelete = { vm.deleteTask(todo.id) }
                )
            }
        }
    }
}
