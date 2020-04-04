package com.example.proj3_475;

public class DownloadTask_TB extends DownloadTask {
    ParseJSONActivity myActivity;

    DownloadTask_TB(ParseJSONActivity activity) {
        attach(activity);
    }

    @Override
    protected void onPostExecute(String result) {
        if (myActivity != null) {
            myActivity.processJSON(result);
        }
    }

    /**
     * important do not hold a reference so garbage collector can grab old
     * defunct dying activity
     */
    void detach() {
        myActivity = null;
    }

    /**
     * @param activity
     *            grab a reference to this activity, mindful of leaks
     */
    void attach(ParseJSONActivity activity) {
        this.myActivity = activity;
    }
}