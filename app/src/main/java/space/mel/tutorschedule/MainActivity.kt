package space.mel.tutorschedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import space.mel.tutorschedule.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //TODO: Касается всех фрагментов и вьюмоделей.
    // Переменные и функции должны быть объявлены в таком порядке:
    // 1. override переменные
    // 2. обычные публичные переменные
    // 3. приватные переменные
    // 4. override функции
    // 5. обычные публичные функции
    // 6. приватные функции
    // 7. companion object (если есть)

   private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}