package com.hn_2452.shoes_nike.ui.profile.change_info

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import coil.load
import com.google.firebase.storage.FirebaseStorage
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentChangeInfoBinding
import com.hn_2452.shoes_nike.utility.handleResource
import com.hn_2452.shoes_nike.utility.toDayString
import com.hn_2452.shoes_nike.utility.toTime
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Calendar


@AndroidEntryPoint
class ChangeInfoFragment : BaseFragment<FragmentChangeInfoBinding>() {

    private val mViewModel: ChangeInfoViewModel by viewModels()

    private var mNewAvatar: String? = null
    private var mCurrentAvatar: String? = null
    private var mChangeAvatarFlag = false


    companion object {
        const val TAG = "Nike:ChangeInfoFragment: "
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onStart() {
        super.onStart()
        setupBottomBar(false)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentChangeInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.materialToolbar)
        setupLoading(mBinding?.loadingProgress)
        setupGenderList()
        loadUserData()
        setupBirthDay()
        setupUploadUserInfo()
        setupGetImage()
    }

    private fun setupGetImage() {
        mBinding?.edtAvatar?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data
            selectedImageUri?.let {
                val inputStream: InputStream =
                    requireActivity().contentResolver.openInputStream(selectedImageUri) ?: return
                val bitmap = BitmapFactory.decodeStream(inputStream)
                mChangeAvatarFlag = true
                mBinding?.imvUser?.setImageBitmap(bitmap)
            }
        }
    }

    private fun setupUploadUserInfo() {
        mBinding?.btnUpdate?.setOnClickListener {
            var fullName: String? = mBinding?.edtFullName?.editText?.text?.trim().toString()
            var birthDay: String? = mBinding?.edtBirthday?.editText?.text?.trim().toString()
            var phoneNumber: String? = mBinding?.edtPhoneNumber?.editText?.text?.trim().toString()
            val genderPosition = mBinding?.gender?.selectedItemPosition

            if (fullName?.isEmpty() == true) {
                fullName = null
            }

            if (birthDay?.isEmpty() == true) {
                birthDay = null
            }

            if (phoneNumber?.isEmpty() == true) {
                phoneNumber = null
            }

            if (mChangeAvatarFlag) {
                startLoading()
                val baos = ByteArrayOutputStream()
                mBinding?.imvUser?.drawToBitmap()?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val avatarRef =
                    FirebaseStorage.getInstance().reference.child("avatar/" + "avatar-${System.currentTimeMillis()}")
                avatarRef.putBytes(baos.toByteArray())
                    .addOnCompleteListener { task ->
                        mCurrentAvatar?.let {
                            try {
                                FirebaseStorage.getInstance().getReferenceFromUrl(it).delete()
                                    .addOnCompleteListener {
                                        Log.i(TAG, "setupUploadUserInfo: delete success")
                                    }
                                    .addOnFailureListener {
                                        Log.e(TAG, "setupUploadUserInfo: ${it.message}")
                                    }
                            } catch (ex: Exception) {
                                Log.e(TAG, "setupUploadUserInfo: ${ex.message}")
                            }
                        }
                        avatarRef.downloadUrl
                            .addOnCompleteListener {
                                mNewAvatar = it.result.toString()
                                updateInfo(
                                    mNewAvatar,
                                    fullName,
                                    birthDay?.toTime()?.toString(),
                                    genderPosition ?: 0,
                                    phoneNumber
                                )
                            }.addOnFailureListener {
                                Log.e(TAG, "setupUploadUserInfo: ${it.message}", )
                            }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "onActivityResult: ${it.message}")
                    }
            } else {
                updateInfo(
                    null,
                    fullName,
                    birthDay?.toTime()?.toString(),
                    genderPosition ?: 0,
                    phoneNumber
                )
            }


        }
    }

    private fun updateInfo(
        avatar: String?,
        fullName: String?,
        birthDay: String?,
        genderPosition: Int,
        phoneNumber: String?
    ) {
        handleResource(
            data = mViewModel.updateUser(
                avatar,
                fullName,
                birthDay,
                genderPosition.toString(),
                phoneNumber
            ),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            onSuccess = {
                stopLoading()
                if (it == true) {
                    mChangeAvatarFlag = false
                    Toast.makeText(
                        requireContext(),
                        "Cập nhật thành công thông tin",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadUserData()
                } else {
                    Log.e(TAG, "setupUploadUserInfo: update success")
                }
            },
            isErrorInform = true,
            onLoading = {

            },
            onError = {
                stopLoading()
            }
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBirthDay() {
        mBinding?.edtBirthday?.editText?.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                showDatePickerDialog()
            }
            true
        }
    }

    private fun showDatePickerDialog() {
        val year: Int = Calendar.getInstance().get(Calendar.YEAR)
        val month: Int = Calendar.getInstance().get(Calendar.MONTH)
        val day: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, _year, _month, dayOfMonth -> // Handle the selected date
                val selectedDate = getTimeInMillis(dayOfMonth, _month, _year).toDayString()
                mBinding?.edtBirthday?.editText?.setText(selectedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun loadUserData() {
        handleResource(
            data = mViewModel.loadUserData(),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            isErrorInform = true,
            onError = {
                stopLoading()
            },
            onLoading = {
                startLoading()
            },
            onSuccess = { user ->
                stopLoading()
                user?.let {
                    mViewModel.updateUser(user)
                    mCurrentAvatar = user.avatar
                    mBinding?.imvUser?.load(user.avatar) {
                        error(R.drawable.user_placeholder)
                        placeholder(R.drawable.user_placeholder)
                    }
                    mBinding?.edtUserName?.editText?.setText(user.name)
                    mBinding?.edtFullName?.editText?.setText(user.fullName)
                    mBinding?.edtPhoneNumber?.editText?.setText(user.phoneNumber)
                    mBinding?.edtBirthday?.editText?.setText(user.birthDay?.toDayString())

                    if (user.gender != null) {
                        when (user.gender) {
                            0 -> {
                                mBinding?.gender?.setSelection(0)
                            }

                            1 -> {
                                mBinding?.gender?.setSelection(1)
                            }

                            else -> {
                                mBinding?.gender?.setSelection(2)
                            }
                        }
                    }
                }
            }
        )
    }

    private fun setupGenderList() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.genders_array, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding?.gender?.adapter = adapter
    }

    fun getTimeInMillis(dayOfMonth: Int, month: Int, year: Int): Long {
        // Month is 0-based in Calendar, so subtract 1
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth

        // Set the time to midnight (00:00:00) for the specified date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        // Get the timestamp in milliseconds
        return calendar.getTimeInMillis()
    }


}