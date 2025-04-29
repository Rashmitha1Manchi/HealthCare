import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.healthcare.R

class CartAdapter(
    private val context: Context,
    private val items: ArrayList<HashMap<String, String>>,
    private val onRemoveClick: (position: Int) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): Any = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.multi_lines1, parent, false)

        val line1 = view.findViewById<TextView>(R.id.textViewLine1)
        val line2 = view.findViewById<TextView>(R.id.textViewLine2)
        val line5 = view.findViewById<TextView>(R.id.textViewLine5)
        val btnRemove = view.findViewById<Button>(R.id.buttonRemove)

        line1.text = items[position]["line1"]
        line2.text = items[position]["line2"]
        line5.text = items[position]["line5"]

        btnRemove.setOnClickListener {
            onRemoveClick(position)
        }

        return view
    }
}