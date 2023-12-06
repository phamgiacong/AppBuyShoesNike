package com.hn_2452.shoes_nike.ui.cart.payment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.ggpaykotlin.util.PaymentsUtil
import com.example.ggpaykotlin.viewModel.CheckoutViewModel
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.button.ButtonOptions
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.data.model.Cart
import com.hn_2452.shoes_nike.data.model.Receipt
import com.hn_2452.shoes_nike.databinding.DialogOrderSuccessBinding
import com.hn_2452.shoes_nike.databinding.FragmentConfirmCartBinding
import com.hn_2452.shoes_nike.databinding.FragmentPaymentBinding
import com.hn_2452.shoes_nike.ui.cart.CartViewModel
import com.hn_2452.shoes_nike.ui.cart.ReceiptViewModel
import com.hn_2452.shoes_nike.ui.cart.ShoesToCartViewModel
import com.hn_2452.shoes_nike.utility.Status
import org.json.JSONException
import org.json.JSONObject

class PaymentFragment:BaseFragment<FragmentPaymentBinding>(){
    private val googlePayRequestCode = 1001
    private var _mBinding: FragmentPaymentBinding? = null
    private val model: CheckoutViewModel by viewModels()
    protected val  binding  get() = _mBinding!!
    private var cart1 = Cart()
    private val receiptViewModel:ReceiptViewModel by lazy {
        ViewModelProvider(this,ReceiptViewModel.ReceiptViewModelFactory(requireActivity().application))[
                ReceiptViewModel::class.java
        ]
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(this, CartViewModel.CartViewModelFactory(requireActivity().application))[
            CartViewModel::class.java
        ]
    }
    private val shoesToCartViewModel: ShoesToCartViewModel by lazy{
        ViewModelProvider(this, ShoesToCartViewModel.ShoesToCartViewModelFactory(requireActivity().application))[
            ShoesToCartViewModel::class.java
        ]
    }
    var resolvePaymentForResult = registerForActivityResult<IntentSenderRequest, ActivityResult>(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result: ActivityResult ->
        when (result.resultCode) {
            AppCompatActivity.RESULT_OK -> {
                val resultData = result.data
                if (resultData != null) {
                    val paymentData = PaymentData.getFromIntent(result.data!!)
                    paymentData?.let { extractPaymentBillingName(it) }
                }
            }

            AppCompatActivity.RESULT_CANCELED -> {

            }
        }
    }
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPaymentBinding.inflate(inflater,container,false)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mBinding = getViewBinding(inflater,container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCart("123")
        model.canUseGooglePay.observe(viewLifecycleOwner,this::setGooglePayAvailable)
        try {
            binding.ggpayButton.initialize(ButtonOptions.newBuilder().setAllowedPaymentMethods(PaymentsUtil.allowedPaymentMethods.toString()).build())
            binding.ggpayButton.setOnClickListener(this::requestPayment)
        }catch (e:JSONException){

        }
        binding.confirmPayment.setOnClickListener({
        })

    }
    private fun getCart(idU:String){
        cartViewModel.getCart(idU).observe(viewLifecycleOwner,{
            it?.let { resoucce ->
                when(resoucce.status){
                    Status.SUCCESS ->{
                        resoucce.data?.let {
                                cart ->
                            cart1 =cart
                        }
                    }
                    Status.ERROR ->{
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING->{

                    }
                }
            }
        })
    }
    private fun setGooglePayAvailable(available:Boolean){
        if(available){
            binding.ggpayButton.visibility=View.VISIBLE
        }else{

        }
    }

    public fun requestPayment(view:View){
        binding.ggpayButton.isClickable = false
        val dummyPriceCents: Double = 100.0
        val shippingCostCents: Double = 900.0
        val totalPriceCents = dummyPriceCents + shippingCostCents
        var task = model.getLoadPaymentDataTask(cart1.totalPrice)
        AutoResolveHelper.resolveTask(
            task,requireActivity(),googlePayRequestCode
        )
    }



    //su ly sau khi thanh toan
    private fun extractPaymentBillingName(paymentData: PaymentData): String? {
        val paymentInformation = paymentData.toJson()

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData =
                JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            val billingName = paymentMethodData.getJSONObject("info")
                .getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)

            // Logging token string.
            Log.d(
                "Google Pay token", paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token")
            )

            for(i in cart1.idShoesToCart!!){
                shoesToCartViewModel.deleteShoesToCartById(i).observe(viewLifecycleOwner,{
                })
            }
            cart1._id?.let {
                cartViewModel.deleteCart(it).observe(viewLifecycleOwner,{
                })
            }
            var receipt = Receipt(null,cart1,0,null)
            receiptViewModel.createReceipt(receipt).observe(viewLifecycleOwner,{
                it?.let { resource ->
                    when(resource.status){
                        Status.SUCCESS->{
                            resource.data?.let {
                                    receipt ->
                                Log.e("TAG", "Receipt:  $receipt", )
                                showDialogSuccess()
                            }
                        }Status.LOADING ->{
                        }Status.ERROR ->{
                        }
                    }
                }
            })


            return billingName
        } catch (error: JSONException) {
            Log.e("handlePaymentSuccess", "Error: $error")
        }

        return null
    }
    private fun showDialogSuccess(){
        var dialog:Dialog = Dialog(requireContext())
        var viewBindding : DialogOrderSuccessBinding = DialogOrderSuccessBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(viewBindding.root)
        dialog.show()
    }
}