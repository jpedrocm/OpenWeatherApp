package br.com.jpedrocm.openweatherapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.jpedrocm.openweatherapp.models.CityModel;
import br.com.jpedrocm.openweatherapp.R;
import br.com.jpedrocm.openweatherapp.utils.Utils;

public class CityListAdapter extends ArrayAdapter<CityModel> {

    private ArrayList<CityModel> cities;

    public CityListAdapter(Context context, int resource, ArrayList<CityModel> cities) {
        super(context, resource, cities);
        this.cities = cities;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        ViewHolder holder;
        View view = convertView;

        if(view==null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_city_list, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        CityModel city = cities.get(position);

        holder.textViewName.setText(city.getName());
        holder.textViewTemp.setText(Utils.getTempString(city.getCurrentTemp(), Utils.TEMP_UNIT.Celsius));

        return view;
    }

    private class ViewHolder {
        TextView textViewName;
        TextView textViewTemp;

        private ViewHolder(View v){
            textViewName = (TextView) v.findViewById(R.id.tv_city_name);
            textViewTemp = (TextView) v.findViewById(R.id.tv_city_temp);
        }
    }
}