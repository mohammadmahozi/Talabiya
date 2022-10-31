package com.mahozi.sayed.talabiya.order.details.info

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.order.OrderViewModel
import com.mahozi.sayed.talabiya.order.details.info.CustomEditText.CustomEditTextInterface
import com.mahozi.sayed.talabiya.order.store.OrderEntity
import com.mahozi.sayed.talabiya.order.view.create.DatePickerPopUp
import com.mahozi.sayed.talabiya.order.view.create.TimePickerPopUp
import com.mahozi.sayed.talabiya.person.PersonFragment
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class OrderInfoFragment : Fragment() {
  private lateinit var dateEditText: EditText
  private lateinit var timeEditText: EditText
  private lateinit var totalEditText: EditText
  private lateinit var payerEditText: EditText
  private lateinit var statusEditText: EditText
  private lateinit var noteEditText: CustomEditText
  private lateinit var statusCheckBox: CheckBox
  private lateinit var receiptImageButton: ImageButton
  private lateinit var orderViewModel: OrderViewModel
  private lateinit var currentPath: String
  lateinit var currentOrder: OrderEntity

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_order_info, container, false)
    orderViewModel = ViewModelProviders.of(requireActivity())[OrderViewModel::class.java]

    currentOrder = orderViewModel.currentOrder

    dateEditText = view.findViewById(R.id.fragment_order_details_date)
    dateEditText.setText(currentOrder.date)
    dateEditText.setOnClickListener(View.OnClickListener {
      val datePick = DatePickerPopUp { date ->
        dateEditText.setText(date)
        currentOrder.date = date
        orderViewModel.updateOrder(currentOrder)
      }
      datePick.show(requireActivity().supportFragmentManager, "DatePicker")
    })

    timeEditText = view.findViewById(R.id.fragment_order_details_time)
    timeEditText.setText(currentOrder.time)
    timeEditText.setOnClickListener(View.OnClickListener {
      val timePick = TimePickerPopUp { time ->
        timeEditText.setText(time)
        currentOrder.time = time
        orderViewModel.updateOrder(currentOrder)
      }
      timePick.show(requireActivity().supportFragmentManager, "TimePicker")
    })

    totalEditText = view.findViewById(R.id.fragment_order_details_total)
    totalEditText.setText(currentOrder.total.toString())

    payerEditText = view.findViewById(R.id.fragment_order_details_payer)
    payerEditText.setText(currentOrder.payer)
    payerEditText.setOnClickListener(View.OnClickListener { startPersonFragment() })

    statusEditText = view.findViewById(R.id.fragment_order_details_status)
    statusEditText.setText(if (currentOrder.clearance_date == null) resources.getString(R.string.not_payed) else currentOrder.clearance_date)
    statusCheckBox = view.findViewById(R.id.fragment_order_details_status_check_box)
    statusCheckBox.setChecked(currentOrder.status)
    statusCheckBox.setOnTouchListener(OnTouchListener { v, event ->
      if (event.action == MotionEvent.ACTION_DOWN) {
        onStatusCheckBoxClick()
        return@OnTouchListener true
      }
      false
    })

    noteEditText = view.findViewById(R.id.fragment_order_details_note)
    noteEditText.setText(currentOrder.note)
    noteEditText.setCustomEditTextInterface(CustomEditTextInterface {
      currentOrder.note = noteEditText.getText().toString().trim { it <= ' ' }
      orderViewModel.updateOrder(currentOrder)
      Toast.makeText(context, R.string.note_updated, Toast.LENGTH_LONG).show()
      noteEditText.clearFocus()
    })
    noteEditText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        val imm =
          requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        currentOrder.note = noteEditText.getText().toString().trim { it <= ' ' }
        orderViewModel.updateOrder(currentOrder)
        Toast.makeText(context, R.string.note_updated, Toast.LENGTH_LONG).show()
        noteEditText.clearFocus()
        return@OnEditorActionListener true
      }
      false
    })

    receiptImageButton = view.findViewById(R.id.receipt_image_button)
    if (currentOrder.receiptPath != null) {
      val height = resources.getDimensionPixelSize(R.dimen.thumbnail_height)
      val width = resources.getDimensionPixelSize(R.dimen.thumbnail_width)
      val thumbImage = ThumbnailUtils.extractThumbnail(
        BitmapFactory.decodeFile(currentOrder.receiptPath),
        width,
        height
      )
      receiptImageButton.setImageBitmap(thumbImage)
    }
    receiptImageButton.setOnClickListener(View.OnClickListener {
      if (currentOrder.receiptPath == null) {
        currentPath = dispatchTakePictureIntent()
      } else {
        showImageInGallery()
      }
    })
    receiptImageButton.setOnLongClickListener(OnLongClickListener {
      onReceiptImageButtonLongClick()
      false
    })
    return view
  }

  private fun startPersonFragment() {
    Dispatchers.Main
    val personFragment = PersonFragment()
    val bundle = Bundle()
    bundle.putBoolean("getPerson", true)
    personFragment.target = this
    personFragment.arguments = bundle
    requireActivity().supportFragmentManager
      .beginTransaction()
      .replace(R.id.order_container, personFragment)
      .commit()
  }

  @JvmField
  var listener: Listener = object : Listener {
    override fun onName(name: String) {
      currentOrder.payer = name
      orderViewModel.updateOrder(currentOrder)
      payerEditText.setText(name)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == REQUEST_TAKE_PHOTO) {
      if (resultCode == Activity.RESULT_OK) {
        if (currentOrder.receiptPath != null) File(currentOrder.receiptPath).delete()
        currentOrder.receiptPath = currentPath
        //update the path in the database if the picture is taken
        orderViewModel.updateOrder(currentOrder)
        val height = resources.getDimensionPixelSize(R.dimen.thumbnail_height)
        val width = resources.getDimensionPixelSize(R.dimen.thumbnail_width)
        val thumbImage = ThumbnailUtils.extractThumbnail(
          BitmapFactory.decodeFile(currentPath),
          width,
          height
        )
        receiptImageButton.setImageBitmap(thumbImage)
      }
      if (resultCode == Activity.RESULT_CANCELED) {
        if (currentOrder.receiptPath == null) {
          File(currentPath).delete()
        }
      }
    }
  }

  private fun onStatusCheckBoxClick() {
    //if it was checked and got unchecked. the condition happens after the click and the chnage
    if (statusCheckBox.isChecked) {
      AlertDialog.Builder(requireActivity())
        .setTitle(R.string.confirm).setMessage(R.string.status_to_not_payed)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton(android.R.string.yes) { dialog, whichButton ->
          currentOrder.status = false
          currentOrder.clearance_date = resources.getString(R.string.not_payed)
          orderViewModel.updateOrder(currentOrder)
          statusEditText.setText(resources.getString(R.string.not_payed))
          statusCheckBox.isChecked = false
        }
        .setNegativeButton(android.R.string.no, null).show()
    } else {
      val datePick = DatePickerPopUp { date ->
        currentOrder.status = true
        currentOrder.clearance_date = date
        orderViewModel.updateOrder(currentOrder)
        statusCheckBox.isChecked = true
        statusEditText.setText(date)
      }
      datePick.show(requireActivity().supportFragmentManager, "DatePicker")
    }
  }

  @Throws(IOException::class)
  private fun createImageFile(): File {
    // Create an image file name
    val timeStamp =
      SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir =
      requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    // Save a file: path for use with ACTION_VIEW intents
    //currentPhotoPath = image.getAbsolutePath();
    return File.createTempFile(
      imageFileName,  /* prefix */
      ".jpg",  /* suffix */
      storageDir /* directory */
    )
  }

  private fun dispatchTakePictureIntent(): String {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    // Ensure that there's a camera activity to handle the intent
    if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
      // Create the File where the photo should go
      var photoFile: File? = null
      try {
        photoFile = createImageFile()
      } catch (ex: IOException) {
        // Error occurred while creating the File
      }
      // Continue only if the File was successfully created
      if (photoFile != null) {
        val photoURI = FileProvider.getUriForFile(
          requireContext(),
          "com.mahozi.sayed.talabiya.FileProvider",
          photoFile
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        return photoFile.absolutePath
      }
    }
    return ""
  }

  private fun showImageInGallery() {
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    val f = File(currentOrder.receiptPath)
    val uri =
      FileProvider.getUriForFile(requireContext(), "com.mahozi.sayed.talabiya.FileProvider", f)
    intent.setDataAndType(uri, "image/*")
    startActivity(intent)
  }

  private fun onReceiptImageButtonLongClick() {
    if (currentOrder.receiptPath != null) {
      showContextMenuForImage()
    }
  }

  private fun showContextMenuForImage() {
    val items = arrayOf<CharSequence>(
      resources.getString(R.string.edit),
      resources.getString(R.string.delete)
    )
    val builder = AlertDialog.Builder(
      requireActivity()
    )
    builder.setItems(items) { dialog, item ->
      if (item == 0) {
        currentPath = dispatchTakePictureIntent()
      } else if (item == 1) {
        AlertDialog.Builder(requireActivity())
          .setTitle(R.string.confirm).setMessage(R.string.delete_receipt_picture)
          .setIcon(android.R.drawable.ic_dialog_alert)
          .setPositiveButton(android.R.string.yes) { dialog, whichButton ->
            receiptImageButton.setImageResource(R.mipmap.attach_icon)
            File(currentOrder.receiptPath).delete()
            currentOrder.receiptPath = null
            orderViewModel.updateOrder(currentOrder)
          }
          .setNegativeButton(android.R.string.no, null).show()
      }
    }
    builder.show()
  }

  companion object {
    const val REQUEST_TAKE_PHOTO = 2
  }
}