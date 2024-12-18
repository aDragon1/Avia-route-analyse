package self.adragon.aviarouteanalyse.utils

import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener


class SearchViewHelper(val airportNames: List<Pair<String, Int>>) {

    fun setSearchViewListener(searchView: SearchView, onSubmit: (String?) -> Unit) {

        val autocompleteId = androidx.appcompat.R.id.search_src_text
        val searchAutoCompleteTextView =
            searchView.findViewById<AutoCompleteTextView>(autocompleteId)
        searchAutoCompleteTextView.threshold = 1

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(query: String?): Boolean {
                val q = query?.trim() ?: ""
                val names = if (q.isEmpty()) emptyList() else airportNames

                val cursor = createSuggestionsCursor(q, names)
                searchView.suggestionsAdapter.changeCursor(cursor)

                return true
            }
        })

        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            changeSearchViewCursorVisibility(v as SearchView, hasFocus)
        }

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val clickedRow = searchView.suggestionsAdapter.getItem(position) as Cursor

                val i1 = SearchManager.SUGGEST_COLUMN_TEXT_1

                val queryColIndex = clickedRow.getColumnIndex(i1)
                val clickedItem =
                    clickedRow.getString(if (queryColIndex > 0) queryColIndex else return false)
                searchView.setQuery(clickedItem, false)

                onSubmit(clickedItem)

                searchView.clearFocus()
                return true
            }
        })

        val closeButton: View? = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeButton?.setOnClickListener {
            searchView.setQuery("", false)
            onSubmit("")
        }
    }

    private fun createSuggestionsCursor(
        query: String?,
        airportNames: List<Pair<String, Int>>
    ): Cursor {
        val id1 = BaseColumns._ID
        val id2 = SearchManager.SUGGEST_COLUMN_TEXT_1
        val id3 = SearchManager.SUGGEST_COLUMN_TEXT_2

        val arr = arrayOf(id1, id2, id3)
        val matrixCursor = MatrixCursor(arr)

        var i = 0
        airportNames.filter { it.first.contains(query ?: "", ignoreCase = true) }
            .forEach { el ->
                val name = el.first
                val index = el.second
                matrixCursor.addRow(arrayOf(i++, name, index))
            }
        return matrixCursor
    }

    private fun changeSearchViewCursorVisibility(searchView: SearchView, isCursorVisible: Boolean) {
        val searchTextView =
            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchTextView.isCursorVisible = isCursorVisible
    }
}