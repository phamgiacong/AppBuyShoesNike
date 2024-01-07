package com.hn_2452.shoes_nike.ui.profile.change_info

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import coil.load
import com.hn_2452.shoes_nike.BASE_URL
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentChangeInfoBinding
import com.hn_2452.shoes_nike.utility.getTimeOfDay
import com.hn_2452.shoes_nike.utility.handleResource
import com.hn_2452.shoes_nike.utility.toDayString
import com.hn_2452.shoes_nike.utility.toTime
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.parse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.io.InputStream
import java.util.Calendar


@AndroidEntryPoint
class ChangeInfoFragment : BaseFragment<FragmentChangeInfoBinding>() {

    private val mViewModel: ChangeInfoViewModel by viewModels()
    lateinit var mImage:  MultipartBody.Part


    companion object {
        const val TAG = "Nike:ChangeInfoFragment: "
        private const val PICK_IMAGE_REQUEST = 1
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
                createRequestBody(selectedImageUri)
                val inputStream: InputStream = requireActivity().contentResolver.openInputStream(selectedImageUri) ?: return
                val bitmap = BitmapFactory.decodeStream(inputStream)
                mBinding?.imvUser?.setImageBitmap(bitmap)
            }
        }
    }

    private fun createRequestBody(imageUri: Uri?) {
        try {
            imageUri ?: return
            val inputStream: InputStream =
                requireActivity().contentResolver.openInputStream(imageUri) ?: return
            // Convert InputStream to byte array
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes)
            inputStream.close()

            // Create RequestBody
            val requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), bytes)
            mImage = MultipartBody.Part.createFormData("image", "image.jpg", requestBody)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setupUploadUserInfo() {
        val userName = mBinding?.edtUserName?.editText?.text.toString()
        val fullName = mBinding?.edtFullName?.editText?.text.toString()
        val birthDay = mBinding?.edtBirthday?.editText?.text.toString()
        val phoneNumber = mBinding?.edtPhoneNumber?.editText?.text.toString()
        val genderPosition = mBinding?.gender?.selectedItemPosition


        val nameReq : RequestBody? = if(userName.isNotEmpty()) {
            userName.toRequestBody()
        } else {
            null
        }

        val fullNameReq : RequestBody? = if(fullName.isNotEmpty()) {
            fullName.toRequestBody()
        } else {
            null
        }

        val birthDayReq: RequestBody? = if(birthDay.toTime() != -1L) {
           birthDay.toTime().toString().toRequestBody()
        } else {
            null
        }

        val phoneNumberReq : RequestBody? = if(phoneNumber.isNotEmpty()) {
            phoneNumber.toRequestBody()
        } else {
            null
        }

        val genderReq: RequestBody = genderPosition.toString().toRequestBody()

        handleResource(
            data = mViewModel.updateUser(mImage, nameReq, fullNameReq, birthDayReq, phoneNumberReq, genderReq),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            onSuccess = {
                if(it == true) {
                    Toast.makeText(requireContext(), "Cập nhật thành công thông tin", Toast.LENGTH_SHORT).show()
                    loadUserData()
                } else {
                    Log.e(TAG, "setupUploadUserInfo: update success",)
                }
            },
            isErrorInform = true
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
            { _, year, month, dayOfMonth -> // Handle the selected date
                Calendar.getInstance().set(year, month + 1, dayOfMonth)
                val selectedDate = Calendar.getInstance().time.time.toDayString()
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

                    if (user.accountType == 0) {
                        mBinding?.imvUser?.load(BASE_URL + user.avatar) {
                            error(R.drawable.user_placeholder)
                        }
                    } else {
                        mBinding?.imvUser?.load(user.avatar) {
                            error(R.drawable.user_placeholder)
                        }
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


}