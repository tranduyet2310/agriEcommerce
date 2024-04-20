package com.example.argiecommerce.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.KeywordAdapter
import com.example.argiecommerce.databinding.FragmentSearchBinding
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.Constants.HISTORY_DATA

@SuppressLint("ClickableViewAccessibility")
class SearchFragment : Fragment(), AdapterView.OnItemClickListener,
    AdapterView.OnItemLongClickListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var keywordAdapter: KeywordAdapter
    private lateinit var list: ArrayList<String>
    private lateinit var sharedPreferences: SharedPreferences
    private var word: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.search)

        setupView()
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefChangeListener)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.wordList.onItemClickListener = this
        binding.wordList.onItemLongClickListener = this

        binding.clearAll.setOnClickListener {
            clearAll(it)
        }

        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }

        setListenerForQuery()
    }
    private fun setListenerForQuery() {
        binding.editQuery.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.editQuery.text.toString().trim().length == 1) {
                    binding.editQuery.clearFocus()
                    binding.editQuery.requestFocus()
                    binding.editQuery.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_back_24, 0, R.drawable.ic_close, 0
                    )
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        binding.editQuery.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else false
        }

        binding.editQuery.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (event.rawX >= binding.editQuery.right - binding.editQuery.compoundDrawables[2].bounds.width()) {
                    binding.editQuery.text.clear()
                    true
                } else if ((event.rawX + binding.editQuery.paddingLeft) <= (binding.editQuery.compoundDrawables[0].bounds.width() + binding.editQuery.left)) {
                    navController.navigate(R.id.action_searchFragment_to_homeFragment)
                    true
                } else false
            } else false
        }
    }

    fun performSearch() {
        word = binding.editQuery.text.toString().trim()
        setKeyword(requireContext(), word, word)
        val b = Bundle().apply {
            putParcelable(Constants.CATEGORY_KEY, null)
            putParcelable(Constants.SUBCATEGORY_KEY, null)
            putString(
                Constants.TITLE_KEY,
                requireContext().resources.getString(R.string.search_result)
            )
            putString(Constants.SEARCH_KEY, word)
        }
        navController.navigate(R.id.action_searchFragment_to_seeAllFragment, b)
    }

    private fun setupView() {
        list = ArrayList(getKeywords(requireContext()).keys)
        keywordAdapter = KeywordAdapter(requireContext(), list)
        binding.wordList.apply {
            divider = null
            adapter = keywordAdapter
        }
        keywordAdapter.notifyDataSetChanged()
        if (list.isEmpty()) {
            binding.imgQuestion.visibility = View.VISIBLE
        } else binding.imgQuestion.visibility = View.GONE
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val b = Bundle().apply {
            putParcelable(Constants.CATEGORY_KEY, null)
            putParcelable(Constants.SUBCATEGORY_KEY, null)
            putString(
                Constants.TITLE_KEY,
                requireContext().resources.getString(R.string.search_result)
            )
            putString(Constants.SEARCH_KEY, list.get(position))
        }
        navController.navigate(R.id.action_searchFragment_to_seeAllFragment, b)
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        val context = requireContext()
        word = list.get(position)
        clearOneItemInSharedPreferences(word, requireContext())
        keywordAdapter.remove(word)
        Toast.makeText(context, context.resources.getString(R.string.remove_successfully), Toast.LENGTH_SHORT).show()
        return true
    }

    fun getKeywords(context: Context): Map<String, *> {
        sharedPreferences = context.getSharedPreferences(HISTORY_DATA, Context.MODE_PRIVATE)
        return sharedPreferences.all
    }

    fun setKeyword(context: Context, key: String, word: String) {
        sharedPreferences = context.getSharedPreferences(HISTORY_DATA, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, word)
        editor.apply()
    }

    fun clearSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences(HISTORY_DATA, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    fun clearOneItemInSharedPreferences(key: String, context: Context) {
        context.getSharedPreferences(HISTORY_DATA, Context.MODE_PRIVATE)
            .edit()
            .remove(key)
            .apply()
    }

    fun clearAll(view: View) {
        clearSharedPreferences(requireContext())
        keywordAdapter.clear()
        val context = requireContext()
        Toast.makeText(context, context.resources.getString(R.string.remove_successfully), Toast.LENGTH_SHORT).show()
    }

    private val prefChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (!keywordAdapter.isEmpty && key != null) {
                if (key.equals(word)) {
                    keywordAdapter.clear()
                    val list = ArrayList(getKeywords(requireContext()).keys)
                    keywordAdapter.addAll(list)
                    binding.wordList.adapter = keywordAdapter
                }
            }
        }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(prefChangeListener)
    }
    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(prefChangeListener)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}