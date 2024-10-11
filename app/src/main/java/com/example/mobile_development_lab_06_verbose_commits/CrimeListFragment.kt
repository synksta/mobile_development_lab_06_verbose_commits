package com.example.mobile_development_lab_06_verbose_commits

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_development_lab_06_verbose_commits.databinding.FragmentCrimeListBinding
import com.example.mobile_development_lab_06_verbose_commits.databinding.ListItemCrimeBinding

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private var adapter: CrimeAdapter? = null

    private var _binding: FragmentCrimeListBinding? = null // Объявляем переменную для binding
    private val binding get() = _binding!! // Создаем геттер для безопасного доступа

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this)[CrimeListViewModel::class.java] // Используем ViewModelProvider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Инициализация binding
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)

        // Установка LayoutManager для RecyclerView
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        // Установка адаптера для RecyclerView
        binding.crimeRecyclerView.adapter = CrimeAdapter(crimeListViewModel.crimes)

        updateUI()

        return binding.root // Возвращаем корневой элемент из binding
    }

    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        binding.crimeRecyclerView.adapter = adapter
    }

    private inner class CrimeHolder(private val binding: ListItemCrimeBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var crime: Crime

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            binding.crimeTitle.text = this.crime.title
            binding.crimeDate.text = this.crime.date.toString()
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${this.crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class CrimeAdapter(private val crimes: List<Crime>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        private val VIEW_TYPE_NORMAL = 0


        override fun getItemViewType(position: Int): Int {
            return VIEW_TYPE_NORMAL
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val binding = ListItemCrimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CrimeHolder(binding)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val crime = crimes[position]
            when (holder) {
                is CrimeHolder -> holder.bind(crime)
            }
        }

        override fun getItemCount(): Int {
            return crimes.size
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