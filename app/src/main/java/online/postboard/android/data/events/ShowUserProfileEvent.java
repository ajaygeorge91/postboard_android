package online.postboard.android.data.events;

/**
 * Created by ajayg on 9/27/2017.
 */

public class ShowUserProfileEvent {

    private int adapterPosition;

    public ShowUserProfileEvent(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }
}
