package com.kaushalya.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaushalya.app.databinding.FragmentProfileBinding
import com.kaushalya.app.ui.activities.WorkerDetailActivity
import com.kaushalya.app.ui.auth.LoginActivity
import com.kaushalya.app.utils.SessionManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionManager(requireContext())

        binding.tvName.text = session.getUserName()
        binding.tvEmail.text = session.getUserEmail()
        binding.tvUserType.text = session.getUserType()

        if (session.isWorker() && session.getWorkerId() != -1) {
            binding.btnViewMyProfile.visibility = View.VISIBLE
            binding.btnViewMyProfile.setOnClickListener {
                val intent = Intent(requireContext(), WorkerDetailActivity::class.java).apply {
                    putExtra("worker_id", session.getWorkerId())
                    putExtra("is_owner", true)
                }
                startActivity(intent)
            }
        }

        binding.btnLogout.setOnClickListener {
            session.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
