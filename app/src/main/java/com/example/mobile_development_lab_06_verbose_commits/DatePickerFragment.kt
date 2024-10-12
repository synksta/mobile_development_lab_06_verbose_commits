package com.example.mobile_development_lab_06_verbose_commits

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface Callbacks {
        fun onDateSelected(date: Date)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateListener = { _: DatePicker, year: Int, month: Int, day: Int ->
            // Создаем дату из выбранных значений
            val resultDate: Date = GregorianCalendar(year, month, day).time

            // Отправляем результат обратно в CrimeFragment
            val resultBundle = Bundle().apply {
                putSerializable(ARG_DATE, resultDate)
            }

            setFragmentResult(DIALOG_DATE, resultBundle)
        }

        val date: Date? = arguments?.getSerializable(ARG_DATE, Date::class.java)

        val calendar = Calendar.getInstance()

        if (date != null) {
            calendar.time = date
        }

        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }.time

        // Отправляем результат обратно в CrimeFragment
        val resultBundle = Bundle().apply {
            putSerializable(ARG_DATE, date)
        }

        setFragmentResult(DIALOG_DATE, resultBundle)
    }

    companion object {
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }
            return DatePickerFragment().apply { arguments = args }
        }
    }
}
