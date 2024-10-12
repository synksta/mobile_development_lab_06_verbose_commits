package com.example.mobile_development_lab_06_verbose_commits

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_development_lab_06_verbose_commits.databinding.FragmentCrimeListBinding
import com.example.mobile_development_lab_06_verbose_commits.databinding.ListItemCrimeBinding
import java.util.Date
import java.util.UUID

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }
    private var callbacks: Callbacks? = null

    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    private var _binding: FragmentCrimeListBinding? = null // Объявляем переменную для binding
    private val binding get() = _binding!! // Создаем геттер для безопасного доступа

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this)[CrimeListViewModel::class.java] // Используем ViewModelProvider
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
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

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeAdapter(crimes)
        binding.crimeRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем menuHost из активности
        val menuHost: MenuHost = requireActivity()

        // Добавляем MenuProvider
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_crime_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.new_crime -> {
                        Log.d("CrimeListFragment", "New crime clicked!")
                        val crime: Crime = Crime()
                        crimeListViewModel.addCrime(crime)
                        callbacks?.onCrimeSelected(crime.id)
                        true
                    }
//                    else -> return super.onOptionsItemSelected(item)
                    else -> false

                }
            }
        }, viewLifecycleOwner)

        // Наблюдаем за изменениями в crimeListLiveData
        crimeListViewModel.crimeListLiveData.observe(viewLifecycleOwner) { crimes ->
            crimes?.let {
                Log.i(TAG, "Got crimes: ${crimes.size}")
                updateUI(crimes)
            }
        }
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
                if (crime.isSolved) View.VISIBLE
                else View.GONE
        }

        private fun formatDate(date: Date): String {
            val dayOfWeek = DateFormat.format("EEEE", date).toString() // Получаем день недели
            val monthDayYear = DateFormat.format("MMM dd, yyyy", date).toString() // Форматируем оставшуюся часть даты
            return "$dayOfWeek, $monthDayYear" // Объединяем строки
        }

        override fun onClick(v: View) {
            Log.d("CrimeListFragment", "${crime.title} clicked!")
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val binding = ListItemCrimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CrimeHolder(binding)
        }
        override fun getItemCount() =
            crimes.size
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
