package com.example.mobile_development_lab_06_verbose_commits

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mobile_development_lab_06_verbose_commits.databinding.FragmentCrimeBinding
import java.util.Date
import java.util.UUID

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"

class CrimeFragment : Fragment(), DatePickerFragment.Callbacks{

    private lateinit var crime: Crime
    private var _binding: FragmentCrimeBinding? = null
    private val binding get() = _binding!!
    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
        val crimeId : UUID = arguments?.getSerializable(ARG_CRIME_ID, UUID::class.java)!!
        Log.d(TAG, "args bundle crime ID: $crimeId")
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                Log.d(TAG, "Crime id: $crimeId")
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimeBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем crimeId из аргументов
        val crimeId: UUID? = arguments?.getSerializable(ARG_CRIME_ID, UUID::class.java)

        // Проверяем, что crimeId не null и загружаем данные
        crimeId?.let {
            crimeDetailViewModel.loadCrime(it) // Загружаем преступление по ID
        }

        parentFragmentManager.setFragmentResultListener(DIALOG_DATE, viewLifecycleOwner) { _, bundle ->
            val date = bundle.getSerializable(ARG_DATE, Date::class.java)
            date?.let {
                crime.date = it
                updateUI()
            }
        }

        // Наблюдаем за изменениями в crimeLiveData
        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner) { crime ->
            crime?.let {
                this.crime = it
                updateUI()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.crimeTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {}
        })

        binding.crimeSolved.setOnCheckedChangeListener { _, isChecked ->
            crime.isSolved = isChecked
        }

        binding.crimeDate.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).show(parentFragmentManager, DIALOG_DATE)
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }

    private fun updateUI() {
        binding.crimeTitle.setText(crime.title)
        binding.crimeDate.text = crime.date.toString()
        binding.crimeSolved.isChecked = crime.isSolved
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}