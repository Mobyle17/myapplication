package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.db.DatabaseProvider
import com.example.myapplication.data.db.SeedData
import com.example.myapplication.data.db.entities.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.get(this)

        setContent {
            MaterialTheme {

                var categories by remember { mutableStateOf<List<CategoryEntity>>(emptyList()) }
                var status by remember { mutableStateOf("Загрузка...") }

                LaunchedEffect(Unit) {
                    // 1) Засеваем категории, если база пустая
                    withContext(Dispatchers.IO) {
                        val count = db.categoryDao().count()
                        if (count == 0) {
                            db.categoryDao().insertAll(SeedData.defaultCategories())
                        }
                    }

                    // 2) Подписываемся на изменения
                    db.categoryDao().observeActive().collect { list ->
                        categories = list
                        status = "Категорий: ${list.size}"
                    }
                }

                Text(
                    text = status,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn(modifier = Modifier.padding(top = 48.dp)) {
                    items(categories) { c ->
                        Text(
                            text = "• ${c.name}",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
