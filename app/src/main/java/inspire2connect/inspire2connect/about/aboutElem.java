package inspire2connect.inspire2connect.about;

import android.content.Context;

public class aboutElem {
    public String name;
    public String tag;
    public String url;

    public aboutElem(Context context, int name, int tag, int url) {
        this.name = context.getString(name);
        this.tag = context.getString(tag);
        this.url = context.getString(url);
    }
}
