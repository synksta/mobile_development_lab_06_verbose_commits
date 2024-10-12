package com.example.mobile_development_lab_06_verbose_commits

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_development_lab_06_verbose_commits.databinding.FragmentCrimeListBinding
import com.example.mobile_development_lab_06_verbose_commits.databinding.ListItemCrimeBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Date
import java.util.UUID

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks // Используем безопасный приведение типов
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    private var _binding: FragmentCrimeListBinding? = null // Объявляем переменную для binding
    private val binding get() = _binding ?: throw IllegalStateException("Binding should not be null") // Безопасный доступ

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this)[CrimeListViewModel::class.java] // Используем ViewModelProvider
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)

        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.crimeRecyclerView.adapter = adapter

        return binding.root // Возвращаем корневой элемент из binding
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(crimes: List<Crime>) {
        adapter?.crimes = crimes // Обновляем данные в существующем адаптере
        adapter?.notifyDataSetChanged() // Уведомляем адаптер об изменениях
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes: ${crimes.size}") // Логируем количество преступлений
                    updateUI(crimes)
                }
            }
        )
    }

    private inner class CrimeHolder(private val binding: ListItemCrimeBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var crime: Crime

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            binding.crimeTitle.text = this.crime.title

            // Форматируем дату
            val formattedDate = formatDate(crime.date)
            binding.crimeDate.text = formattedDate

            binding.crimeSolved.visibility =
                if (crime.isSolved) View.VISIBLE else View.GONE
        }

        private fun formatDate(date: Date): String {
            // Форматируем дату в виде "Monday, Jul 22, 2019"
            val dayOfWeek = DateFormat.format("EEEE", date).toString() // Получаем день недели
            val monthDayYear = DateFormat.format("MMM dd, yyyy", date).toString() // Форматируем оставшуюся часть даты
            return "$dayOfWeek, $monthDayYear" // Объединяем строки
        }

        override fun onClick(v: View) {
            Snackbar.make(v, "${this.crime.title} pressed!", Snackbar.LENGTH_SHORT).show() // Используем Snackbar вместо Toast
            Log.d(TAG, "${crime.title} clicked!")
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val binding = ListItemCrimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CrimeHolder(binding)
        }

        override fun getItemCount() = crimes.size

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Освобождаем память при уничтожении представления
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}
