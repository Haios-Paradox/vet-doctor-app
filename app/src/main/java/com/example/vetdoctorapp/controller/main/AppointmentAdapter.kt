package com.example.vetdoctorapp.controller.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vetdoctorapp.databinding.ItemRowAppointmentBinding
import com.example.vetdoctorapp.model.data.Appointment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject

class AppointmentAdapter(private val appointment: List<DocumentSnapshot>): RecyclerView.Adapter<ViewHolderAppointment>() {

    val appointments = appointment.map{it.toObject<Appointment>()}.sortedBy { it?.timestamp }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAppointment {
        val binding = ItemRowAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolderAppointment(binding)
    }

    override fun getItemCount(): Int {
        return appointment.count()
    }

    override fun onBindViewHolder(holder: ViewHolderAppointment, position: Int) {
        with(holder){
            with(binding){
                tvRowAppoDesc.text = appointments[position]?.description
                tvRowAppoName.text = appointments[position]?.patientName
                if(appointments[position]?.complete==true)
                    tvStatus.text = "COMPLETE"
                else
                    tvStatus.text = "ONGOING"
            }
        }
    }
}

class ViewHolderAppointment(val binding : ItemRowAppointmentBinding): RecyclerView.ViewHolder(binding.root)