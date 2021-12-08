package algonquin.cst2335.recycler;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private static ArrayList<User> usersList;


    /**
     * Recycler adapter constructor for userslist
     * @param userslist
     */
    public RecyclerAdapter(ArrayList<User> userslist) {

        this.usersList = userslist;
    }


    /**
     * Implemented Adapter methods
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String id = usersList.get(position).getID();
        String type = usersList.get(position).getType();
        String attributes = usersList.get(position).getAttributes();
        holder.idTxt.setText(id);
        holder.typeTxt.setText(type);
        holder.attTxt.setText(attributes);


    }

    @Override
    public int getItemCount() {

        return usersList.size();
    }


    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * Declare variables
         */
        private TextView idTxt;
        private TextView typeTxt;
        private TextView attTxt;
        private TextView distanceTxt;

        public MyViewHolder(View view) {
            super(view);
            idTxt = view.findViewById(R.id.ID);
            typeTxt = view.findViewById(R.id.Type);
            attTxt = view.findViewById(R.id.Attributes);
            distanceTxt = view.findViewById(R.id.distanceText);


            /**
             * Alert dialog when selecting vehicle make
             */
            view.setOnClickListener(click -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(click.getContext());
                    builder.setTitle("Select vehicle make")
                            .setMessage("Here are the details of the vehicle you selected, Would you like to select this make?")
                            .setNegativeButton("No", (dialog, cl) -> {})
                            .setPositiveButton("Yes", (dialog, cl) -> {  });
                            builder.create().show();


            });
        }

    }

    /**
     * Filtered list method to update recycler view
     * @param filteredList
     */
    public void filterList(ArrayList<User> filteredList) {
        usersList = filteredList;
        notifyDataSetChanged();


    }
}

