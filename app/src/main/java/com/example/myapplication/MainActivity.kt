package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.db.DatabaseProvider
import com.example.myapplication.data.db.SeedData
import com.example.myapplication.data.db.entities.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(this)

        setContent {
            MaterialTheme {
                val scope = rememberCoroutineScope()

                // сидирование 1 раз
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) { SeedData.seedIfEmpty(db) }
                }

                // список из Flow
                val products by db.productDao()
                    .getAllProducts()
                    .collectAsState(initial = emptyList())

                var showAddDialog by remember { mutableStateOf(false) }
                var selectedProduct by remember { mutableStateOf<ProductEntity?>(null) }
                var query by remember { mutableStateOf("") }
                var sortMode by remember { mutableStateOf(SortMode.NAME) }

                val filteredProducts by remember(products, query, sortMode) {
                    derivedStateOf {
                        val filtered = if (query.isBlank()) {
                            products
                        } else {
                            products.filter { it.name.contains(query, ignoreCase = true) }
                        }

                        when (sortMode) {
                            SortMode.NAME -> filtered.sortedBy { it.name.lowercase() }
                            SortMode.CALORIES -> filtered.sortedByDescending { it.caloriesPer100g }
                        }
                    }
                }

                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = { showAddDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Добавить")
                        }
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            placeholder = { Text("Поиск продукта") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = sortMode == SortMode.NAME,
                                onClick = { sortMode = SortMode.NAME },
                                label = { Text("А-Я") }
                            )
                            FilterChip(
                                selected = sortMode == SortMode.CALORIES,
                                onClick = { sortMode = SortMode.CALORIES },
                                label = { Text("Ккал") }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
                            items(filteredProducts) { product ->
                                Text(
                                    text = "${product.name} — ${product.caloriesPer100g} ккал / 100г",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedProduct = product }
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                if (showAddDialog) {
                    AddProductDialog(
                        onDismiss = {showAddDialog = false },
                        onSave = { name, calories, protein, fat, carbs ->
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    db.productDao().insert(
                                        ProductEntity(
                                            name = name.trim(),
                                            caloriesPer100g = calories,
                                            proteinPer100g = protein,
                                            fatPer100g = fat,
                                            carbsPer100g = carbs
                                        )
                                    )
                                }
                            }
                        }
                    )
                }

                // ✅ карточка продукта показывается только если selectedProduct != null
                selectedProduct?.let { product ->
                    ProductDetailsDialog(
                        product = product,
                        onDismiss = { selectedProduct = null }
                    )
                }
            }
        }
    }
}

private enum class SortMode { NAME, CALORIES }

@Composable
private fun AddProductDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, calories: Int, protein: Float, fat: Float, carbs: Float) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новый продукт") },
        text = {
            Column {
                if (error != null) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") }
                )

                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Калории, ккал/100г") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("Белки, г/100г") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text("Жиры, г/100г") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it },
                    label = { Text("Углеводы, г/100г") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val c = calories.toIntOrNull()
                val p = protein.toFloatOrNull()
                val f = fat.toFloatOrNull()
                val cb = carbs.toFloatOrNull()

                error = when {
                    name.isBlank() -> "Введите название"
                    c == null || p == null || f == null || cb == null -> "Заполните все поля числами"
                    c < 0 || p < 0 || f < 0 || cb < 0 -> "Числа должны быть ≥ 0"
                    else -> null
                }

                if (error == null) onSave(name, c!!, p!!, f!!, cb!!)
            }) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@Composable
private fun ProductDetailsDialog(
    product: ProductEntity,
    onDismiss: () -> Unit
) {
    val protein = String.format(Locale.getDefault(), "%.1f", product.proteinPer100g)
    val fat = String.format(Locale.getDefault(), "%.1f", product.fatPer100g)
    val carbs = String.format(Locale.getDefault(), "%.1f", product.carbsPer100g)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(product.name) },
        text = {
            Column {
                Text("Калории: ${product.caloriesPer100g} ккал / 100 г")
                Text("Белки: $protein г / 100 г")
                Text("Жиры: $fat г / 100 г")
                Text("Углеводы: $carbs г / 100 г")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Закрыть") }
        }
    )
}