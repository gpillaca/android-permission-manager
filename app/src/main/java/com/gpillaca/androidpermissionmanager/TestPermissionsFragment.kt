package com.gpillaca.androidpermissionmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gpillaca.androidpermissionmanager.utils.permission.FragmentPermissionManager
import com.gpillaca.androidpermissionmanager.utils.permission.PermissionWrapper

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TestPermissionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestPermissionsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val permissionManager = FragmentPermissionManager.from(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_permissions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
    }

    private fun checkPermissions() {
        permissionManager.request(PermissionWrapper.LocationPermission)
            .rationaleTitle(getString(R.string.PERMISSION_REQUIRED_TITLE))
            .rationaleMessage(getString(R.string.PERMISSION_REQUIRED_MESSAGE))
            .checkPermission { isGranted ->
                when {
                    isGranted -> {
                        Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestPermissionsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}