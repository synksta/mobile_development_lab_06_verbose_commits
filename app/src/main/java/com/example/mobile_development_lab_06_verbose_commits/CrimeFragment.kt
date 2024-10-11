package com.example.mobile_development_lab_06_verbose_commits

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobile_development_lab_06_verbose_commits.databinding.FragmentCrimeBinding

class CrimeFragment : Fragment() {

    private lateinit var crime: Crime
    private var _binding: FragmentCrimeBinding? = null // Объявляем переменную для binding
    private val binding get() = _binding!! // Создаем геттер для безопасного доступа

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimeBinding.inflate(inflater, container, false) // Инициализация binding
        return binding.root // Возвращаем корневой элемент из binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Используем binding для доступа к элементам UI
        binding.crimeTitle.setText(crime.title) // Устанавливаем текст заголовка
        binding.crimeDate.apply {
            text = crime.date.toString()
            isEnabled = false
        }

        binding.crimeSolved.isChecked = crime.isSolved // Устанавливаем состояние чекбокса

        // Добавляем слушатель на изменения текста заголовка
        binding.crimeTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) { }
        })

        // Добавляем слушатель на изменения состояния чекбокса
        binding.crimeSolved.setOnCheckedChangeListener { _, isChecked ->
            crime.isSolved = isChecked
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Освобождаем память при уничтожении представления
    }
}
