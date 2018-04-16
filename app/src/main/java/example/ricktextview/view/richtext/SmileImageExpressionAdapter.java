package example.ricktextview.view.richtext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import example.ricktextview.R;


public class SmileImageExpressionAdapter extends ArrayAdapter<String> {

    public SmileImageExpressionAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.layout_row_expression, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expression);

        String filename = getItem(position);
        if ("delete_expression".equals(filename)) {
            int resId = getContext().getResources().getIdentifier(filename, "drawable", getContext().getPackageName());
            imageView.setImageResource(resId);
        } else {
            int resId = SmileUtils.getRedId(filename);
            imageView.setImageResource(resId);
        }
        return convertView;
    }

}
